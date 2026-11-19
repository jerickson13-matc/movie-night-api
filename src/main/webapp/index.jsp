<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Movie Night Voting API</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<header>
    <h1>Movie Night Voting API</h1>
</header>

<nav>
    <a href="#movies">Movies</a>
    <a href="#movie-nights">Movie Nights</a>
    <a href="#votes">Votes</a>
</nav>

<main>

    <h2>Overview</h2>
    <p>The Movie Night Voting API provides endpoints for managing a movie catalog, organizing movie night events, submitting votes for nominated movies, and viewing voting results.</p>

    <h2>Base Path</h2>
    <p><code>/api</code></p>
    <p>All paths are relative to the application's URL.</p>

    <h2>Authentication</h2>
    <p>No authentication or API key is required.</p>

    <h2>Request Format</h2>
    <p>Requests with a JSON body use Content-Type: application/json.</p>

    <section id="movies">

        <h2>Movies</h2>

        <h3>Endpoints</h3>

        <table>
            <thead>
                <tr>
                    <th>Method</th>
                    <th>Endpoint</th>
                    <th>Description</th>
                    <th>Status Codes</th>
                </tr>
            </thead>
            <tbody>
                <tr><td>GET</td><td>/api/movies</td><td>Get all movies</td><td>200</td></tr>
                <tr><td>GET</td><td>/api/movies/{id}</td><td>Get a movie by ID</td><td>200, 404</td></tr>
                <tr><td>POST</td><td>/api/movies</td><td>Add a movie</td><td>201, 400</td></tr>
                <tr><td>PUT</td><td>/api/movies/{id}</td><td>Update a movie</td><td>200, 400, 404</td></tr>
                <tr><td>DELETE</td><td>/api/movies/{id}</td><td>Delete a movie</td><td>204, 404</td></tr>
            </tbody>
        </table>

        <h3>Movie Fields</h3>

        <table>
            <thead>
                <tr>
                    <th>Field</th>
                    <th>Data Type</th>
                    <th>Required</th>
                    <th>Description</th>
                </tr>
            </thead>
            <tbody>
                <tr><td>id</td><td>Long</td><td>No</td><td>Auto-generated ID</td></tr>
                <tr><td>title</td><td>String</td><td>Yes</td><td>Nonblank, max 255 characters</td></tr>
                <tr><td>genre</td><td>String</td><td>Yes</td><td>Nonblank, max 100 characters</td></tr>
                <tr><td>releaseYear</td><td>Integer</td><td>Yes</td><td>Greater than zero</td></tr>
                <tr><td>runtime</td><td>Integer</td><td>Yes</td><td>Runtime in minutes, greater than zero</td></tr>
                <tr><td>description</td><td>String</td><td>No</td><td>Movie description</td></tr>
            </tbody>
        </table>

        <h3>POST and PUT Request Example</h3>

        <pre>POST /api/movies
