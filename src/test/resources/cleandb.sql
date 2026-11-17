-- Drop tables in reverse order of foreign key dependencies
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS movie_night_movies;
DROP TABLE IF EXISTS movie_night;
DROP TABLE IF EXISTS movie;

-- 1. Movie Table (Person 1 - Jack)
CREATE TABLE movie (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    release_year INT NOT NULL,
    runtime INT NOT NULL,
    description TEXT
);

-- 2. MovieNight Table (Person 2 - Dawson)
CREATE TABLE movie_night (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    event_date DATE NOT NULL,
    host VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN'
);

-- 3. Join Table for Nominations (Handled automatically by Hibernate @ManyToMany)
CREATE TABLE movie_night_movies (
    movie_night_id INT NOT NULL,
    movie_id INT NOT NULL,
    PRIMARY KEY (movie_night_id, movie_id),
    CONSTRAINT fk_nomination_movie_night
        FOREIGN KEY (movie_night_id) REFERENCES movie_night(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_nomination_movie
        FOREIGN KEY (movie_id) REFERENCES movie(id)
            ON DELETE CASCADE
);

-- 4. Vote Table (Person 3 - JaQuan)
CREATE TABLE vote (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movie_night_id INT NOT NULL,
    movie_id INT NOT NULL,
    voter_name VARCHAR(100) NOT NULL,
    vote_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_vote_movie_night
        FOREIGN KEY (movie_night_id) REFERENCES movie_night(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_vote_movie
        FOREIGN KEY (movie_id) REFERENCES movie(id)
            ON DELETE CASCADE,
    CONSTRAINT uq_voter_per_movie_night
        UNIQUE (movie_night_id, voter_name)
);