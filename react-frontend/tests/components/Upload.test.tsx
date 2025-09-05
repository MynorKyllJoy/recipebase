import { render, screen } from "@testing-library/react"
import Upload from "../../src/components/Upload"
import { useNavigate } from "react-router-dom";
import API from "../../src/config/API";
import userEvent from "@testing-library/user-event";


describe("Upload", () => {
    vi.mock("./src/config/API", async () => ({
        API: {
            ...await vi.importActual("./src/config/API"),
            post: vi.fn()
        }
    }));
    vi.mock("react-router-dom", async () => ({
        useNavigate: vi.fn()
    }));


    // tests: display typed input correctly
    it("should display the title the user types in", async () => {
        render(<Upload/>);
        const titleInput = screen.getByTestId("title");

        expect(titleInput).toHaveValue("");
        await userEvent.type(titleInput, "title");
        expect(titleInput).toHaveValue("title");
    });


    it("should display the description the user types in", async () => {
        render(<Upload/>);
        const descriptionInput = screen.getByTestId("description");

        expect(descriptionInput).toHaveValue("");
        await userEvent.type(descriptionInput, "description");
        expect(descriptionInput).toHaveValue("description");
    });


    it("should display the ingredient the user types in", async () => {
        render(<Upload/>);
        const ingredientInput = screen.getByTestId("currIngredient");

        expect(ingredientInput).toHaveValue("");
        await userEvent.type(ingredientInput, "1 large egg");
        expect(ingredientInput).toHaveValue("1 large egg");
    });


    it("should display the instruction the user types in", async () => {
        render(<Upload/>);
        const instructionInput = screen.getByTestId("currInstruction");

        expect(instructionInput).toHaveValue("");
        await userEvent.type(instructionInput, "Fry the eggs.");
        expect(instructionInput).toHaveValue("Fry the eggs.");
    });


    // test: add-, edit-, and deleteInstructionHandler with DynamicEditList
    // tests for ingredient list
    it("should add typed ingredient to the DynamicEditList", async () => {
        render(<Upload/>)
        const ingredientInput = screen.getByTestId("currIngredient");

        expect(screen.queryByText(/edit/i)).toBeNull();
        expect(screen.queryByText(/delete/i)).toBeNull();

        await userEvent.type(ingredientInput, "1 large egg");
        await userEvent.click(screen.getByTestId("addIngredient-btn"));

        expect(ingredientInput).toHaveValue("");
        expect(screen.getByText("1 large egg")).toBeInTheDocument();
        expect(screen.getByText(/edit/i)).toBeInTheDocument();
        expect(screen.getByText(/delete/i)).toBeInTheDocument();
    });


    it("should add of typed ingredient to the DynamicEditList and activate edit mode", async () => {
        render(<Upload/>)
        const ingredientInput = screen.getByTestId("currIngredient");

        await userEvent.type(ingredientInput, "1 large egg");
        await userEvent.click(screen.getByTestId("addIngredient-btn"));
        await userEvent.click(screen.getByText(/edit/i));

        expect(ingredientInput).toHaveValue("");
        expect(screen.getByDisplayValue("1 large egg")).toBeInTheDocument();
        expect(screen.queryByText(/edit/i)).toBeNull();
        expect(screen.queryByText(/delete/i)).toBeNull();
    });


    it("should add of typed ingredient to the DynamicEditList and activate edit mode and cancel", async () => {
        render(<Upload/>)
        const ingredientInput = screen.getByTestId("currIngredient");

        await userEvent.type(ingredientInput, "1 large egg");
        await userEvent.click(screen.getByTestId("addIngredient-btn"));
        await userEvent.click(screen.getByText(/edit/i));

        await userEvent.type(screen.getByDisplayValue("1 large egg"), ", cooked");
        await userEvent.click(screen.getByText(/cancel/i));

        expect(screen.getByText("1 large egg")).toBeInTheDocument();
        expect(screen.queryByText("1 large egg, cooked")).toBeNull();
    });


    it("should add of typed ingredient to the DynamicEditList and activate edit mode and save edit", async () => {
        render(<Upload/>)
        const ingredientInput = screen.getByTestId("currIngredient");

        await userEvent.type(ingredientInput, "1 large egg");
        await userEvent.click(screen.getByTestId("addIngredient-btn"));
        await userEvent.click(screen.getByText(/edit/i));

        await userEvent.type(screen.getByDisplayValue("1 large egg"), ", cooked");
        await userEvent.click(screen.getByText(/save/i));

        expect(screen.queryByText("1 large egg")).toBeNull();
        expect(screen.getByText("1 large egg, cooked")).toBeInTheDocument();
    });


    it("should add of typed ingredient to the DynamicEditList and delete it", async () => {
        render(<Upload/>)
        const ingredientInput = screen.getByTestId("currIngredient");

        await userEvent.type(ingredientInput, "1 large egg");
        await userEvent.click(screen.getByTestId("addIngredient-btn"));
        await userEvent.click(screen.getByText(/delete/i));

        expect(ingredientInput).toHaveValue("");
        expect(screen.queryByText("1 large egg")).toBeNull();
        expect(screen.queryByText(/edit/i)).toBeNull();
        expect(screen.queryByText(/delete/i)).toBeNull();
    });


    // tests for intruction list
    it("should add typed instruction to the DynamicEditList", async () => {
        render(<Upload/>)
        const instructionInput = screen.getByTestId("currInstruction");

        expect(screen.queryByText(/edit/i)).toBeNull();
        expect(screen.queryByText(/delete/i)).toBeNull();

        await userEvent.type(instructionInput, "Fry the egg");
        await userEvent.click(screen.getByTestId("addInstruction-btn"));

        expect(instructionInput).toHaveValue("");
        expect(screen.getByText("Fry the egg")).toBeInTheDocument();
        expect(screen.getByText(/edit/i)).toBeInTheDocument();
        expect(screen.getByText(/delete/i)).toBeInTheDocument();
    });


    it("should add of typed instruction to the DynamicEditList and activate edit mode", async () => {
        render(<Upload/>)
        const instructionInput = screen.getByTestId("currInstruction");

        await userEvent.type(instructionInput, "Fry the egg");
        await userEvent.click(screen.getByTestId("addInstruction-btn"));
        await userEvent.click(screen.getByText(/edit/i));

        expect(instructionInput).toHaveValue("");
        expect(screen.getByDisplayValue("Fry the egg")).toBeInTheDocument();
        expect(screen.queryByText(/edit/i)).toBeNull();
        expect(screen.queryByText(/delete/i)).toBeNull();
    });


    it("should add of typed instruction to the DynamicEditList and activate edit mode and cancel", async () => {
        render(<Upload/>)
        const instructionInput = screen.getByTestId("currInstruction");

        await userEvent.type(instructionInput, "Fry the egg");
        await userEvent.click(screen.getByTestId("addInstruction-btn"));
        await userEvent.click(screen.getByText(/edit/i));

        await userEvent.type(screen.getByDisplayValue("Fry the egg"), " over medium heat");
        await userEvent.click(screen.getByText(/cancel/i));

        expect(screen.getByText("Fry the egg")).toBeInTheDocument();
        expect(screen.queryByText("Fry the egg over medium heat")).toBeNull();
    });


    it("should add of typed instruction to the DynamicEditList and activate edit mode and save edit", async () => {
        render(<Upload/>)
        const instructionInput = screen.getByTestId("currInstruction");

        await userEvent.type(instructionInput, "Fry the egg");
        await userEvent.click(screen.getByTestId("addInstruction-btn"));
        await userEvent.click(screen.getByText(/edit/i));

        await userEvent.type(screen.getByDisplayValue("Fry the egg"), " over medium heat");
        await userEvent.click(screen.getByText(/save/i));

        expect(screen.queryByText("Fry the egg")).toBeNull();
        expect(screen.getByText("Fry the egg over medium heat")).toBeInTheDocument();
    });


    it("should add of typed instruction to the DynamicEditList and delete it", async () => {
        render(<Upload/>)
        const instructionInput = screen.getByTestId("currInstruction");

        await userEvent.type(instructionInput, "Fry the egg");
        await userEvent.click(screen.getByTestId("addInstruction-btn"));
        await userEvent.click(screen.getByText(/delete/i));

        expect(instructionInput).toHaveValue("");
        expect(screen.queryByText("Fry the egg.")).toBeNull();
        expect(screen.queryByText(/edit/i)).toBeNull();
        expect(screen.queryByText(/delete/i)).toBeNull();
    });


    // tests: uploadHandler testing
    it("should redirect after clicking upload button", async () => {
        const mockNavigate = vi.fn();
        (useNavigate as ReturnType<typeof vi.fn>).mockReturnValue(mockNavigate);
        const mockAPI = vi.spyOn(API, "post").mockImplementation(() => {
            return Promise.resolve({data: {id: "1234"}});
        });

        render(<Upload/>);
        await userEvent.click(screen.getByTestId("upload"));

        expect(mockAPI).toBeCalledTimes(1);
        expect(mockNavigate).toBeCalledWith("/recipes/1234", {replace: true});
    });


    it("should log error after clicking upload button", async () => {
        vi.spyOn(console, "log");
        const mockAPI = vi.spyOn(API, "post").mockImplementation(() => {
            return Promise.reject({error: {status: 403}});
        });

        render(<Upload/>);
        await userEvent.click(screen.getByTestId("upload"));

        expect(mockAPI).toBeCalledTimes(1);
        expect(console.log).toHaveBeenLastCalledWith({error: {status: 403}});
    });
})