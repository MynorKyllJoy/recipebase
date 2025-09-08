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

    const saveEditHandler = () => {
        onEditItem(index, editValue);
        setIsEditing(false);
    };

    const cancelEditHandler = () => {
        setIsEditing(false);
    };

    return (
        <li>
            {
                isEditing ? (<>
                    <input 
                        type="text" 
                        value={editValue} 
                        onChange={(e) => setEditValue(e.target.value)}
                    />
                    <button className="saveButton" onClick={saveEditHandler}>Save</button>
                    <button className="cancelButton" onClick={cancelEditHandler}>Cancel</button>
                </>) : (<>
                    {item}
                    <button className="editButton" onClick={() => setIsEditing(true)}>Edit</button>
                    <button className="deleteButton" onClick={() => onDeleteItem(index)}>Delete</button>
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
