# recipebase
A small spring boot application which saves uploaded or scraped recipe to a Neo4j database.

## Run instructions with docker
- Have Docker installed
- Download latest Neo4j image
- Run "docker build -t recipebase:0.0.1 ." to create an image of the Spring application.
- Run "docker compose up"
- Access website via Localhost:8080

## Website
- /: displays "Hello, world"
- /scrapeRecipe: allows user to enter a recipe site to scrape. Will redirect to /showRecipe or /error depending on 
  whether the scraping was successful.
- /showRecipe?id=x: shows the recipe
- listRecipe: shows a list of all recipes

## Potential Issues
- The scraping has only been tested on a few selected websites
- The scraping only works for english websites

## Future Features
- Searching for recipes by ingredient (with amount, unit and state)
- React Front-end
- Upload own recipes
- Creation of grocery list
- Multiple Users
- Reviews
- Add support for german language