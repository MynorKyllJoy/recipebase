import { render, screen } from "@testing-library/react";
import DynamicEditList from "../../src/components/DynamicEditList";
import userEvent from "@testing-library/user-event";


describe("DynamicEditList", () => {
    it("should render empty list if list is empty", () => {
        render(<DynamicEditList items={[]} onDeleteItem={() => {}} onEditItem={() => {}}/>)
        const list = screen.getByRole("list")

        expect(list).toBeEmptyDOMElement()
        expect(list).toBeInTheDocument()
    })

    it("should render '1 large egg' and '100g thin cut bacon' as li and initial states for buttons and textboxes", () => {
        const items = ["1 large egg", "100g thin cut bacon"];
        render(<DynamicEditList items={items} onDeleteItem={() => {}} onEditItem={() => {}}/>)
        const listItems = screen.getAllByRole("listitem")

        expect(listItems).toHaveLength(2)
        expect(listItems[0]).toBeInTheDocument();
        expect(listItems[0]).toHaveTextContent(/edit/i);
        expect(listItems[0]).toHaveTextContent(/delete/i);

        expect(listItems[1]).toBeInTheDocument();
        expect(listItems[1]).toHaveTextContent(/edit/i);
        expect(listItems[1]).toHaveTextContent(/delete/i);

        expect(screen.getByText(items[0])).toBeInTheDocument();
        expect(screen.getByText(items[1])).toBeInTheDocument();
    })

    it("should render 'save' & 'cancel' for list element after edit and return to 'edit' & 'delete' after cancel", async () => {
        const items = ["1 large egg", "100g thin cut bacon"];
        render(<DynamicEditList items={items} onDeleteItem={() => {}} onEditItem={() => {}}/>)
        const listItems = screen.getAllByRole("listitem")
        const editButtons = screen.getAllByText(/edit/i);

        expect(listItems[0]).toHaveTextContent(/edit/i);
        expect(listItems[0]).toHaveTextContent(/delete/i);
        expect(listItems[1]).toHaveTextContent(/edit/i);
        expect(listItems[1]).toHaveTextContent(/delete/i);

        await userEvent.click(editButtons[0]);
        expect(listItems[0]).toHaveTextContent(/save/i);
        expect(listItems[0]).toHaveTextContent(/cancel/i);
        expect(listItems[1]).toHaveTextContent(/edit/i);
        expect(listItems[1]).toHaveTextContent(/delete/i);

        
        await userEvent.click(screen.getByText(/cancel/i));
        expect(listItems[0]).toHaveTextContent(/edit/i);
        expect(listItems[0]).toHaveTextContent(/delete/i);
        expect(listItems[1]).toHaveTextContent(/edit/i);
        expect(listItems[1]).toHaveTextContent(/delete/i);
    });
});
