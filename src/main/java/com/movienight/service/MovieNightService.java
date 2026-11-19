package com.movienight.service;

import com.movienight.entity.MovieNight;
import com.movienight.persistence.GenericDao;
import com.movienight.entity.Movie;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

/**
 * REST service for managing movie nights.
 */
@Path("/movie-nights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieNightService {
    private final GenericDao<MovieNight> dao = new GenericDao<>(MovieNight.class);
    private static final Set<String> VALID_STATUSES = Set.of("OPEN", "CLOSED", "COMPLETED");
    private final GenericDao<Movie> movieDao = new GenericDao<>(Movie.class);

    /**
     * Retrieve all movie nights.
     *
     * @return list of movie nights
     */
    @GET
    public List<MovieNight> getAllMovieNights() {
        return dao.getAll();
    }

    /**
     * Retrieve a movie night by ID.
     *
     * @param id the movie night ID
     * @return HTTP response containing the movie night
     */
    @GET
    @Path("/{id}")
    public Response getMovieNightById(@PathParam("id") Long id) {
        MovieNight movieNight = dao.getById(id);

        if (movieNight == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(movieNight).build();
    }

    /**
     * Create a new movie night.
     *
     * @param movieNight the movie night to create
     * @return HTTP response containing the new movie night
     */
    @POST
    public Response createMovieNight(MovieNight movieNight) {
        if (!isValidMovieNight(movieNight)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        movieNight.setId(0L);

        long id = dao.insert(movieNight);
        movieNight.setId(id);

        return Response.status(Response.Status.CREATED)
                .entity(movieNight)
                .build();
    }

    /**
     * Update an existing movie night.
     *
     * @param id the movie night ID
     * @param movieNight the updated movie night
     * @return HTTP response containing the updated movie night
     */
    @PUT
    @Path("/{id}")
    public Response updateMovieNight(@PathParam("id") Long id, MovieNight movieNight) {
        MovieNight existingMovieNight = dao.getById(id);

        if (existingMovieNight == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!isValidMovieNight(movieNight)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        movieNight.setId(id);
        dao.update(movieNight);

        return Response.ok(movieNight).build();
    }

    /**
     * Delete an existing movie night.
     *
     * @param id the movie night ID
     * @return HTTP response indicating the result
     */
    @DELETE
    @Path("/{id}")
    public Response deleteMovieNight(@PathParam("id") Long id) {
        MovieNight movieNight = dao.getById(id);

        if (movieNight == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        dao.delete(movieNight);

        return Response.noContent().build();
    }

    /**
     * Retrieve nominated movies for a movie night.
     *
     * @param id the movie night ID
     * @return HTTP response containing the nominated movies
     */
    @GET
    @Path("/{id}/movies")
    public Response getNominatedMovies(@PathParam("id") Long id) {
        MovieNight movieNight = dao.getById(id);

        if (movieNight == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(movieNight.getMovies()).build();
    }

    /**
     * Nominate a movie for a movie night.
     *
     * @param id the movie night ID
     * @param movieInput the movie payload (with id or movie details)
     * @return HTTP response containing the updated movie night or nominated movies
     */
    @POST
    @Path("/{id}/movies")
    public Response nominateMovie(@PathParam("id") Long id, Movie movieInput) {
        MovieNight movieNight = dao.getById(id);

        if (movieNight == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (movieInput == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final Movie movieToNominate;
        if (movieInput.getId() != null && movieInput.getId() > 0) {
            Movie existingMovie = movieDao.getById(movieInput.getId());
            if (existingMovie == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            movieToNominate = existingMovie;
        } else if (movieInput.getTitle() != null && !movieInput.getTitle().isBlank()) {
            movieToNominate = movieInput;
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean alreadyNominated = movieNight.getMovies().stream()
                .anyMatch(m -> m.getId() != null && movieToNominate.getId() != null
                        && m.getId().equals(movieToNominate.getId()));

        if (!alreadyNominated) {
            movieNight.addMovie(movieToNominate);
            dao.update(movieNight);
        }

        return Response.status(Response.Status.CREATED)
                .entity(dao.getById(id).getMovies())
                .build();
    }

    /**
     * Validate required movie night data.
     *
     * @param movieNight the movie night to validate
     * @return true if the movie night is valid
     */
    private boolean isValidMovieNight(MovieNight movieNight) {
        return movieNight != null
                && movieNight.getName() != null
                && !movieNight.getName().isBlank()
                && movieNight.getName().length() <= 255
                && movieNight.getEventDate() != null
                && movieNight.getHost() != null
                && !movieNight.getHost().isBlank()
                && movieNight.getHost().length() <= 100
                && (movieNight.getDescription() == null || movieNight.getDescription().length() <= 1000)
                && movieNight.getStatus() != null
                && VALID_STATUSES.contains(movieNight.getStatus());
    }
}
