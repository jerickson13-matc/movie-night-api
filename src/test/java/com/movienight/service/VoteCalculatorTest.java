package com.movienight.service;

import com.movienight.entity.Movie;
import com.movienight.entity.MovieNight;
import com.movienight.entity.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for VoteCalculator.
 */
public class VoteCalculatorTest {

    private VoteCalculator voteCalculator;
    private Movie movieOne;
    private Movie movieTwo;
    private MovieNight movieNight;

    /**
     * Set up test data before each test.
     */
    @BeforeEach
    void setUp() {

        voteCalculator = new VoteCalculator();

        movieOne = new Movie(
                "Movie One",
                "Horror",
                2020,
                100,
                "First test movie"
        );

        movieOne.setId(1L);

        movieTwo = new Movie(
                "Movie Two",
                "Comedy",
                2021,
                110,
                "Second test movie"
        );

        movieTwo.setId(2L);

        movieNight = new MovieNight(
                "Test Movie Night",
                LocalDate.of(2026, 11, 18),
                "Test Host",
                "Testing voting logic",
                "OPEN"
        );
    }

    /**
     * Verify votes are counted correctly for a movie.
     */
    @Test
    void countVotesForMovieSuccess() {

        List<Vote> votes = new ArrayList<>();

        votes.add(
                new Vote(
                        movieNight,
                        movieOne,
                        "Voter One"
                )
        );

        votes.add(
                new Vote(
                        movieNight,
                        movieOne,
                        "Voter Two"
                )
        );

        votes.add(
                new Vote(
                        movieNight,
                        movieTwo,
                        "Voter Three"
                )
        );

        int voteCount =
                voteCalculator.countVotesForMovie(
                        movieOne,
                        votes
                );

        assertEquals(2, voteCount);
    }

    /**
     * Verify a single winner is determined correctly.
     */
    @Test
    void determineSingleWinnerSuccess() {

        List<Movie> movies =
                List.of(movieOne, movieTwo);

        List<Vote> votes = new ArrayList<>();

        votes.add(
                new Vote(
                        movieNight,
                        movieOne,
                        "Voter One"
                )
        );

        votes.add(
                new Vote(
                        movieNight,
                        movieOne,
                        "Voter Two"
                )
        );

        votes.add(
                new Vote(
                        movieNight,
                        movieTwo,
                        "Voter Three"
                )
        );

        List<Movie> winners =
                voteCalculator.determineWinners(
                        movies,
                        votes
                );

        assertEquals(1, winners.size());

        assertEquals(
                movieOne.getId(),
                winners.get(0).getId()
        );
    }

    /**
     * Verify a tie returns both winning movies.
     */
    @Test
    void determineTieSuccess() {

        List<Movie> movies =
                List.of(movieOne, movieTwo);

        List<Vote> votes = new ArrayList<>();

        votes.add(
                new Vote(
                        movieNight,
                        movieOne,
                        "Voter One"
                )
        );

        votes.add(
                new Vote(
                        movieNight,
                        movieTwo,
                        "Voter Two"
                )
        );

        List<Movie> winners =
                voteCalculator.determineWinners(
                        movies,
                        votes
                );

        assertEquals(2, winners.size());

        assertTrue(winners.contains(movieOne));
        assertTrue(winners.contains(movieTwo));
    }

    /**
     * Verify no winner is returned when there are no votes.
     */
    @Test
    void determineNoWinnerWithNoVotes() {

        List<Movie> movies =
                List.of(movieOne, movieTwo);

        List<Vote> votes = new ArrayList<>();

        List<Movie> winners =
                voteCalculator.determineWinners(
                        movies,
                        votes
                );

        assertTrue(winners.isEmpty());
    }
}