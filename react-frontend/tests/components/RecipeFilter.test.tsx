import { render, screen, waitFor } from "@testing-library/react";
import RecipeFilter from "../../src/components/RecipeFilter";
import API from "../../src/config/API";
import userEvent from "@testing-library/user-event";
import type { Recipe } from "../../src/types/Recipe";

const ingredients = [
    {name: "eggs"}, 
    {name: "bacon"}, 
    {name: "ham"}
];
const recipes: Recipe[] = [
    {
        id: "1234",
        title: "Kimchi Pancake",
        description: "A simple kimchi pancake recipe.",
        source: "username",
        instructions: [
            "Mix all ingredients into a batter.",
            "Fry the batter 5-10 mins on each side over medium heat.",
            "Serve."
        ],
        ingredientInfos: [
            "1 cup Kimchi, finely chopped.",
            "0.5 cup flour",
            "1/4 cup cold water"
        ]
    }, 
    {
        id: "1234",
        title: "Sunny-sideup Egg",
        description: "A way to cook an egg recipe.",
        source: "admin",
        instructions: [
            "Add oil into a pan.",
            "Heat up oil over medium high heat.",
            "Crack the egg into the pan.",
            "Cook the egg on one side until desired doneness is reached.",
            "Serve."
        ],
        ingredientInfos: [
            "1 large egg",
        ]
    }
];
vi.mock("./src/config/API", async () => ({
    get: vi.fn(),
    post: vi.fn()
}));


describe("RecipeFilter", () => {
    // test init state
    it("should render initial state", () => {
        render(<RecipeFilter/>);
        const textField = screen.getByRole("combobox");
        const addButton = screen.getByText(/add/i);
        const filterButton = screen.getByText(/add/i);

        expect(textField).toHaveValue("");
        expect(addButton).toBeInTheDocument();
        expect(filterButton).toBeInTheDocument();
    });


    // test text input
    it("should render user typed input in textfield", async () => {
        render(<RecipeFilter/>);
        const textField = screen.getByRole("combobox");

        await userEvent.type(textField, "eggs");

        expect(textField).toHaveValue("eggs");
    });


    // test API related behavior
    it("should datalist should contain all ingredients from the API.get call", async () => {
        const mockApiGet = vi.spyOn(API, "get").mockImplementation(() =>  {
            return Promise.resolve({ data: ingredients });
        });

        render(<RecipeFilter/>);

        expect(await waitFor(() => mockApiGet)).toBeCalledTimes(1);
        const ingredientOptions = screen.getAllByTestId("ingredient");
        expect(ingredientOptions).toHaveLength(3);
        ingredients.forEach((ingredient, index) => {
                expect(ingredientOptions[index]).toHaveValue(ingredient.name)
            }
        );
    });


    it("should add typed input to the filter ingredient list", async () => {
        const mockApiGet = vi.spyOn(API, "get").mockImplementation(() =>  {
            return Promise.resolve({ data: ingredients });
        });
        render(<RecipeFilter/>);

        expect(await waitFor(() => mockApiGet)).toBeCalledTimes(1);
        const textField = screen.getByRole("combobox");
        await userEvent.type(textField, "eggs");
        await userEvent.click(screen.getByText(/add/i));

        const addedIngredient = screen.getByRole("listitem");
        expect(addedIngredient).toHaveTextContent("eggs");
        expect(addedIngredient).toHaveTextContent(/delete/i);
        expect(screen.getByText("eggs")).toBeInTheDocument();
        expect(screen.getByText(/delete/i)).toBeInTheDocument();
        expect(textField).toHaveValue("");
    });


    it("should not add typed input to the filter ingredient list", async () => {
        const mockApiGet = vi.spyOn(API, "get").mockImplementation(() =>  {
            return Promise.resolve({ data: ingredients });
        });
        render(<RecipeFilter/>);

        expect(await waitFor(() => mockApiGet)).toBeCalledTimes(1)
        const textField = screen.getByRole("combobox");
        await userEvent.type(textField, "eg");
        await userEvent.click(screen.getByText(/add/i));

        expect(screen.queryAllByRole("listitem")).toHaveLength(0);
        expect(screen.queryByText(/delete/i)).toBeNull();
    });


    it("should add typed input to the filter ingredient list and delete it", async () => {
        const mockApiGet = vi.spyOn(API, "get").mockImplementation(() =>  {
            return Promise.resolve({ data: ingredients });
        });
        render(<RecipeFilter/>);

        expect(await waitFor(() => mockApiGet)).toBeCalledTimes(1)
        const textField = screen.getByRole("combobox");
        await userEvent.type(textField, "eggs");
        await userEvent.click(screen.getByText(/add/i));
        await userEvent.click(screen.getByText(/delete/i));

        expect(screen.queryAllByRole("listitem")).toHaveLength(0);
        expect(screen.queryByText("eggs")).toBeNull();
        expect(screen.queryByText(/delete/i)).toBeNull();
        expect(textField).toHaveValue("");
    });


    it("should render a list of recipes after pressing filter", async () => {
        const mockApiPost = vi.spyOn(API, "post").mockImplementation(() =>  {
            return Promise.resolve({ data: recipes });
        });
        render(<RecipeFilter/>);

        await userEvent.click(screen.getByText(/filter/i));
        const recipeList = screen.getAllByRole("listitem");

        expect(await waitFor(() => mockApiPost)).toBeCalledTimes(1);
        expect(recipeList).toHaveLength(2);
        recipes.forEach((recipe, index) => {
            expect(recipeList[index]).toHaveTextContent(recipe.title)
        });
    });


    it("should log error after API post fails", async () => {
        vi.spyOn(console, "log");
        const mockApiPost = vi.spyOn(API, "post").mockImplementation(() =>  {
            return Promise.reject({ error: { status: 404 } });
        });
        render(<RecipeFilter/>);

        await userEvent.click(screen.getByText(/filter/i));
        expect(await waitFor(() => mockApiPost)).toBeCalledTimes(1)
        expect(console.log).toHaveBeenLastCalledWith({ error: { status: 404 } });
    });
});
