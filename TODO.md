# TODO
A list of features that have been or have yet to be implemented and chores to complete.

## Features:
- [x] scrape the ld+json data from a recipe website URL
- [x] get instructions and ingredient infos from ld+json
- [x] extract amount, unit, state and ingredient name for each listed ingredient info
- [ ] clean more complex ingredient info
  - [ ] "pork belly or shoulder" add pork in front of shoulder
  - [ ] handle something like: "a combination of x and y"
  - [ ] deal with overlap with types, e.g.: sprinkles as ingredient and sprinkles as unit
  - [ ] swap all units to their abbreviations, e.g: ounces -> oz
- [x] save ingredients, recipe and requirements into Neo4j DB
  - [ ] replace the temporary TypeConstants class
  - [ ] deal with states
  - [ ] add converter for gram to ounce, for cups to ml and only save one of these units
  - [ ] improve tokenizer by using a mixture between a state machine and a builder instead of a simple for loop 
       (might remove the need for the IngredientCleaner)
- [x] add users
  - [x] password encryption 
  - [ ] add reviews 
  - [x] upload/save own recipes 
  - [x] fix logout
- [ ] filtered recipe search
  - [x] filter for recipes that use a set of ingredients
  - [ ] extend filtering to use amounts and units
  - [ ] extend filtering to use states
  - [ ] extend filtering to use at least this amount of a certain ingredient
- [ ] move hardcoded userAgent string in RecipeSiteRequestAdapter
- [ ] actual exception handling
- [x] react frontend
- [ ] optimize 
- [ ] create grocery list for set of selected recipes
- [ ] a dish can be an ingredient to (e.g.: bread), link to recipes for said dish (inheritance?)
- [ ] Superclasses? Sugar as super class for white and brown sugar?
## chores:
  - [ ] write documentation
  - [ ] search for "ERROR" and fix edge cases
  - [ ] write tests
  - [ ] improve code readability and cleanliness
  - [ ] maintain ci pipeline
