# How to Run the Favorite Movies Project

This project consists of a Java Spring Boot backend and a React frontend.

## Prerequisites
- Java 24
- Node.js (v22 or later) and npm
- MySQL

## Backend Setup
1. **Configure the Database:**
   - Create a new database in MySQL (e.g., `favorite_movies`).
   - Update `backend/src/main/resources/application.properties` with your database connection details and ensure the OMDB API key (can get from here https://www.omdbapi.com/) is set:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/favorite_movies
     spring.datasource.username=YOUR_DB_USERNAME
     spring.datasource.password=YOUR_DB_PASSWORD
     omdb.api.key=YOUR_OMDB_API_KEY
     ```

2. **Build and Run the Backend:**
   - Open a terminal in the `backend` directory.
   - Run:
     ```sh
     ./mvnw clean install
     ./mvnw spring-boot:run
     ```
   - The backend will start on [http://localhost:8080](http://localhost:8080).

## Frontend Setup
1. **Install Dependencies:**
   - Open a terminal in the `frontend` directory.
   - Run:
     ```sh
     npm install
     ```

2. **Start the Frontend:**
   - In the same terminal, run:
     ```sh
     npm start
     ```
   - The frontend will start on [http://localhost:3000](http://localhost:3000).

## Usage
- Register a new user and log in
- Search for movies and add them to your favorites
- Clicking a grey heart adds the movie to your favorites
- Clicking a red heart removes the movie from your favorites
- Clicking the IMDb ID redirects you to the movie's page
- Clicking on a poster enlarges it


