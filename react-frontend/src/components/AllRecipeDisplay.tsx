import { useEffect, useState } from "react";
import api from "../config/axios_config";


interface Recipe {
    id: string,
    title: string,
    description: string,
    source: string,
    instructions: string[],
    ingredientInfos: string[]
}


function RecipeListDisplay() {
    const [recipes, setRecipes] = useState<Recipe[]>([]);

    useEffect(() => {
        api.get(
            "/api/v1/recipes/all",
        ).then(
            (response) => {
                setRecipes(response.data)
            }
        ).catch(
            // TODO: Error Handling
            (error) => console.log(error)
        );
    }, [])

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
