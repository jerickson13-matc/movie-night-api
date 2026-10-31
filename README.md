# Movie Night Voting API

## Project Overview

The Movie Night Voting API is a RESTful web service that allows groups to organize movie nights, nominate movies, vote on what they want to watch, and determine which movie wins.

**Technologies:**
- Java
- Jersey
- Jackson
- Hibernate
- SQL database

---

## Problem Statement

Everyone has experienced the struggle of choosing a movie to watch with friends or family. One person wants horror, another wants comedy, and someone else insists on watching an action movie.

Deciding what to watch can sometimes take longer than actually watching the movie.

The Movie Night Voting API provides a way for groups to organize movie nights, nominate movies, and vote on what they want to watch.

Instead of arguing about which movie to pick, everyone can submit their choice and vote. The API calculates the results and determines which movie wins.

### Example Scenario

Five friends are planning a Halloween movie night.

1. Someone creates a movie night called `Halloween Movie Marathon`.
2. Group members nominate movies.
3. Everyone votes for their preferred movie.
4. The API tallies the votes.
5. The group watches the winning movie.

---

## Project Objectives

The objectives of the Movie Night Voting API are to:

- Store a collection of movies that can be nominated for movie nights.
- Allow users to create and organize movie nights.
- Allow movies to be nominated for a movie night.
- Allow participants to vote for their preferred nominated movie.
- Limit each participant to one vote per movie night.
- Retrieve voting totals.
- Automatically determine which movie received the most votes.
- Provide RESTful operations that other applications can use to interact with the movie night data.

---

## Main Features

### Movie Collection

Store movies with information including:

- Title
- Genre
- Description
- Release year
- Runtime

### Organize Movie Nights

Create movie night events containing:

- Name
- Date
- Description
- Host
- Status
- Nominated movies

### Vote on Movies

Group members can vote for their favorite nominated movie.

Each participant receives one vote per movie night.

### Determine the Winner

The API retrieves vote totals and determines which nominated movie received the most votes.

---

# Proposed Resources

The API will begin with three main resources:

1. `Movie`
2. `MovieNight`
3. `Vote`

---

## Resource 1: Movie

The `Movie` resource stores information about movies that are available for nomination.

| Property | Type | Description |
| --- | --- | --- |
| `id` | Long | Unique movie ID |
| `title` | String | Movie title |
| `genre` | String | Movie genre such as Comedy, Horror, or Action |
| `releaseYear` | Integer | Year the movie was released |
| `runtime` | Integer | Runtime in minutes |
| `description` | String | Movie synopsis |

---

## Resource 2: MovieNight

The `MovieNight` resource represents an organized movie night.

| Property | Type | Description |
| --- | --- | --- |
| `id` | Long | Unique event ID |
| `name` | String | Name of the movie night |
| `eventDate` | LocalDate | Date of the event |
| `host` | String | Person organizing the movie night |
| `description` | String | Event details |
| `status` | String | `OPEN`, `CLOSED`, or `COMPLETED` |

A movie night can have multiple nominated movies, and the same movie can be nominated for multiple movie nights.

This relationship can be represented with a Hibernate `@ManyToMany` relationship and a join table.

---

## Resource 3: Vote

The `Vote` resource represents a participant's movie choice.

| Property | Type | Description |
| --- | --- | --- |
| `id` | Long | Unique vote ID |
| `movieNightId` | Long | Associated movie night |
| `movieId` | Long | Movie receiving the vote |
| `voterName` | String | Person voting |
| `voteDate` | LocalDateTime | Time the vote was submitted |

Each vote belongs to one movie and one movie night.

A validation rule will ensure that a participant cannot vote more than once in the same movie night.

---

# Entity Relationships

The main relationships between the resources are:

### MovieNight and Movie

A movie night can contain multiple nominated movies.

A movie can also be nominated for multiple movie nights.

```text
MovieNight ↔ Movie
Many-to-Many nominations
```

### MovieNight and Vote

A movie night can contain multiple vote records.

```text
MovieNight
    |
    ↓
Vote
```

### Movie and Vote

Each vote identifies the movie that received the vote.

```text
Movie
    |
    ↓
Vote
```

---

# Proposed RESTful Operations

## Movie Operations

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/movies` | Retrieve all movies |
| GET | `/movies/{id}` | Retrieve one movie |
| POST | `/movies` | Add a movie |
| PUT | `/movies/{id}` | Update movie information |
| DELETE | `/movies/{id}` | Delete a movie |

---

## Movie Night Operations

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/movie-nights` | Retrieve all movie nights |
| GET | `/movie-nights/{id}` | Retrieve one movie night |
| POST | `/movie-nights` | Create a movie night |
| PUT | `/movie-nights/{id}` | Update a movie night |
| DELETE | `/movie-nights/{id}` | Delete a movie night |

---

## Nomination Operations

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/movie-nights/{id}/movies` | Nominate a movie |
| GET | `/movie-nights/{id}/movies` | Retrieve nominated movies |

---

## Voting Operations

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/movie-nights/{id}/votes` | View votes for a movie night |
| POST | `/movie-nights/{id}/votes` | Submit a vote |
| DELETE | `/votes/{id}` | Remove a vote |

---

## Results Operations

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/movie-nights/{id}/results` | Calculate and retrieve voting results |

---

## Project Plan

### Week 9 - Planning and Setup

- Create the GitHub repository and add all team members as collaborators.
- Complete the team resume and team charter.
- Finalize the project problem statement.
- Define the `Movie`, `MovieNight`, and `Vote` resources.
- Define the proposed REST API operations.
- Submit the project scope.

### Week 10 - Database and Core Classes

- Create the Java project structure.
- Create the `Movie`, `MovieNight`, and `Vote` entities.
- Create the database tables and relationships.
- Configure Hibernate/JPA.
- Create the DAO functionality needed for the three resources.
- Write tests for the database operations.

### Week 11 - REST API Development

- Create REST endpoints for movies.
- Create REST endpoints for movie nights.
- Add the ability to nominate movies for a movie night.
- Add the ability to submit votes.
- Prevent the same voter from voting more than once in the same movie night.
- Create an endpoint that returns the voting results and winning movie.
- Test the main API operations.

### Week 12 - Final Testing and Documentation

- Test the complete API.
- Fix bugs and make changes based on feedback.
- Complete JavaDoc.
- Create the consumer documentation explaining how another developer can use the API.
- Deploy the API to a publicly accessible location.
- Test the deployed API.
- Prepare the final project presentation.
