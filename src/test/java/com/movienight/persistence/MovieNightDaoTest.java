package com.movienight.persistence;

import com.movienight.entity.MovieNight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.movienight.test.util.Database;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GenericDao handling MovieNight entities.
 */
public class MovieNightDaoTest {
    GenericDao<MovieNight> dao;
    MovieNight newMovieNight;

    /**
     * Reset the test database before each test.
     */
    @BeforeEach
    void setUp() {
        Database database = Database.getInstance();
        database.runSQL("cleandb.sql");
        dao = new GenericDao<>(MovieNight.class);

        newMovieNight = new MovieNight(
                "Sci-Fi Night",
                LocalDate.of(2026, 12, 1),
                "Dawson",
                "Space movie marathon",
                "OPEN"
        );
    }

    /**
     * Verify successful movie night insertion.
     */
    @Test
    void insertSuccess() {
        long id = dao.insert(newMovieNight);

        assertNotNull(id);
        assertTrue(id > 0);

        MovieNight insertedMovieNight = dao.getById(id);

        assertNotNull(insertedMovieNight);
        assertEquals(newMovieNight.getName(), insertedMovieNight.getName());
        assertEquals(newMovieNight.getEventDate(), insertedMovieNight.getEventDate());
        assertEquals(newMovieNight.getHost(), insertedMovieNight.getHost());
        assertEquals(newMovieNight.getDescription(), insertedMovieNight.getDescription());
        assertEquals(newMovieNight.getStatus(), insertedMovieNight.getStatus());
    }

    /**
     * Verify successful retrieval of a movie night by ID.
     */
    @Test
    void getByIdSuccess() {
        long id = dao.insert(newMovieNight);
        MovieNight retrievedMovieNight = dao.getById(id);

        assertNotNull(retrievedMovieNight);
        assertEquals(id, retrievedMovieNight.getId());
        assertEquals(newMovieNight.getName(), retrievedMovieNight.getName());
    }

    /**
     * Verify successful retrieval of all movie nights.
     */
    @Test
    void getAllSuccess() {
        int originalSize = dao.getAll().size();

        dao.insert(newMovieNight);

        List<MovieNight> movieNights = dao.getAll();

        assertEquals(originalSize + 1, movieNights.size());
    }

    /**
     * Verify successful movie night update.
     */
    @Test
    void updateSuccess() {
        Long id = dao.insert(newMovieNight);

        MovieNight movieNightToUpdate = dao.getById(id);

        String newStatus = "CLOSED";
        movieNightToUpdate.setStatus(newStatus);

        dao.update(movieNightToUpdate);

        MovieNight updatedMovieNight = dao.getById(id);

        assertEquals(newStatus, updatedMovieNight.getStatus());
    }

    /**
     * Verify successful movie night deletion.
     */
    @Test
    void deleteSuccess() {
        long id = dao.insert(newMovieNight);

        MovieNight movieNightToDelete = dao.getById(id);

        dao.delete(movieNightToDelete);

        assertNull(dao.getById(id));
    }

    /**
     * Verify that a movie night can be inserted without a description.
     */
    @Test
    void insertWithoutDescriptionSuccess() {
        newMovieNight.setDescription(null);

        long id = dao.insert(newMovieNight);
        MovieNight insertedMovieNight = dao.getById(id);

        assertNotNull(insertedMovieNight);
        assertNull(insertedMovieNight.getDescription());
    }
}
