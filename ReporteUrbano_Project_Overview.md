# ReporteUrbano: Project Overview

## 1. High-Level Summary

ReporteUrbano is a Spring Boot application designed for citizens to report urban incidents or problems. The system utilizes Google Cloud's Gemini AI to process, categorize, or analyze these reports. All incident and user data is stored and managed in a PostgreSQL database using Spring Data JPA.

## 2. Key Features

The ReporteUrbano application offers the following key features:

*   **User Authentication and Management:**
    *   Secure user registration and login functionality implemented using Spring Security.
    *   JSON Web Tokens (JWT) are employed to secure API endpoints, ensuring that only authenticated users can report incidents and access sensitive data.

*   **Incident Reporting Mechanism:**
    *   Users can submit detailed reports of urban incidents.
    *   Each incident report captures:
        *   `title`: A concise title for the incident.
        *   `description`: A comprehensive description of the issue.
        *   `location`: Geographical coordinates (latitude and longitude) of the incident.
        *   `photo`: An image uploaded by the user to visually document the incident.
        *   `category`: Classification of the incident type (e.g., pothole, damaged public lighting, waste accumulation).
        *   `userId`: An identifier linking the report to the registered user.

*   **AI-Powered Guidance Generation:**
    *   The system integrates with Google's Gemini AI model (via Google Cloud AI Platform) to provide users with relevant guidance.
    *   Data sent to the AI for generating this guidance includes the incident's `description`, `photo` (or its analysis), `category`, and a human-readable `address` (obtained by reverse geocoding the location coordinates using OpenStreetMap).
    *   The AI-generated guidance may include:
        *   Step-by-step instructions on how to formally report the issue to the appropriate municipal authorities.
        *   Contact information for specific city departments or emergency services.
        *   Practical tips or advice for the citizen concerning the reported problem.

*   **Data Persistence:**
    *   All application data, including user details and incident reports, is stored in a PostgreSQL database.
    *   Spring Data JPA is used for Object-Relational Mapping (ORM), simplifying database interactions and management.

*   **RESTful API for Incident Management:**
    *   The application exposes a set of RESTful API endpoints for managing incidents, likely including:
        *   `POST /ocorrencias`: To create a new incident report.
        *   `GET /ocorrencias`: To retrieve a list of all reports.
        *   `GET /ocorrencias/{id}`: To fetch a specific report by its unique ID.
        *   `GET /ocorrencias/user/{userId}`: To view all incidents reported by a particular user.
        *   `DELETE /ocorrencias/{id}`: To remove an incident report.

## 3. Technology Stack

ReporteUrbano is built using the following major technologies and frameworks:

*   **Core Framework & Language:**
    *   **Java 17:** The primary programming language.
    *   **Spring Boot:** The core application framework providing robust features for web application development.

*   **Database:**
    *   **PostgreSQL:** The chosen relational database system for data storage.
    *   **Spring Data JPA:** For ORM, facilitating interaction with the PostgreSQL database.

*   **AI & Geocoding Services:**
    *   **Google Cloud AI Platform (Vertex AI) / Gemini:** Powers the AI-driven analysis and guidance generation.
    *   **OpenStreetMap (Nominatim API):** Used for reverse geocoding, converting latitude/longitude coordinates into street addresses for use by the AI and for display.

*   **Security:**
    *   **Spring Security:** Manages authentication and authorization.
    *   **JSON Web Tokens (JWT):** Used for securing API communications.

*   **Build Tool:**
    *   **Gradle:** The project's build automation tool.

## 4. System Workflow: Incident Reporting and AI Guidance

The process from a user reporting an incident to receiving AI-generated guidance is as follows:

1.  **User Submits Report:**
    *   The user fills out an incident form in the client application (web/mobile) with details like title, description, category, location (latitude/longitude), and uploads a photo.
    *   This action triggers a `POST` request to the `/ocorrencias` API endpoint.

2.  **Backend Data Reception:**
    *   The `OcorrenciaController` in the Spring Boot backend receives the submitted data, including the user's ID (typically extracted from their JWT token).

3.  **Backend Processing Steps:**
    *   **a. Geocoding:** The `GeminiService` (or a dedicated geocoding component it uses) takes the `latitude` and `longitude` and queries the OpenStreetMap (Nominatim) API to convert these coordinates into a human-readable street address.
    *   **b. AI Interaction:** The `GeminiService` compiles a data packet for the Google Gemini AI. This includes the incident's `description`, the `address` from OpenStreetMap, the `photo` (or its representation), and the `category`.
    *   **c. Guidance Generation:** Gemini AI processes this information and generates guidance, which may include reporting instructions, authority contacts, and safety tips. This guidance is returned to the `GeminiService`.
    *   **d. Data Persistence:** The original incident data, along with the geocoded `address` and the `aiGuidance` from Gemini, is assembled into an `Ocorrencia` entity. This entity is then saved to the PostgreSQL database via the `OcorrenciaRepository`.

4.  **Response to User:**
    *   The backend sends an HTTP response (e.g., `201 Created`) back to the client application.
    *   This response includes the details of the newly created incident report (now with a database ID), the geocoded address, and the AI-generated guidance.
    *   The client application displays this confirmation and guidance to the user.

## 5. Key Code Components and Their Roles

The ReporteUrbano application is structured into several key Java packages and classes:

*   **Controllers (`com.reporteurbano.reporte_urbano.controller`):**
    *   `OcorrenciaController.java`: Handles HTTP requests for incident management (create, read, delete).
    *   `AuthController.java`: Manages user authentication (login, registration) and token generation.

*   **Services (`com.reporteurbano.reporte_urbano.service`):**
    *   `GeminiService.java`: Orchestrates interaction with Google Gemini AI, including data preparation and communication with OpenStreetMap for geocoding.
    *   `TokenService.java`: Responsible for JWT creation and validation.
    *   *(Implicitly)* `OcorrenciaService.java`, `UsuarioService.java`: Would typically encapsulate business logic for incidents and users respectively, working closely with repositories.

*   **Models/Entities (`com.reporteurbano.reporte_urbano.models`):**
    *   `Ocorrencia.java`: JPA entity representing an incident, defining its attributes and relationships.
    *   `Usuario.java`: JPA entity representing a user, defining user attributes and roles.

*   **Repositories (`com.reporteurbano.reporte_urbano.repository`):**
    *   `OcorrenciaRepository.java`: Spring Data JPA interface for CRUD operations on `Ocorrencia` entities.
    *   `UsuarioRepository.java`: Spring Data JPA interface for CRUD operations on `Usuario` entities.

*   **Security (`com.reporteurbano.reporte_urbano.security` or config packages):**
    *   Components like `SecurityConfig` (for configuring Spring Security rules) and `UserDetailsServiceImpl` (for loading user data during authentication) are essential, working alongside `TokenService`.

This structure promotes a modular and maintainable codebase, separating concerns according to typical Spring Boot application design patterns.
