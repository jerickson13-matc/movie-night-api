package com.movienight.persistence;

import com.movienight.entity.Movie;
import com.movienight.entity.MovieNight;
import com.movienight.entity.Vote;
import com.movienight.test.util.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GenericDao handling Vote entities.
 */
public class VoteDaoTest {

    GenericDao<Vote> voteDao;
    GenericDao<Movie> movieDao;
    GenericDao<MovieNight> movieNightDao;

    Movie movie;
    MovieNight movieNight;
    Vote newVote;

    /**
     * Reset the test database before each test.
     */
    @BeforeEach
    void setUp() {
        Database database = Database.getInstance();
        database.runSQL("cleandb.sql");

        voteDao = new GenericDao<>(Vote.class);
        movieDao = new GenericDao<>(Movie.class);
        movieNightDao = new GenericDao<>(MovieNight.class);

        movie = new Movie(
                "Scream",
                "Horror",
                1996,
                111,
                "A masked killer targets a group of teenagers."
        );

        long movieId = movieDao.insert(movie);
        movie = movieDao.getById(movieId);

        movieNight = new MovieNight(
                "Horror Movie Night",
                LocalDate.of(2026, 11, 18),
                "JaQuan",
                "Vote for a horror movie.",
                "OPEN"
        );

        long movieNightId = movieNightDao.insert(movieNight);
        movieNight = movieNightDao.getById(movieNightId);

        newVote = new Vote(movieNight, movie, "Test Voter");
    }

    /**
     * Verify successful vote insertion.
     */
    @Test
    void insertSuccess() {
        long id = voteDao.insert(newVote);

        assertTrue(id > 0);

        Vote insertedVote = voteDao.getById(id);

        assertNotNull(insertedVote);
        assertEquals(newVote.getVoterName(), insertedVote.getVoterName());
        assertEquals(movie.getId(), insertedVote.getMovie().getId());
        assertEquals(movieNight.getId(), insertedVote.getMovieNight().getId());
        assertNotNull(insertedVote.getVoteDate());
    }

    /**
     * Verify successful retrieval of a vote by ID.
     */
    @Test
    void getByIdSuccess() {
        long id = voteDao.insert(newVote);

        Vote retrievedVote = voteDao.getById(id);

        assertNotNull(retrievedVote);
        assertEquals(id, retrievedVote.getId().longValue());
        assertEquals("Test Voter", retrievedVote.getVoterName());
    }

    /**
     * Verify successful retrieval of all votes.
     */
    @Test
    void getAllSuccess() {
        int originalSize = voteDao.getAll().size();

        voteDao.insert(newVote);

        List<Vote> votes = voteDao.getAll();

        assertEquals(originalSize + 1, votes.size());
    }

    /**
     * Verify successful vote update.
     */
    @Test
    void updateSuccess() {
        long id = voteDao.insert(newVote);

        Vote voteToUpdate = voteDao.getById(id);

        String newVoterName = "Updated Voter";
        voteToUpdate.setVoterName(newVoterName);

        voteDao.update(voteToUpdate);

        Vote updatedVote = voteDao.getById(id);

        assertEquals(newVoterName, updatedVote.getVoterName());
    }

    /**
     * Verify successful vote deletion.
     */
    @Test
    void deleteSuccess() {
        long id = voteDao.insert(newVote);

        Vote voteToDelete = voteDao.getById(id);

        voteDao.delete(voteToDelete);

        assertNull(voteDao.getById(id));
    }
}