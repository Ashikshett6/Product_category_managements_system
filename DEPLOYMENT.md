# Deployment Guide

## 1. Frontend (GitHub Pages) – Automatic

The repo includes a GitHub Actions workflow that deploys the `frontend` folder on every push to `main`.

### One-time setup on GitHub

1. Open: https://github.com/Ashikshett6/Product_category_managements_system/settings/pages
2. Under **Build and deployment** → **Source**, select **GitHub Actions** (not “Deploy from branch”).
3. Save.

### After push to `main`

- Go to **Actions** tab → wait for **Deploy Frontend to GitHub Pages** to finish (green).
- Your site URL:
  **https://ashikshett6.github.io/Product_category_managements_system/**

---

## 2. Backend (Spring Boot API)

GitHub Pages hosts only static files. The API must run on a server.

### Option A – Run on your PC (development)

```bash
# Create MySQL database
CREATE DATABASE pcmsr;

# Start API (port 8082)
.\mvnw spring-boot:run
```

Open frontend and set API URL in footer: `http://localhost:8082`

### Option B – Deploy API to Render (free tier)

1. Sign up at https://render.com
2. **New → Web Service** → connect your GitHub repo
3. Settings:
   - **Root Directory:** leave empty (project root)
   - **Build Command:** `./mvnw clean package -DskipTests`
   - **Start Command:** `java -jar target/ProductCategoryMangementSystem-0.0.1-SNAPSHOT.jar`
4. Add **MySQL** database on Render (or use external MySQL) and set environment variables:

| Variable | Example |
|----------|---------|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://host:3306/pcmsr` |
| `SPRING_DATASOURCE_USERNAME` | your user |
| `SPRING_DATASOURCE_PASSWORD` | your password |
| `SERVER_PORT` | `8082` or Render’s `$PORT` |

5. After deploy, copy your Render URL (e.g. `https://pcms-api.onrender.com`).
6. Open GitHub Pages site → set **API URL** in footer to that URL.

---

## 3. Full stack locally

| Step | Command / URL |
|------|----------------|
| MySQL | Database `pcmsr` created |
| Backend | `.\mvnw spring-boot:run` |
| API | http://localhost:8082/category |
| Frontend | Open `frontend/index.html` or `npx serve frontend` |

---

## 4. Verify deployment

| Check | Expected |
|-------|----------|
| GitHub Pages | Site loads with title “Product Category Management System” |
| API running | `GET http://localhost:8082/category` returns `[]` or JSON list |
| Frontend + API | Add category from UI → success message |

---

## 5. Troubleshooting

| Problem | Fix |
|---------|-----|
| CORS error in browser | Ensure backend is running and `CorsConfig.java` is deployed |
| 404 on GitHub Pages | Enable **GitHub Actions** as Pages source in repo Settings |
| API connection failed | Check API URL in footer matches running backend |
| MySQL connection failed | Check `application.yaml` credentials and database name `pcmsr` |
