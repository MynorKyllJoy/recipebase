import { render, screen } from "@testing-library/react";
import DynamicEditList from "../../src/components/DynamicEditList";


describe("DynamicEditList", () => {
    it("should render empty list if list is empty", () => {
        render(<DynamicEditList items={[]} onDeleteItem={() => {}} onEditItem={() => {}}/>)
        const list = screen.getByRole("list")

        expect(list).toBeEmptyDOMElement()
        expect(list).toBeInTheDocument()
    })

    it("should render '1 large egg' and '100g thin cut bacon' as li", () => {
        const items = ["1 large egg", "100g thin cut bacon"];
        render(<DynamicEditList items={items} onDeleteItem={() => {}} onEditItem={() => {}}/>)
        const listItems = screen.getAllByRole("listitem")

        expect(listItems).toHaveLength(2)
        expect(listItems[0]).toBeInTheDocument();
        expect(listItems[0]).toHaveTextContent(/Edit/i);
        expect(listItems[0]).toHaveTextContent(/Delete/i);

        expect(listItems[1]).toBeInTheDocument();
        expect(listItems[1]).toHaveTextContent(/Edit/i);
        expect(listItems[1]).toHaveTextContent(/Delete/i);

        expect(screen.getByText(items[0])).toBeInTheDocument();
        expect(screen.getByText(items[1])).toBeInTheDocument();
    })
})