PUT /api/movies/1</pre>

        <pre class="json"><code>{
    "title": "Halloween",
    "genre": "Horror",
    "releaseYear": 1978,
    "runtime": 91,
    "description": "Michael Myers escapes and returns to Haddonfield."
}</code></pre>

        <p>
            POST generates a new ID. PUT uses the ID in the URL.
            Leaving out the description during an update sets it to null
        </p>

        <h3>Response Example</h3>

        <pre>GET /api/movies/1</pre>

        <pre class="json"><code>{
    "id": 1,
    "title": "Halloween",
    "genre": "Horror",
    "releaseYear": 1978,
    "runtime": 91,
    "description": "Michael Myers escapes and returns to Haddonfield."
}</code></pre>

        <p>
            GET by ID, POST, and PUT return a movie object.
            GET all returns an array of movie objects.
        </p>

        <h3>Error Handling and Behavior</h3>

        <ul>
            <li>400: Invalid or missing required data.</li>
            <li>404: Movie not found.</li>
            <li>400 and 404 responses have no custom response body.</li>
            <li>Deleting a movie also removes its nominations and votes.</li>
        </ul>

    </section>

    <section id="movie-nights">

        <h2>Movie Nights</h2>

        <h3>Endpoints</h3>

        <table>
            <thead>
                <tr>
                    <th>Method</th>
                    <th>Endpoint</th>
                    <th>Description</th>
                    <th>Status Codes</th>
                </tr>
            </thead>
            <tbody>
                <tr><td>GET</td><td>/api/movie-nights</td><td>Get all movie nights</td><td>200</td></tr>
                <tr><td>GET</td><td>/api/movie-nights/{id}</td><td>Get a movie night by ID</td><td>200, 404</td></tr>
                <tr><td>POST</td><td>/api/movie-nights</td><td>Create a movie night</td><td>201, 400</td></tr>
                <tr><td>PUT</td><td>/api/movie-nights/{id}</td><td>Update a movie night</td><td>200, 400, 404</td></tr>
                <tr><td>DELETE</td><td>/api/movie-nights/{id}</td><td>Delete a movie night</td><td>204, 404</td></tr>
                <tr><td>GET</td><td>/api/movie-nights/{id}/movies</td><td>Get nominated movies</td><td>200, 404</td></tr>
                <tr><td>POST</td><td>/api/movie-nights/{id}/movies</td><td>Nominate a movie</td><td>201, 400, 404</td></tr>
            </tbody>
        </table>

        <h3>Movie Night Fields</h3>

        <table>
            <thead>
                <tr>
                    <th>Field</th>
                    <th>Data Type</th>
                    <th>Required</th>
                    <th>Description</th>
                </tr>
            </thead>
            <tbody>
                <tr><td>id</td><td>long</td><td>No</td><td>Auto-generated ID</td></tr>
                <tr><td>name</td><td>String</td><td>Yes</td><td>Nonblank event name, max 255 characters</td></tr>
                <tr><td>eventDate</td><td>LocalDate</td><td>Yes</td><td>YYYY-MM-DD</td></tr>
                <tr><td>host</td><td>String</td><td>Yes</td><td>Nonblank host name, max 100 characters</td></tr>
                <tr><td>description</td><td>String</td><td>No</td><td>Event description, max 1000 characters</td></tr>
                <tr><td>status</td><td>String</td><td>Yes</td><td>OPEN, CLOSED, or COMPLETED (case-sensitive)</td></tr>
                <tr><td>movies</td><td>List&lt;Movie&gt;</td><td>No</td><td>Nominated movies</td></tr>
            </tbody>
        </table>

        <h3>POST and PUT Request Example</h3>

        <pre>POST /api/movie-nights
