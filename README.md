# Product Category Management System

REST API + HTML/CSS/JavaScript frontend for managing product categories and products.

## Tech Stack

- **Backend:** Java 21, Spring Boot 3.5.9, Spring Data JPA, MySQL, ModelMapper, Lombok
- **Frontend:** HTML, CSS, JavaScript (vanilla)

## Features

- Category CRUD (Create, Read, Update, Delete)
- Product Create, Update, Delete (under a category)
- One-to-many relationship: Category → Products
- Global exception handling (404 for not found)
- CORS enabled for frontend

## Prerequisites

- Java 21
- MySQL (database: `pcmsr`)
- Maven (or use `mvnw` in project root)

## Database Setup

```sql
CREATE DATABASE pcmsr;
```

Update `src/main/resources/application.yaml` if your MySQL username/password differ.

## Run Backend

```bash
.\mvnw spring-boot:run
```

API runs at: **http://localhost:8082**

## Run Frontend

1. Start the backend first.
2. Open `frontend/index.html` in a browser, or serve the folder:

```bash
cd frontend
npx serve .
```

3. Default API URL in the page footer: `http://localhost:8082` (change if needed).

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/category` | Create category |
| GET | `/category` | Get all categories |
| GET | `/category/{id}` | Get category by ID |
| PUT | `/category/{id}` | Update category |
| DELETE | `/category/{id}` | Delete category |
| POST | `/product/{categoryId}` | Create product |
| PUT | `/product/{productId}` | Update product |
| DELETE | `/category/{categoryId}/product/{productId}` | Delete product |

## GitHub Pages (Frontend)

1. Push this repo to GitHub.
2. Go to **Settings → Pages**.
3. Source: **Deploy from branch** → `main` → folder **`/frontend`**.
4. Save. Your UI will be at: `https://<username>.github.io/Product_category_managements_system/`

> **Note:** GitHub Pages serves only the static frontend. The Spring Boot API must run elsewhere (local machine, cloud, etc.) and you must set the API URL in the frontend footer.

## Project Structure

```
├── frontend/          # HTML, CSS, JS UI
├── src/main/java/     # Spring Boot application
├── src/main/resources/application.yaml
├── pom.xml
└── README.md
```

## Author

Ashik Shett – [GitHub](https://github.com/Ashikshett6/Product_category_managements_system)
