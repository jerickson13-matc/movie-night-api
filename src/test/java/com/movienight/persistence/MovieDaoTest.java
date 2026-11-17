package com.movienight.persistence;

import com.movienight.entity.Movie;
import com.movienight.test.util.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MovieDaoTest {

    GenericDao<Movie> dao;
    Movie newMovie;

    /**
     * Reset the test database before each test.
     */
    @BeforeEach
    void setUp() {
        Database database = Database.getInstance();
        database.runSQL("cleandb.sql");
        dao = new GenericDao<>(Movie.class);

        newMovie = new Movie(
                "Halloween",
                "Horror",
                1978,
                91,
                "Michael Myers returns to Haddonfield."
        );
    }

    /**
     * Verify successful movie insertion.
     */
    @Test
    void insertSuccess() {
        Long id = dao.insert(newMovie);

        assertNotNull(id);
        assertTrue(id > 0);

        Movie insertedMovie = dao.getById(id);

        assertNotNull(insertedMovie);
        assertEquals(newMovie.getTitle(), insertedMovie.getTitle());
        assertEquals(newMovie.getGenre(), insertedMovie.getGenre());
        assertEquals(newMovie.getReleaseYear(), insertedMovie.getReleaseYear());
        assertEquals(newMovie.getRuntime(), insertedMovie.getRuntime());
        assertEquals(newMovie.getDescription(), insertedMovie.getDescription());
    }

    /**
     * Verify successful retrieval of a movie by ID.
     */
    @Test
    void getByIdSuccess() {
        Long id = dao.insert(newMovie);
        Movie retrievedMovie = dao.getById(id);

        assertNotNull(retrievedMovie);
        assertEquals(id, retrievedMovie.getId());
        assertEquals(newMovie.getTitle(), retrievedMovie.getTitle());
    }

    /**
     * Verify successful retrieval of all movies.
     */
    @Test
    void getAllSuccess() {
        int originalSize = dao.getAll().size();

        dao.insert(newMovie);

        List<Movie> movies = dao.getAll();

        assertEquals(originalSize + 1, movies.size());
    }

    /**
     * Verify successful movie update.
     */
    @Test
    void updateSuccess() {
        Long id = dao.insert(newMovie);

        Movie movieToUpdate = dao.getById(id);

        String newTitle = "Halloween (1978)";
        movieToUpdate.setTitle(newTitle);

        dao.update(movieToUpdate);

        Movie updatedMovie = dao.getById(id);

        assertEquals(newTitle, updatedMovie.getTitle());
    }

    /**
     * Verify successful movie deletion.
     */
    @Test
    void deleteSuccess() {
        Long id = dao.insert(newMovie);

        Movie movieToDelete = dao.getById(id);

        dao.delete(movieToDelete);

        assertNull(dao.getById(id));
    }

    /**
     * Verify that a movie can be inserted without a description.
     */
    @Test
    void insertWithoutDescriptionSuccess() {
        newMovie.setDescription(null);

        Long id = dao.insert(newMovie);
        Movie insertedMovie = dao.getById(id);

        assertNotNull(insertedMovie);
        assertNull(insertedMovie.getDescription());
    }


}
