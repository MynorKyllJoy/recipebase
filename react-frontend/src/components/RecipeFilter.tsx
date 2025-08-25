import { useEffect, useState } from "react";
import api from "../config/axios_config";
import type { Recipe } from "../types/Recipe";

interface Ingredient {
    name: string
}

interface FilterListItemProps {
    ingredient: string,
    index: number,
    onDelete: (index: number) => void
}

interface FilterListProps {
    ingredients: string[],
    onDelete: (index: number) => void
}

function FilterListItem({ingredient, index, onDelete}: FilterListItemProps) {
    const [amount, setAmount] = useState("");
    const [unit, setUnit] = useState("Any");
    const [isAny, setIsAny] = useState(true);

    const unitHandler = (event: any) => {
        const newUnit = event.target.value;
        if(newUnit == "Any") {
            setAmount("");
            setIsAny(true);
        } else {
            setIsAny(false);
        }
        setUnit(newUnit);
    }
    return (<>
        <li key={ingredient}>
            <input
                type="text"
                value={amount}
                readOnly={isAny}
                onChange={(e) => setAmount(e.target.value)}
            />
            <select value={unit} onChange={unitHandler}>
                <option value="Any">Any</option>
                <option value="Cup">Cup</option>
                <option value="Liter">Liter</option>
                <option value="Ounces">Ounces</option>
                <option value="Grams">Grams</option>
            </select>
            <p>{ingredient}</p>
            <button onClick={() => onDelete(index)}>Delete</button>
        </li>
    </>)
}

function FilterList({ingredients, onDelete}: FilterListProps) {
    return (<ul>
        {
            ingredients.map((ingredient, index) => (
                <FilterListItem 
                    key={ingredient}
                    ingredient={ingredient} 
                    index={index} 
                    onDelete={onDelete}
                />
            ))
        }
    </ul>);
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
        <FilterList 
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