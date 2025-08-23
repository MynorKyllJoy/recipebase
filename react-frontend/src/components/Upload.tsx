import { useState } from "react";
import api from "../config/axios_config";
import { useNavigate } from "react-router-dom";

interface ListItemProps {
    item: string,
    index: number,
    onDeleteItem: (index: number) => void,
    onEditItem: (index: number, editValue: string) => void
}

interface ListProps {
    items: string[],
    onDeleteItem: (index: number) => void,
    onEditItem: (index: number, editValue: string) => void
}

function ListItem({item, index, onDeleteItem, onEditItem}: ListItemProps) {
    const [isEditing, setIsEditing] = useState(false);
    const [editValue, setEditValue] = useState(item);

    const saveEditHandler = () => {
        onEditItem(index, editValue);
        setIsEditing(false);
    };

    const cancelEditHandler = () => {
        setIsEditing(false);
    }

    return (
        <li>
            {
                isEditing ? (<>
                    <input 
                        type="text" 
                        value={editValue} 
                        onChange={(e) => setEditValue(e.target.value)}
                    />
                    <button onClick={saveEditHandler}>Save</button>
                    <button onClick={cancelEditHandler}>Cancel</button>
                </>) : (<>
                    {item}
                    <button onClick={() => setIsEditing(true)}>Edit</button>
                    <button onClick={() => onDeleteItem(index)}>Delete</button>
                </>)
            }
        </li>
    );
}

function List({items, onEditItem, onDeleteItem}: ListProps) {
    return (
        <ul>
            {
                items.map((item, index) => (
                    <ListItem 
                        key={index}
                        item={item} index={index} 
                        onEditItem={onEditItem} 
                        onDeleteItem={onDeleteItem}
                    />
                ))
            }
        </ul>
    );
}

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
        api.post(
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
                />
                <button onClick={uploadHandler}>Upload</button><br/>
                <textarea 
                    placeholder="Description..."
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />
            </>
            <>
                <p>Ingredient</p>
                <input 
                    type="text"
                    placeholder="200g onions, chopped"
                    value={ingredientInput}
                    onChange={(e) => setIngredientInput(e.target.value)}
                />
                <button onClick={addIngredientHandler}>Add</button>
            </>
            <>
                <List
                    items={ingredients}
                    onEditItem={editIngredientHandler}
                    onDeleteItem={deleteIngredientHandler}
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
                />
                <button onClick={addInstructionHandler}>Add</button>
            </>
            <>
                <List
                    items={instructions}
                    onEditItem={editInstructionHandler}
                    onDeleteItem={deleteInstructionHandler}
                />
            </>
        </div>
    </>);
}

export default Upload;
