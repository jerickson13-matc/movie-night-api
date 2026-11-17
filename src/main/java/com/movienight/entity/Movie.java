package com.movienight.entity;

import jakarta.persistence.*;

/**
 * Represents a movie.
 */
@Entity(name = "Movie")
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String genre;

    @Column(name = "release_year")
    private Integer releaseYear;

    private Integer runtime;

    private String description;

    /**
     * Instantiates a new Movie.
     */
    public Movie() {
    }

    /**
     * Instantiates a new Movie without an id.
     *
     * @param title the movie title
     * @param genre the movie genre
     * @param releaseYear the movie release year
     * @param runtime the movie runtime in minutes
     * @param description the movie description
     */
    public Movie(String title, String genre,
                 Integer releaseYear, Integer runtime,
                 String description) {
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.runtime = runtime;
        this.description = description;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets genre.
     *
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets genre.
     *
     * @param genre the genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets release year.
     *
     * @return the release year
     */
    public Integer getReleaseYear() {
        return releaseYear;
    }

    /**
     * Sets release year.
     *
     * @param releaseYear the release year
     */
    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Gets runtime.
     *
     * @return the runtime in minutes
     */
    public Integer getRuntime() {
        return runtime;
    }

    /**
     * Sets runtime.
     *
     * @param runtime the runtime in minutes
     */
    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseYear=" + releaseYear +
                ", runtime=" + runtime +
                ", description='" + description + '\'' +
                '}';
    }
}
