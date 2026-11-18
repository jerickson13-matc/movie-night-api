package com.movienight.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Represents a vote for a movie during a movie night.
 */
@Entity(name = "Vote")
@Table(name = "vote")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_night_id", nullable = false)
    private MovieNight movieNight;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "voter_name", nullable = false)
    private String voterName;

    @Column(name = "vote_date", nullable = false)
    private LocalDateTime voteDate;

    /**
     * Instantiates a new Vote.
     */
    public Vote() {
    }

    /**
     * Instantiates a new Vote without an id.
     *
     * @param movieNight the movie night
     * @param movie the movie receiving the vote
     * @param voterName the name of the voter
     */
    public Vote(MovieNight movieNight, Movie movie, String voterName) {
        this.movieNight = movieNight;
        this.movie = movie;
        this.voterName = voterName;
        this.voteDate = LocalDateTime.now();
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
     * Gets movie night.
     *
     * @return the movie night
     */
    public MovieNight getMovieNight() {
        return movieNight;
    }

    /**
     * Sets movie night.
     *
     * @param movieNight the movie night
     */
    public void setMovieNight(MovieNight movieNight) {
        this.movieNight = movieNight;
    }

    /**
     * Gets movie.
     *
     * @return the movie
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Sets movie.
     *
     * @param movie the movie
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
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

    /**
     * Gets vote date.
     *
     * @return the vote date
     */
    public LocalDateTime getVoteDate() {
        return voteDate;
    }

    /**
     * Sets vote date.
     *
     * @param voteDate the vote date
     */
    public void setVoteDate(LocalDateTime voteDate) {
        this.voteDate = voteDate;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", voterName='" + voterName + '\'' +
                ", voteDate=" + voteDate +
                '}';
    }
}