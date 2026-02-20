# Product Management API

This is a RESTful API solution designed for managing products and their associated items. It provides full CRUD operations, secure authentication using JWT with refresh token rotation, and follows modern Spring Boot best practices.

## 🚀 Tech Stack

*   **Java 17+**
*   **Spring Boot 3.x**
*   **Spring Data JPA (Hibernate)**
*   **PostgreSQL** (Production Database)
*   **H2 Database** (Test Database)
*   **Spring Security** (JWT & Refresh Token)
*   **JUnit 5 & Mockito** (Testing)
*   **Swagger/OpenAPI** (Documentation)
*   **Docker & Docker Compose** (Containerization)

## 🏗 Architecture

The application follows a standard **Layered Architecture**:

1.  **Controller Layer**: Handles HTTP requests and responses.
2.  **Service Layer**: Contains business logic and transaction management.
3.  **Repository Layer**: Interacts with the database using Spring Data JPA.
4.  **Entity Layer**: Represents database tables.
5.  **DTO Layer**: Data Transfer Objects for request/response payloads.
6.  **Security Layer**: Handles authentication and authorization filters.

## 🛠 Setup Instructions

### Prerequisites

*   Java 17 or higher
*   Maven
*   Docker & Docker Compose (optional, for containerized run)

### Running Locally (with Maven)

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd product-api
    ```

2.  **Configure Database:**
    Ensure you have a PostgreSQL database running locally or update `src/main/resources/application.properties` with your database credentials.
    
    Alternatively, you can use the provided `docker-compose.yml` to spin up a database.

3.  **Build and Run:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

### Running with Docker Compose

1.  **Build and Start Services:**
    ```bash
    docker-compose up --build
    ```
    This will start both the API (on port 8080) and the PostgreSQL database (on port 5432).

## 📚 API Documentation

Once the application is running, you can access the Swagger UI documentation at:

*   **URL**: `http://localhost:8080/swagger-ui/index.html`
*   **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 🧪 Testing

The project includes Unit and Integration tests using JUnit 5 and Mockito, utilizing an in-memory H2 database.

To run the tests:
```bash
mvn test
```

## 🔒 Security

*   **Authentication**: Implemented using JWT (JSON Web Tokens).
*   **Refresh Token**: Supports token rotation for enhanced security.
*   **Authorization**: Role-based access control (RBAC).
    *   `POST /api/v1/auth/signup`: Register a new user.
    *   `POST /api/v1/auth/login`: Login to get Access and Refresh tokens.
    *   `POST /api/v1/auth/refresh-token`: Get a new Access token using a Refresh token.
    *   `GET /api/v1/products`: Accessible by Authenticated Users.
    *   `POST /api/v1/products`: Accessible by `USER` or `ADMIN`.
    *   `PUT /api/v1/products/{id}`: Accessible by `USER` or `ADMIN`.
    *   `DELETE /api/v1/products/{id}`: Accessible by `ADMIN` only.

## 🗄 Database Schema

**Product Table**
*   `id`: Primary Key
*   `product_name`: Indexed for faster search
*   `created_by`
*   `created_on`: Timestamp
*   `modified_by`
*   `modified_on`: Timestamp

**Item Table**
*   `id`: Primary Key
*   `product_id`: Foreign Key
*   `quantity`

## 🐳 Docker Configuration

*   **Dockerfile**: Multi-stage build (or simple JDK image) to package the application.
*   **docker-compose.yml**: Orchestrates the Spring Boot API and PostgreSQL container.
