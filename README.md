# recipebase
A small spring boot application which saves uploaded or scraped recipe to a Neo4j database.

## Run instructions with docker
- Have Docker installed
- Download latest Neo4j image
- Run "docker build -t recipebase-backend:0.0.1 ./spring-backend" to create an image of the Spring backend.
- Run "docker build -t recipebase-frontend:0.0.1 ./react-frontend" to create an image of the React frontend.
- Run "docker compose up"
- Access website via Localhost:3000 in the browser

## Potential Issues
- The scraping has only been tested on a few selected websites
- The application only works for english recipes

## Future Features
- Searching for recipes by ingredient (with amount, unit and state)
- Creation of grocery list
- Reviews
- Add support for german language