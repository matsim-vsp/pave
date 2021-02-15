#!/usr/bin/env bash
set -euo pipefail

echo --- BUILDING ---
npm run build

echo --- CREATING GIT COMMIT FOR GH-PAGES ---
cd dist
git init .
git add . && git commit -m "gh-pages"
git remote add origin git@github.com:matsim-vsp/pave.git

echo --- PUSHING TO GITHUB ---
git push --force origin master:gh-pages
