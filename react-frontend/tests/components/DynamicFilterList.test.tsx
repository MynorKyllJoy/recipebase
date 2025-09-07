import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import DynamicFilterList from "../../src/components/DynamicFilterList";


describe("DynamicFilterList", () => {
    it("should render empty list if list is empty", () => {
        render(<DynamicFilterList ingredients={[]} onDelete={() => {}} />);
        const list = screen.getByRole("list");

        expect(list).toBeEmptyDOMElement();
        expect(list).toBeInTheDocument();
    });


    it("should render 'egg' and 'bacon' as ingredient list and initial states for dropdowns and textfields", () => {
        const items = ["egg", "bacon"];
        const units = [/any/i, /cups/i, /liters/i, /ounces/i, /grams/i];;

        render(<DynamicFilterList ingredients={items} onDelete={() => {}} />);
        const listItems = screen.getAllByRole("listitem");
        const selectItems = screen.getAllByRole("combobox");
        const textFields = screen.getAllByRole("textbox");

        expect(listItems).toHaveLength(2);
        expect(listItems[0]).toBeInTheDocument();
        expect(listItems[0]).toHaveTextContent(/Delete/i);

        expect(listItems[1]).toBeInTheDocument();
        expect(listItems[1]).toHaveTextContent(/Delete/i);

        expect(screen.getByText(items[0])).toBeInTheDocument();
        expect(screen.getByText(items[1])).toBeInTheDocument();

        expect(selectItems).toHaveLength(2);
        selectItems.map(selectItem => {
            units.map(unit => {
                expect(selectItem).toHaveTextContent(unit);
            })
            expect(selectItem).toHaveValue("Any");
        });

        expect(textFields).toHaveLength(2);
        textFields.map(textField => expect(textField).toBeDisabled());
    });


    it("should enable textbox for first ingredient textbox", async () => {
        const items = ["egg", "bacon"];

        render(<DynamicFilterList ingredients={items} onDelete={() => {}} />);
        const selectItems = screen.getAllByRole("combobox");
        const textFields = screen.getAllByRole("textbox");

        await userEvent.selectOptions(selectItems[0], "Liters");

        expect(textFields[0]).not.toBeDisabled();
        expect(textFields[1]).toBeDisabled();
    });


    it("should delete test from textbox after switching from a unit to any", async () => {
        const items = ["egg", "bacon"];

        render(<DynamicFilterList ingredients={items} onDelete={() => {}} />);
        const selectItems = screen.getAllByRole("combobox");
        const textFields = screen.getAllByRole("textbox");

        await userEvent.selectOptions(selectItems[0], "Liters");
        await userEvent.type(textFields[0], "Hello, world!");

        expect(textFields[0]).not.toBeDisabled();
        expect(textFields[0]).toHaveValue("Hello, world!");
        expect(textFields[1]).toBeDisabled();
        expect(textFields[1]).toHaveValue("");

        await userEvent.selectOptions(selectItems[0], "Any");
        expect(textFields[0]).toHaveValue("");
        expect(textFields[1]).toHaveValue("");
    });
});
