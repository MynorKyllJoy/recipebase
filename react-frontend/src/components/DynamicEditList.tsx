import "../styles/DynamicEditList.css"
import { useState } from "react";


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
    // the original value needs to be saved, so it won't be overwritten by edits
    const [originalValue, setOriginalValue] = useState(item);

    const saveEditHandler = () => {
        onEditItem(index, editValue);
        setIsEditing(false);
        setOriginalValue(editValue);
    };

    const cancelEditHandler = () => {
        setIsEditing(false);
        setEditValue(originalValue);
    };

    return (
        <li className="listItemContent">
                <input 
                    type="text" 
                    value={editValue} 
                    onChange={(e) => setEditValue(e.target.value)}
                    disabled={isEditing===false}
                />
            {
                isEditing ? (<>
                    <div className="buttonList">
                        <button className="saveButton" onClick={saveEditHandler}>Save</button>
                        <button className="cancelButton" onClick={cancelEditHandler}>Cancel</button>
                    </div>
                </>) : (<>
                    <div className="buttonList">
                        <button className="editButton" onClick={() => setIsEditing(true)}>Edit</button>
                        <button className="deleteButton" onClick={() => onDeleteItem(index)}>Delete</button>
                    </div>
                </>)
            }
        </li>
    );
}


function DynamicEditList({items, onEditItem, onDeleteItem}: ListProps) {
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

export default DynamicEditList;
