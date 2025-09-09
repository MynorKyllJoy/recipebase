import { useEffect, useState } from "react";
import API from "../config/API";
import type { Recipe } from "../types/Recipe";
import DynamicFilterList from "./DynamicFilterList";


interface Ingredient {
    name: string
}


function RecipeFilter() {
    const [ingredientInput, setIngredientInput] = useState("");
    const [allIngredients, setAllIngredients] = useState<Ingredient[]>([]);

    const [filterIngredients, setFilterIngredients] = useState<string[]>([]);
    const [recipes, setRecipes] = useState<Recipe[]>([]);

    useEffect(() => {
        API.get("api/v1/recipes/ingredients")
            .then((response) => {
                setAllIngredients(response.data)
            })
            .catch((error) => console.log(error)) // TODO: Error handling
    }, []);

    const addIngredientHandler = () => {
        const ingredients = allIngredients.map((ingredient) => ingredient.name);
        if(ingredients.includes(ingredientInput)) {
            setFilterIngredients([...filterIngredients, ingredientInput]);
            setIngredientInput("");
        } else {
            // TODO: display error message
            console.log("error");
        }
    }

    const deleteFilterHandler = (index: number) => {
        const newFilterIngredients = filterIngredients.filter(
            (_, filterIndex) => (filterIndex !== index)
        );
        setFilterIngredients(newFilterIngredients);
    }

    const filterRecipesHandler = () => {
        API.post(
            "/api/v1/recipes/filter",
            {
                ingredientNames: filterIngredients
            }
        ).then((response) => {
            setRecipes(response.data);
        }).catch((error) => console.log(error)); // TODO: Error handling
    }

    return (<>
        <input type="text" list="ingredients" value={ingredientInput} onChange={(e) => setIngredientInput(e.target.value)}/>
        <button onClick={addIngredientHandler}>Add</button>
        <button className="submitButton" onClick={filterRecipesHandler}>Filter</button>
        <datalist id="ingredients">
            {
                allIngredients.map((ingredient) => (
                    <option data-testid="ingredient" key={ingredient.name} value={ingredient.name}/>
                ))
            }
        </datalist>
        <DynamicFilterList 
            ingredients={filterIngredients}
            onDelete={deleteFilterHandler}
        />
        <ul>
            {
                recipes.map((recipe) => (
                    <li key={recipe.title}>{recipe.title}</li>
                ))
            }
        </ul>
    </>);
}

export default RecipeFilter;
