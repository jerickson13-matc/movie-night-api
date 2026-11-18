package com.movienight.service;

import com.movienight.entity.Movie;
import com.movienight.persistence.GenericDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * REST service for managing movies.
 */
@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieService {
    private final GenericDao<Movie> dao = new GenericDao<>(Movie.class);

    /**
     * Retrieve all movies.
     *
     * @return list of movies
     */
    @GET
    public List<Movie> getAllMovies() {
        return dao.getAll();
    }

    /**
     * Retrieve a movie by ID.
     *
     * @param id the movie ID
     * @return HTTP response containing the movie
     */
    @GET
    @Path("/{id}")
    public Response getMovieById(@PathParam("id") Long id) {
        Movie movie = dao.getById(id);

        if (movie == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(movie).build();
    }

    /**
     * Add a new movie.
     *
     * @param movie the movie to add
     * @return HTTP response containing the new movie
     */
    @POST
    public Response addMovie(Movie movie) {
        if (!isValidMovie(movie)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        movie.setId(null);

        Long id = dao.insert(movie);
        movie.setId(id);

        return Response.status(Response.Status.CREATED)
                .entity(movie)
                .build();
    }

    /**
     * Update an existing movie.
     *
     * @param id the movie ID
     * @param movie the updated movie
     * @return HTTP response containing the updated movie
     */
    @PUT
    @Path("/{id}")
    public Response updateMovie(@PathParam("id") Long id, Movie movie) {
        Movie existingMovie = dao.getById(id);

        if (existingMovie == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!isValidMovie(movie)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        movie.setId(id);
        dao.update(movie);

        return Response.ok(movie).build();
    }

    /**
     * Delete an existing movie.
     *
     * @param id the movie ID
     * @return HTTP response indicating the result
     */
    @DELETE
    @Path("/{id}")
    public Response deleteMovie(@PathParam("id") Long id) {
        Movie movie = dao.getById(id);

        if (movie == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        dao.delete(movie);

        return Response.noContent().build();
    }

    /**
     * Validate required movie data.
     *
     * @param movie the movie to validate
     * @return true if the movie is valid
     */
    private boolean isValidMovie(Movie movie) {
        return movie != null
                && movie.getTitle() != null
                && !movie.getTitle().isBlank()
                && movie.getTitle().length() <= 255
                && movie.getGenre() != null
                && !movie.getGenre().isBlank()
                && movie.getGenre().length() <= 100
                && movie.getReleaseYear() != null
                && movie.getReleaseYear() > 0
                && movie.getRuntime() != null
                && movie.getRuntime() > 0;
    }
}
