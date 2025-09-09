import "../styles/DynamicFilterList.css"
import { useState } from "react";


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

    const unitHandler: React.ChangeEventHandler<HTMLSelectElement> = (event) => {
        const newUnit = event.target.value;
        if(newUnit == "Any") {
            setAmount("");
            setIsAny(true);
        } else {
            setIsAny(false);
        }
        setUnit(newUnit);
    };

    return (<>
        <li className="filterItemContent" key={ingredient}>
            <p>{ingredient},</p>
            <div className="filterItemOptions">
                <input
                    className="amountInput"
                    type="text"
                    value={amount}
                    disabled={isAny}
                    onChange={(e) => setAmount(e.target.value)}
                />
                <select value={unit} onChange={unitHandler}>
                    <option value="Any">Any</option>
                    <option value="Cups">Cups</option>
                    <option value="Liters">Liters</option>
                    <option value="Ounces">Ounces</option>
                    <option value="Grams">Grams</option>
                </select>
                <button className="deleteButton" onClick={() => onDelete(index)}>Delete</button>
            </div>
        </li>
    </>);
}

function DynamicFilterList({ingredients, onDelete}: FilterListProps) {
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

export default DynamicFilterList;
