import { useParams } from "react-router-dom";
import api from "../config/axios_config";
import { useEffect, useState } from "react";

interface Recipe {
    title: string,
    description: string,
    source: string,
    instructions: string[],
    ingredientInfos: string[]
}

function RecipeDisplay() {
    let params = useParams();
    const [recipe, setRecipe] = useState<Recipe>({
        title: "",
        description: "",
        source: "",
        instructions: [],
        ingredientInfos: []
    });

    useEffect(() => {
        api.get(
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
    }, [])


    return (<>
        <h1>{recipe.title}</h1>
        <h2>{"By: " + recipe.source}</h2>
        <h3>Description</h3>
        <p>{recipe.description}</p>
        <h3>Ingredients</h3>
        <ul>{
            recipe.ingredientInfos.map((ingredient, index) => (
                <li key={index}>{ingredient}</li>
            ))
        }</ul>
        <h3>Instructions</h3>
        <ul>{
            recipe.instructions.map((instruction, index) => (
                <li key={index}>{instruction}</li>
            ))
        }</ul>
    </>);
}

export default RecipeDisplay;