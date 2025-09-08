import { useParams } from "react-router-dom";
import API from "../config/API";
import { useEffect, useState } from "react";
import type { Recipe } from "../types/Recipe";


function RecipeDisplay() {
    const params = useParams();
    const [recipe, setRecipe] = useState<Recipe>({
        id: "",
        title: "",
        description: "",
        source: "",
        instructions: [],
        ingredientInfos: []
    });

    useEffect(() => {
        API.get(
            "/api/v1/recipes/id/" + params.recipeId,
        ).then(
            (response) => {
                console.log(response.data)
                setRecipe(response.data)
            }
        ).catch(
            // TODO: Error Handling
            (error) => console.log(error)
        );
    }, [params.recipeId]);


    return (<>
        <h1>{recipe.title}</h1>
        <h2>{"By: " + recipe.source}</h2>
        <h3>Description</h3>
        <p>{recipe.description}</p>
        <h3>Ingredients</h3>
        <ul>
            {
                recipe.ingredientInfos.map((ingredient, index) => (
                    <li data-testid="ingredient" key={index}>{ingredient}</li>
                ))
            }
        </ul>
        <h3>Instructions</h3>
        <ul>
            {
                recipe.instructions.map((instruction, index) => (
                    <li data-testid="instruction" key={index}>{instruction}</li>
                ))
            }
        </ul>
    </>);
}

export default RecipeDisplay;
