# SecureBank BST Fraud Detection

Spring Boot backend (Java 17) and static frontend for managing flagged transactions with a **Binary Search Tree**: insert, search (with BST vs unordered list comparison), and delete (with leaf / one-child / two-children explanation).

- **Backend**: Java 17, Spring Boot 3.2, deployable on **Render** (Docker).
- **Frontend**: Static HTML/CSS/JS for **GitHub Pages** (in `docs/` and `frontend/`).

## Features

1. **BST insertion** – Add flagged transactions; tree ordered by transaction ID (left &lt; node &lt; right).
2. **Search by transaction ID** – Returns the transaction and **BST vs unordered list** comparison (comparison counts and efficiency note).
3. **Deletion** – Remove reviewed transactions; API returns **node type** (LEAF, ONE_CHILD, TWO_CHILDREN) and **impact explanation** on BST structure and fraud detection.

## Run locally

- **Backend**: From repo root, run `start-backend.bat` (Windows) or `./start-backend.sh` (Mac/Linux), or:
  - `cd backend && mvn spring-boot:run`
- **Frontend**: Open `frontend/index.html` in a browser, or serve `docs/` (e.g. `npx serve docs`). Set API to `http://localhost:8080` (default in `frontend/config.js`).

## Deploy

### Render (backend)

1. Push this repo to GitHub.
2. In Render: New → Web Service → Connect repo → use repo root.
3. Set **Root Directory** to `backend` if you deploy with Docker; or use the repo root and set **Dockerfile Path** to `backend/Dockerfile`.
4. Add env var **CORS_ORIGINS**: your GitHub Pages origin, e.g. `https://YOUR_USERNAME.github.io` or `https://*.github.io`.
5. Deploy. Note the service URL (e.g. `https://securebank-bst-api.onrender.com`).

### GitHub Pages (frontend)

1. In the GitHub repo: **Settings → Pages**.
2. Source: **Deploy from a branch**.
3. Branch: **main**, folder: **/docs**.
4. Save. The site will be at `https://YOUR_USERNAME.github.io/YOUR_REPO/`.
5. To use the deployed API from the frontend, open the site with the API URL:  
   `https://YOUR_USERNAME.github.io/YOUR_REPO/?api=https://YOUR_RENDER_SERVICE.onrender.com`

## API

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/flagged-transactions` | Insert (body: `transactionId`, `amount`, `reason`) |
| GET | `/api/flagged-transactions/search?transactionId=ID` | Search; returns BST vs list comparison |
| DELETE | `/api/flagged-transactions/{transactionId}` | Delete; returns node type and impact explanation |
| GET | `/api/flagged-transactions` | List all (in-order) |
| GET | `/api/docs/bst-explanation` | BST implementation notes (insert, search, deletion) |

## BST implementation notes (summary)

- **Insert**: Maintains hierarchy by transaction ID; duplicate ID updates the node. O(log n) average.
- **Search**: BST O(log n) average vs unordered list O(n); search response includes comparison counts.
- **Delete**:
  - **Leaf**: Parent pointer set to null; no other structure change.
  - **One child**: Parent points to the child; subtree preserved.
  - **Two children**: Node replaced by inorder successor; ordering and fraud set remain consistent.

All data is from the configured store (in-memory BST + list); no mock or placeholder data.
