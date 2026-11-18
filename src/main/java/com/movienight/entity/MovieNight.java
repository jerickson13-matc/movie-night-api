package com.movienight.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The type MovieNight.
 */
@Entity(name = "MovieNight")
@Table(name = "movie_night")
public class MovieNight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "host")
    private String host;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    /*
     * TODO: Uncomment once Movie and Vote entities are merged into main
     *
     * @OneToMany(mappedBy = "movieNight", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
     * private List<Vote> votes = new ArrayList<>();
     */
     @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
     @JoinTable(
             name = "movie_night_movies",
             joinColumns = @JoinColumn(name = "movie_night_id"),
             inverseJoinColumns = @JoinColumn(name = "movie_id")
     )
     private List<Movie> movies = new ArrayList<>();

    /**
     * Instantiates a new MovieNight.
     */
    public MovieNight() {
    }

    /**
     * Instantiates a new MovieNight.
     *
     * @param name        the name
     * @param eventDate   the event date
     * @param host        the host
     * @param description the description
     * @param status      the status
     */
    public MovieNight(String name, LocalDate eventDate, String host, String description, String status) {
        this.name = name;
        this.eventDate = eventDate;
        this.host = host;
        this.description = description;
        this.status = status;
    }

    /*
     * TODO: Uncomment helper methods and collection getters/setters once Vote is merged
     *
     * public void addVote(Vote vote) {
     *     votes.add(vote);
     *     vote.setMovieNight(this);
     * }
     *
     * public void removeVote(Vote vote) {
     *     votes.remove(vote);
     *     vote.setMovieNight(null);
     * }
     *
     * public List<Vote> getVotes() {
     *     return votes;
     * }
     *
     * public void setVotes(List<Vote> votes) {
     *     this.votes = votes;
     * }
     */

     public void addMovie(Movie movie) {
         movies.add(movie);
     }

     public void removeMovie(Movie movie) {
         movies.remove(movie);
     }
     public List<Movie> getMovies() {
         return movies;
     }

     public void setMovies(List<Movie> movies) {
         this.movies = movies;
      }


    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets event date.
     *
     * @return the event date
     */
    public LocalDate getEventDate() {
        return eventDate;
    }

    /**
     * Sets event date.
     *
     * @param eventDate the event date
     */
    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(String host) {
        this.host = host;
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

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MovieNight{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", eventDate=" + eventDate +
                ", host='" + host + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}