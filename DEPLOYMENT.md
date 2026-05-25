# Fix "Page Not Found" on GitHub Pages

## Your live URL (copy exactly)

```
https://ashikshett6.github.io/Product_category_managements_system/
```

Do **NOT** use `github.io` without the repo name at the end.

---

## Fix in 3 steps (do on GitHub website)

### Step 1 – Open Pages settings

https://github.com/Ashikshett6/Product_category_managements_system/settings/pages

### Step 2 – Set source

| Setting | Value |
|---------|--------|
| **Source** | Deploy from a branch |
| **Branch** | `main` |
| **Folder** | `/docs` |

Click **Save**.

### Step 3 – Wait

Wait **3–10 minutes**, then open:

https://ashikshett6.github.io/Product_category_managements_system/

Press **Ctrl + F5** to hard refresh.

---

## Check that `docs` folder exists

1. Open: https://github.com/Ashikshett6/Product_category_managements_system
2. Open folder **`docs`**
3. You must see: `index.html`, `styles.css`, `app.js`, `.nojekyll`

If `docs` is missing, pull latest code from `main` branch.

---

## Run backend (for Add/Edit/Delete to work)

GitHub Pages shows only the UI. Start API on your PC:

```bash
.\mvnw spring-boot:run
```

On the website footer set: **API URL** = `http://localhost:8082`

---

## Still 404?

| Check | Action |
|-------|--------|
| Wrong URL | Use full URL with `/Product_category_managements_system/` |
| Wrong folder | Pages must use **`/docs`** not `/ (root)` on `gh-pages` |
| Pages not enabled | Settings → Pages → must show green "Your site is live" |
| Private repo | GitHub Pages free only works on **public** repos |

---

## Test locally (no GitHub)

```bash
cd docs
npx serve .
```

Open the URL shown in terminal.
