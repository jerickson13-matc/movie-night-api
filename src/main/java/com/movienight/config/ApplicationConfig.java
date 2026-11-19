package com.movienight.config;

import com.movienight.service.MovieNightService;
import com.movienight.service.MovieService;
import com.movienight.service.VoteService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Configures the base URI and registers REST service classes.
 */
@ApplicationPath("/api")
public class ApplicationConfig extends Application{
    /**
     * Registers the resource classes for the REST API.
     *
     * @return set of REST service classes
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(MovieService.class);
        classes.add(MovieNightService.class);
        classes.add(VoteService.class);
        return classes;
    }
}
