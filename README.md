# User Management Service

This service manages user profile data and preferences, enhancing scalability and security by decoupling user profile management from the core IAM service.

## Features

-   **Retrieve User Profile:** Fetches the profile of the currently authenticated user based on the JWT `sub` claim.
-   **Save User Profile:** Creates or updates the profile of the currently authenticated user. The IAM user ID is extracted from the JWT `sub` claim and associated with the profile.
-   **Update User Profile:** Allows authenticated users to update their own profile information using their IAM user ID as the path variable.
-   **Database Integration:** Stores user profile data in a PostgreSQL database using Spring Data JPA.
-   **JWT-Based Authentication:** Secures endpoints using JWT Bearer tokens issued by the IAM service (e.g., Keycloak).

## Technologies Used

-   **Java:** The primary programming language.
-   **Spring Boot:** A framework for building standalone, production-grade Spring-based Applications.
-   **Spring Web:** For building RESTful APIs.
-   **Spring Security OAuth2 Resource Server:** For JWT-based authentication and authorization.
-   **Spring Data JPA:** For database interaction with PostgreSQL.
-   **PostgreSQL:** The relational database used for storing user profiles.
-   **Lombok:** For reducing boilerplate code (e.g., getters, setters).
-   **Maven:** For project management and dependency management.
-   **Slf4j & Logback:** For logging.

## Architecture

The User Management Service interacts with:

1.  **IAM Service (e.g., Keycloak):** For authenticating users and issuing JWTs.
2.  **PostgreSQL:** For storing user profile data.
3.  **Clients:** Consume the API endpoints to retrieve and manage user profiles.

## Setup and Installation

1.  **Prerequisites:**
    -   Java Development Kit (JDK) 17 or higher
    -   Maven
    -   Docker (for running PostgreSQL - optional but recommended for local development)

2.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd <your-repository-name>
    ```

3.  **Configure Environment:**
    -   Create a `src/main/resources/application.properties` file (or `application.yml`) and configure the following:
        -   **Server Port:** `server.port`
        -   **PostgreSQL Settings:** `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`
        -   **Spring Security OAuth2 Resource Server Settings:** `spring.security.oauth2.resourceserver.jwt.issuer-uri` (the issuer URI of your IAM provider, e.g., Keycloak)
        -   Other application-specific configurations.

    Example `application.properties`:
    ```properties
    server.port=8081

    spring.datasource.url=jdbc:postgresql://localhost:5432/user_profiles_db
    spring.datasource.username=postgres
    spring.datasource.password=postgres
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true

    spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/contentnexus
    ```

4.  **Set up PostgreSQL:**
    -   Ensure you have a PostgreSQL instance running. You can use Docker:
        ```bash
        docker run -d --name user-profiles-db -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=user_profiles_db postgres:latest
        ```

5.  **Build the application:**
    ```bash
    mvn clean install
    ```

6.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

    The service will be accessible at `http://localhost:8081` (or the port configured in your `application.properties`).

## API Endpoints

-   **`GET /api/users/me`**: Retrieves the profile of the currently authenticated user. Requires a valid JWT in the `Authorization` header.
-   **`POST /api/users/save`**: Saves or updates the profile of the currently authenticated user. Accepts a JSON body with the `UserProfile`. Requires a valid JWT in the `Authorization` header.
-   **`PUT /api/users/{userId}`**: Updates the profile of the user with the specified `userId`. The `userId` in the path must match the `sub` claim in the authenticated JWT. Accepts a JSON body with the `UserProfile`. Requires a valid JWT in the `Authorization` header.

## Database Schema

The `user_profile` table in PostgreSQL stores user profile information:

| Column      | Type        | Description                               |
| ----------- | ----------- | ----------------------------------------- |
| id          | BIGINT      | Primary Key, Auto Increment             |
| user_id     | VARCHAR(255)| ID of the user in the IAM system         |
| email       | VARCHAR(255)| User's email address                    |
| username    | VARCHAR(255)| User's username                         |
| first_name  | VARCHAR(255)| User's first name                       |
| last_name   | VARCHAR(255)| User's last name                        |
| role        | VARCHAR(255)| User's role                             |
| phone       | VARCHAR(255)| User's phone number (optional)          |
| preferences | JSONB       | User's preferences (flexible JSON data) |
| ...         | ...         | Other profile fields as needed          |

## Getting Started

To interact with this service, you will need a valid JWT obtained from your IAM provider. Include this token in the `Authorization` header of your HTTP requests as a Bearer token.

Example using `curl` to get the current user's profile:

```bash
curl -X GET \
  http://localhost:8081/api/users/me \
  -H 'Authorization: Bearer <your_jwt_token>'
