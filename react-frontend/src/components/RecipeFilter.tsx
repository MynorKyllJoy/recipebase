import { useEffect, useState } from "react";
import api from "../config/axios_config";
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
        api.get("api/v1/recipes/ingredients")
            .then((response) => {
                setAllIngredients(response.data)
            })
            .catch((error) => console.log(error)) // TODO: Error handling
    }, [])

    const addIngredientHandler = () => {
        const ingredients = allIngredients.map((ingredient) => ingredient.name);
        if(ingredients.includes(ingredientInput)) {
            setFilterIngredients([...filterIngredients, ingredientInput]);
            setIngredientInput("");
        } else {
            // display error message
        }
    }

    const deleteFilterHandler = (index: number) => {
        const newFilterIngredients = filterIngredients.filter(
            (_, filterIndex) => (filterIndex !== index)
        );
        setFilterIngredients(newFilterIngredients);
    }

    const filterRecipesHandler = () => {
        api.post(
            "/api/v1/recipes/filter",
            {
                ingredientNames: filterIngredients
            }
        ).then((response) => {
            console.log(response.data)
            setRecipes(response.data)
        }).catch((error) => console.log(error)); // TODO: Error handling
    }

    return (<>
        <input type="input" list="ingredients" value={ingredientInput} onChange={(e) => setIngredientInput(e.target.value)}/>
        <button onClick={addIngredientHandler}>Add</button>
        <button onClick={filterRecipesHandler}>Filter</button>
        <datalist id="ingredients">
            {
                allIngredients.map((ingredient) => (
                    <option key={ingredient.name} value={ingredient.name}/>
                ))
            }
        </datalist>
        <DynamicFilterList 
            ingredients={filterIngredients}
            onDelete={deleteFilterHandler}
        />
        <ul>{
            recipes.map((recipe) => (
                <li key={recipe.title}>{recipe.title}</li>
            ))
        }</ul>
    </>)
}

export default RecipeFilter;