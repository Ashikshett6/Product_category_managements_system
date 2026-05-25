# Deployment Guide – Fix GitHub Pages Errors

## Step 1: Enable GitHub Pages (IMPORTANT)

1. Open: https://github.com/Ashikshett6/Product_category_managements_system/settings/pages

2. Under **Build and deployment** → **Source**, select:
   - **Deploy from a branch**

3. Set:
   - **Branch:** `gh-pages`
   - **Folder:** `/ (root)`

4. Click **Save**

5. Wait 2–5 minutes for the site to build.

---

## Step 2: Trigger deployment

Every push to `main` runs the workflow and updates the `gh-pages` branch.

- Check: https://github.com/Ashikshett6/Product_category_managements_system/actions
- Workflow name: **Deploy Frontend to GitHub Pages**
- Status must be **green** (success)

If it failed, click the run → read the error → push again after fixes.

---

## Step 3: Open your live site

**URL:** https://ashikshett6.github.io/Product_category_managements_system/

If you see 404:
- Confirm `gh-pages` branch exists (Code → branch dropdown → `gh-pages`)
- Confirm Pages source is **gh-pages** branch, **/ (root)**
- Wait a few minutes and refresh

---

## Step 4: Run backend (for API data)

GitHub Pages only hosts the UI. Start the API on your PC:

```bash
.\mvnw spring-boot:run
```

MySQL database `pcmsr` must exist.

On the website footer, set **API URL:** `http://localhost:8082`

---

## Common errors and fixes

| Error | Fix |
|-------|-----|
| **404 on GitHub Pages** | Settings → Pages → Branch `gh-pages`, folder `/` |
| **Actions workflow failed** | Open Actions tab → read log → fix and push again |
| **CORS / Failed to fetch** | Start backend; set correct API URL in footer |
| **gh-pages branch missing** | Re-run workflow (Actions → Run workflow) |
| **Site shows old version** | Hard refresh (Ctrl+F5) or wait 5 minutes |

---

## Manual deploy (if Actions still fails)

```bash
git checkout --orphan gh-pages
git rm -rf .
cp -r frontend/* .
git add .
git commit -m "Deploy frontend"
git push origin gh-pages --force
git checkout main
```

Then set Pages source to branch `gh-pages` as in Step 1.
