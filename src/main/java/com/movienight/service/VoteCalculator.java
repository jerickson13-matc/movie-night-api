package com.movienight.service;

import com.movienight.entity.Movie;
import com.movienight.entity.Vote;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles vote counting and winner determination.
 */
public class VoteCalculator {

    /**
     * Counts the number of votes for a movie.
     *
     * @param movie movie to count votes for
     * @param votes votes to examine
     * @return number of votes for the movie
     */
    public int countVotesForMovie(Movie movie, List<Vote> votes) {

        int voteCount = 0;

        for (Vote vote : votes) {
            if (vote.getMovie() != null
                    && vote.getMovie().getId() != null
                    && movie != null
                    && movie.getId() != null
                    && vote.getMovie().getId().equals(movie.getId())) {

                voteCount++;
            }
        }

        return voteCount;
    }

    /**
     * Determines the winning movie or movies.
     *
     * @param movies nominated movies
     * @param votes votes submitted for the movie night
     * @return list containing the winner or tied winners
     */
    public List<Movie> determineWinners(
            List<Movie> movies,
            List<Vote> votes) {

        List<Movie> winners = new ArrayList<>();
        int highestVoteCount = 0;

        for (Movie movie : movies) {

            int voteCount =
                    countVotesForMovie(movie, votes);

            if (voteCount > highestVoteCount) {

                highestVoteCount = voteCount;

                winners.clear();
                winners.add(movie);

            } else if (voteCount == highestVoteCount
                    && voteCount > 0) {

                winners.add(movie);
            }
        }

        return winners;
    }
}