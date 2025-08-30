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