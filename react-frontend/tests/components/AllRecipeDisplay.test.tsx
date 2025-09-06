import { render, screen } from "@testing-library/react";
import { waitFor } from "@testing-library/dom";
import API from "../../src/config/API";
import type { Recipe } from "../../src/types/Recipe";
import AllRecipeDisplay from "../../src/components/AllRecipeDisplay";


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
]
vi.mock("./src/config/API", async () => ({
    get: vi.fn()
}));

describe("AllRecipeDisplay", () => {
    it("should display all recipes from recipeList", async () => {
        const mockAPI = vi.spyOn(API, "get").mockImplementation(() => {
            return Promise.resolve({data: recipes});
        });

        render(<AllRecipeDisplay/>)
        expect(await waitFor(() => mockAPI)).toBeCalledTimes(1);

        const recipeList = screen.getAllByRole("listitem");
        expect(recipeList).toHaveLength(recipes.length);

        recipes.forEach((recipe, index) => {
            expect(recipeList[index]).toHaveTextContent(recipe.title);
            expect(recipeList[index]).toHaveTextContent(recipe.source);
            expect(recipeList[index]).toHaveTextContent(recipe.description);
        })
    });

    
    it("should log error when API get fails", async () => {
        vi.spyOn(console, "log");
        const mockAPI = vi.spyOn(API, "get").mockImplementation(() => {
            return Promise.reject({error: { status: 404 }});
        });

        render(<AllRecipeDisplay/>)
        expect(await waitFor(() => mockAPI)).toBeCalledTimes(1);
        expect(console.log).toHaveBeenLastCalledWith({error: { status: 404 }});
    });
});