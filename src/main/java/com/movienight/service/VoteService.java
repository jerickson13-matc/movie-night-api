package com.movienight.service;

import com.movienight.entity.Movie;
import com.movienight.entity.MovieNight;
import com.movienight.entity.Vote;
import com.movienight.persistence.GenericDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * REST service for managing votes for a movie night.
 */
@Path("/movie-nights/{movieNightId}/votes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VoteService {

    private final GenericDao<Vote> voteDao = new GenericDao<>(Vote.class);
    private final GenericDao<Movie> movieDao = new GenericDao<>(Movie.class);
    private final GenericDao<MovieNight> movieNightDao =
            new GenericDao<>(MovieNight.class);

    private final VoteCalculator voteCalculator = new VoteCalculator();

    /**
     * Retrieve all votes for a movie night.
     *
     * @param movieNightId the movie night ID
     * @return HTTP response containing the votes
     */
    @GET
    public Response getVotes(
            @PathParam("movieNightId") long movieNightId) {

        MovieNight movieNight = movieNightDao.getById(movieNightId);

        if (movieNight == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(message("Movie night not found"))
                    .build();
        }

        List<Map<String, Object>> votes = new ArrayList<>();

        for (Vote vote : voteDao.getAll()) {
            if (vote.getMovieNight() != null
                    && vote.getMovieNight().getId() == movieNightId) {

                votes.add(createVoteResponse(vote));
            }
        }

        return Response.ok(votes).build();
    }

    /**
     * Submit a vote for a movie night.
     *
     * @param movieNightId the movie night ID
     * @param request the vote request
     * @return HTTP response containing the created vote
     */
    @POST
    public Response addVote(
            @PathParam("movieNightId") long movieNightId,
            VoteRequest request) {

        if (request == null
                || request.getMovieId() == null
                || request.getVoterName() == null
                || request.getVoterName().isBlank()) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(message("Movie ID and voter name are required"))
                    .build();
        }

        if (request.getVoterName().length() > 100) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(message("Voter name cannot exceed 100 characters"))
                    .build();
        }

        MovieNight movieNight = movieNightDao.getById(movieNightId);

        if (movieNight == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(message("Movie night not found"))
                    .build();
        }

        if (!"OPEN".equalsIgnoreCase(movieNight.getStatus())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(message("Voting is closed for this movie night"))
                    .build();
        }

        Movie movie = movieDao.getById(request.getMovieId());

        if (movie == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(message("Movie not found"))
                    .build();
        }

        boolean nominatedMovie = false;

        for (Movie nominated : movieNight.getMovies()) {
            if (nominated.getId().equals(movie.getId())) {
                nominatedMovie = true;
                break;
            }
        }

        if (!nominatedMovie) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(message(
                            "Movie is not nominated for this movie night"))
                    .build();
        }

        String voterName = request.getVoterName().trim();

        for (Vote existingVote : voteDao.getAll()) {
            if (existingVote.getMovieNight() != null
                    && existingVote.getMovieNight().getId() == movieNightId
                    && existingVote.getVoterName()
                    .equalsIgnoreCase(voterName)) {

                return Response.status(Response.Status.CONFLICT)
                        .entity(message(
                                "Voter has already voted in this movie night"))
                        .build();
            }
        }

        Vote vote = new Vote(movieNight, movie, voterName);

        long id = voteDao.insert(vote);
        vote.setId(id);

        return Response.status(Response.Status.CREATED)
                .entity(createVoteResponse(vote))
                .build();
    }

    /**
     * Delete a vote from a movie night.
     *
     * @param movieNightId the movie night ID
     * @param voteId the vote ID
     * @return HTTP response indicating the result
     */
    @DELETE
    @Path("/{voteId}")
    public Response deleteVote(
            @PathParam("movieNightId") long movieNightId,
            @PathParam("voteId") long voteId) {

        Vote vote = voteDao.getById(voteId);

        if (vote == null
                || vote.getMovieNight() == null
                || vote.getMovieNight().getId() != movieNightId) {

            return Response.status(Response.Status.NOT_FOUND)
                    .entity(message("Vote not found"))
                    .build();
        }

        voteDao.delete(vote);

        return Response.noContent().build();
    }

    /**
     * Calculate the voting results for a movie night.
     *
     * @param movieNightId the movie night ID
     * @return HTTP response containing vote totals and winner
     */
    @GET
    @Path("/results")
    public Response getResults(
            @PathParam("movieNightId") long movieNightId) {

        MovieNight movieNight = movieNightDao.getById(movieNightId);

        if (movieNight == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(message("Movie night not found"))
                    .build();
        }

        List<Vote> movieNightVotes = new ArrayList<>();

        for (Vote vote : voteDao.getAll()) {
            if (vote.getMovieNight() != null
                    && vote.getMovieNight().getId() == movieNightId) {

                movieNightVotes.add(vote);
            }
        }

        List<Map<String, Object>> results = new ArrayList<>();

        for (Movie movie : movieNight.getMovies()) {

            int voteCount =
                    voteCalculator.countVotesForMovie(
                            movie,
                            movieNightVotes);

            Map<String, Object> movieResult =
                    new LinkedHashMap<>();

            movieResult.put("movieId", movie.getId());
            movieResult.put("title", movie.getTitle());
            movieResult.put("votes", voteCount);

            results.add(movieResult);
        }

        List<Movie> winningMovies =
                voteCalculator.determineWinners(
                        movieNight.getMovies(),
                        movieNightVotes);

        int highestVoteCount = 0;

        if (!winningMovies.isEmpty()) {
            highestVoteCount =
                    voteCalculator.countVotesForMovie(
                            winningMovies.get(0),
                            movieNightVotes);
        }

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("movieNightId", movieNight.getId());
        response.put("movieNightName", movieNight.getName());
        response.put("totalVotes", movieNightVotes.size());
        response.put("results", results);

        if (movieNightVotes.isEmpty()) {
            response.put("winner", null);

        } else if (winningMovies.size() == 1) {

            Map<String, Object> winner =
                    new LinkedHashMap<>();

            winner.put(
                    "movieId",
                    winningMovies.get(0).getId());

            winner.put(
                    "title",
                    winningMovies.get(0).getTitle());

            winner.put(
                    "votes",
                    highestVoteCount);

            response.put("winner", winner);

        } else {

            response.put("winner", null);
            response.put("tie", true);

            List<String> tiedMovies = new ArrayList<>();

            for (Movie movie : winningMovies) {
                tiedMovies.add(movie.getTitle());
            }

            response.put("tiedMovies", tiedMovies);
        }

        return Response.ok(response).build();
    }

    /**
     * Convert a Vote entity into a simple JSON response.
     *
     * @param vote the vote
     * @return vote response data
     */
    private Map<String, Object> createVoteResponse(Vote vote) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("id", vote.getId());
        response.put(
                "movieNightId",
                vote.getMovieNight().getId());
        response.put(
                "movieId",
                vote.getMovie().getId());
        response.put(
                "movieTitle",
                vote.getMovie().getTitle());
        response.put(
                "voterName",
                vote.getVoterName());
        response.put(
                "voteDate",
                vote.getVoteDate().toString());

        return response;
    }

    /**
     * Create a simple message response.
     *
     * @param text message text
     * @return message data
     */
    private Map<String, String> message(String text) {

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", text);

        return response;
    }

    /**
     * Request data used when submitting a vote.
     */
    public static class VoteRequest {

        private Long movieId;
        private String voterName;

        /**
         * Instantiates a new VoteRequest.
         */
        public VoteRequest() {
        }

        /**
         * Gets movie ID.
         *
         * @return the movie ID
         */
        public Long getMovieId() {
            return movieId;
        }

        /**
         * Sets movie ID.
         *
         * @param movieId the movie ID
         */
        public void setMovieId(Long movieId) {
            this.movieId = movieId;
        }

        /**
         * Gets voter name.
         *
         * @return the voter name
         */
        public String getVoterName() {
            return voterName;
        }

        /**
         * Sets voter name.
         *
         * @param voterName the voter name
         */
        public void setVoterName(String voterName) {
            this.voterName = voterName;
        }
    }
}