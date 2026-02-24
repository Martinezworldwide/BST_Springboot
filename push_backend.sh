#!/bin/sh
# Push backend subtree to BST_Springboot_Backend
set -e
cd "$(dirname "$0")"
git add -A
git diff --cached --quiet && echo "Nothing to commit" || git commit -m "Backend: fix POM scope, Dockerfile context, backend .gitignore"
git branch -D backend-branch 2>/dev/null || true
git subtree split --prefix=backend -b backend-branch
git push backend-repo backend-branch:main
echo "Done: backend pushed to https://github.com/Martinezworldwide/BST_Springboot_Backend"
