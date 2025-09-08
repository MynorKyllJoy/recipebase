import { useState } from "react";
import API from "../config/API";
import { useNavigate } from "react-router-dom";
import DynamicEditList from "./DynamicEditList";


function Upload() {
    const navigate = useNavigate();
    const [instructions, setInstructions] = useState<string[]>([]);
    const [ingredients, setIngredients] = useState<string[]>([]);
    const [title, setTitle] = useState<string>("");
    const [description, setDescription] = useState<string>("");
    const [ingredientInput, setIngredientInput] = useState("");
    const [instructionInput, setInstructionInput] = useState("");

    const addInstructionHandler = () => {
        if(instructionInput.trim()) {
            setInstructions([...instructions, instructionInput]);
            setInstructionInput("");
        }
    };

    const editInstructionHandler = (editIndex: number, newInstruction: string) => {
        const newInstructions = instructions.map((instruction, index) => {
            if(editIndex === index) {
                return newInstruction;
            }
            return instruction;
        });
        setInstructions(newInstructions);
    };

    const deleteInstructionHandler = (index: number) => {
        const newInstructions = instructions.filter(
            (_, itemIndex) => (itemIndex !== index)
        );
        setInstructions(newInstructions);
    };

    const addIngredientHandler = () => {
        if(ingredientInput.trim()) {
            setIngredients([...ingredients, ingredientInput]);
            setIngredientInput("");
        }
    };

    const editIngredientHandler = (editIndex: number, newInstruction: string) => {
        const newIngredient = ingredients.map((ingredient, index) => {
            if(editIndex === index) {
                return newInstruction;
            }
            return ingredient;
        });
        setIngredients(newIngredient);
    };

    const deleteIngredientHandler = (index: number) => {
        const newIngredient = ingredients.filter(
            (_, itemIndex) => (itemIndex !== index)
        );
        setIngredients(newIngredient);
    };

    const uploadHandler = () => {
        API.post(
            "api/v1/recipes/upload", {
                title,
                description,
                ingredients,
                instructions
            }
        ).then(
            (response) => {
                navigate(`/recipes/${response.data.id}`, {replace: true});
            }
        ).catch(
            // TODO: Error Handling
            (error) => {console.log(error)}
        );
    }

    return (<>
        <div>
            <>
                <input 
                    type="text" 
                    placeholder="Recipe name..."
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    data-testid="title"
                />
                <button data-testid="upload" onClick={uploadHandler}>Upload</button><br/>
                <textarea 
                    placeholder="Description..."
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    data-testid="description"
                />
            </>
            <>
                <p>Ingredient</p>
                <input 
                    type="text"
                    placeholder="200g onions, chopped"
                    value={ingredientInput}
                    onChange={(e) => setIngredientInput(e.target.value)}
                    data-testid="currIngredient"
                />
                <button data-testid="addIngredient-btn" onClick={addIngredientHandler}>Add</button>
            </>
            <>
                <DynamicEditList
                    items={ingredients}
                    onEditItem={editIngredientHandler}
                    onDeleteItem={deleteIngredientHandler}
                    data-testid="ingredientList"
                />
            </>
        </div>
        <div>
            <>
                <p>Instruction Step {instructions.length+1}</p>
                <textarea
                    placeholder="Fry onions until tanslucent."
                    value={instructionInput}
                    onChange={e => setInstructionInput(e.target.value)}
                    data-testid="currInstruction"
                />
                <button data-testid="addInstruction-btn" onClick={addInstructionHandler}>Add</button>
            </>
            <>
                <DynamicEditList
                    items={instructions}
                    onEditItem={editInstructionHandler}
                    onDeleteItem={deleteInstructionHandler}
                    data-testid="instructionList"
                />
            </>
        </div>
    </>);
}

export default Upload;