PUT /api/movie-nights/1</pre>

        <pre class="json"><code>{
    "name": "Halloween Movie Marathon",
    "eventDate": "2026-10-31",
    "host": "Dawson",
    "description": "Annual spooky movie night with friends",
    "status": "OPEN"
}</code></pre>

        <p>
            POST creates a movie night with a generated ID.
            PUT updates the movie night using the ID in the URL.
        </p>

        <h3>Response Example</h3>

        <pre>GET /api/movie-nights/1</pre>

        <pre class="json"><code>{
    "id": 1,
    "name": "Halloween Movie Marathon",
    "eventDate": "2026-10-31",
    "host": "Dawson",
    "description": "Annual spooky movie night with friends",
    "status": "OPEN",
    "movies": [
        {
            "id": 1,
            "title": "Halloween",
            "genre": "Horror",
            "releaseYear": 1978,
            "runtime": 91,
            "description": "Michael Myers escapes and returns to Haddonfield."
        },
        {
            "id": 2,
            "title": "Shaun of the Dead",
            "genre": "Comedy",
            "releaseYear": 2004,
            "runtime": 99,
            "description": "A salesman and his roommate face the zombie apocalypse."
        }
    ]
}</code></pre>

        <p>
            GET by ID, POST, and PUT return a movie night object.
            GET all returns an array of movie night objects.
        </p>

        <h3>Nominating Movies</h3>

        <pre>POST /api/movie-nights/1/movies</pre>

        <p>Nominate an existing movie by providing its ID.</p>

        <pre class="json"><code>{
    "id": 3
}</code></pre>

        <p>
            Returns 201 Created with an array of nominated movies.
            Movies that are already nominated are not added again.
        </p>

        <h3>Retrieving Nominated Movies</h3>

        <pre>GET /api/movie-nights/1/movies</pre>

        <p>
            Returns an array of nominated movies for the specified
            movie night, or an empty array if no movies are nominated.
        </p>

        <h3>Movie Night Statuses</h3>

        <ul>
            <li>OPEN: Voting is allowed.</li>
            <li>CLOSED: Voting is closed.</li>
            <li>COMPLETED: The event is finished.</li>
        </ul>

        <h3>Error Handling and Behavior</h3>

        <ul>
            <li>400: Invalid or missing required data.</li>
            <li>404: Movie night or movie not found.</li>
            <li>400 and 404 responses have no custom response body.</li>
            <li>Deleting a movie night removes its nominations and votes, but not the movies.</li>
        </ul>

    </section>

    <section id="votes">

        <h2>Votes</h2>

        <h3>Endpoints</h3>

        <table>
            <thead>
                <tr>
                    <th>Method</th>
                    <th>Endpoint</th>
                    <th>Description</th>
                    <th>Status Codes</th>
                </tr>
            </thead>
            <tbody>
                <tr><td>GET</td><td>/api/movie-nights/{id}/votes</td><td>Get votes for a movie night</td><td>200, 404</td></tr>
                <tr><td>POST</td><td>/api/movie-nights/{id}/votes</td><td>Submit a vote</td><td>201, 400, 404, 409</td></tr>
                <tr><td>DELETE</td><td>/api/movie-nights/{id}/votes/{voteId}</td><td>Delete a vote</td><td>204, 404</td></tr>
                <tr><td>GET</td><td>/api/movie-nights/{id}/votes/results</td><td>Get voting results</td><td>200, 404</td></tr>
            </tbody>
        </table>

        <h3>Vote Fields</h3>

        <table>
            <thead>
                <tr>
                    <th>Field</th>
                    <th>Data Type</th>
                    <th>Required in POST Body</th>
                    <th>Description</th>
                </tr>
            </thead>
            <tbody>
                <tr><td>id</td><td>Long</td><td>No</td><td>Auto-generated vote ID</td></tr>
                <tr><td>movieNightId</td><td>long</td><td>No</td><td>Movie night ID from URL</td></tr>
                <tr><td>movieId</td><td>Long</td><td>Yes</td><td>Nominated movie ID</td></tr>
                <tr><td>movieTitle</td><td>String</td><td>No</td><td>Title of the selected movie</td></tr>
                <tr><td>voterName</td><td>String</td><td>Yes</td><td>Nonblank, max 100 characters</td></tr>
                <tr><td>voteDate</td><td>String</td><td>No</td><td>Generated date and time</td></tr>
            </tbody>
        </table>

        <h3>Voting Rules</h3>

        <ul>
            <li>The movie night must be OPEN, ignoring capitalization.</li>
            <li>The selected movie must be nominated.</li>
            <li>Each voter name can vote once per movie night.</li>
            <li>Surrounding spaces are removed from submitted voter names.</li>
            <li>Capitalization is ignored when checking for duplicate votes.</li>
            <li>Voter names are not authenticated.</li>
        </ul>

        <h3>POST Request Example</h3>

        <pre>POST /api/movie-nights/1/votes</pre>

        <pre class="json"><code>{
    "movieId": 1,
    "voterName": "Dawson"
}</code></pre>

        <h3>Response Example</h3>

        <p>Example vote response:</p>

        <pre class="json"><code>{
    "id": 1,
    "movieNightId": 1,
    "movieId": 1,
    "movieTitle": "Halloween",
    "voterName": "Dawson",
    "voteDate": "2026-10-30T18:30"
}</code></pre>

        <p>
            POST returns the created vote in this format.
            GET votes returns an array of votes.
            The server generates the vote ID and date.
        </p>

        <h3>Voting Results</h3>

        <pre>GET /api/movie-nights/1/votes/results</pre>

        <p>Example voting results:</p>

        <pre class="json"><code>{
    "movieNightId": 1,
    "movieNightName": "Halloween Movie Marathon",
    "totalVotes": 3,
    "results": [
        {
            "movieId": 1,
            "title": "Halloween",
            "votes": 2
        },
        {
            "movieId": 2,
            "title": "Shaun of the Dead",
            "votes": 1
        }
    ],
    "winner": {
        "movieId": 1,
        "title": "Halloween",
        "votes": 2
    }
}</code></pre>

        <p>
            Results include all nominated movies, even those with zero votes.
            If no votes exist, winner is null.
        </p>

        <p>
            If there is a tie, winner is null, tie is true,
            and tiedMovies contains the titles of the tied movies.
        </p>

        <h3>Error Response Example</h3>

        <pre class="json"><code>{
    "message": "Voter has already voted in this movie night"
}</code></pre>

        <h3>Error Handling and Behavior</h3>

        <ul>
            <li>400: Invalid voting data, voting is closed, or the movie is not nominated.</li>
            <li>404: Movie, movie night, or vote not found.</li>
            <li>409: Voter has already voted in this movie night.</li>
            <li>Voting errors include a message property.</li>
            <li>DELETE requires both the movie night ID and vote ID.</li>
            <li>DELETE returns 404 if the vote does not belong to the specified movie night.</li>
        </ul>

    </section>

</main>

</body>
</html>