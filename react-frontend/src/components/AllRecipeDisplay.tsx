import { useEffect, useState } from "react";
import API from "../config/API";
import type { Recipe } from "../types/Recipe";


function RecipeListDisplay() {
    const [recipes, setRecipes] = useState<Recipe[]>([]);

    useEffect(() => {
        API.get(
            "/api/v1/recipes/all",
        ).then(
            (response) => {
                setRecipes(response.data)
            }
        ).catch(
            // TODO: Error Handling
            (error) => console.log(error)
        );
    }, []);

    return (<>
        <ul>{
            recipes.map((recipe, index) => (
                <li key={index}>
                    <a href={"/recipes/" + recipe.id}>{recipe.title}</a>
                    <p>{recipe.source}</p>
                    <p>{recipe.description}</p>
                </li>
            ))
        }</ul>
    </>);
}

export default RecipeListDisplay;
