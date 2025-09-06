import { render, screen, waitFor } from "@testing-library/react"
import API from "../../src/config/API";
import type { Recipe } from "../../src/types/Recipe";
import RecipeDisplay from "../../src/components/RecipeDisplay";
import { MemoryRouter, Route, Routes } from "react-router-dom";

const recipeData: Recipe = {
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
}  

vi.mock("./src/config/API", async () => ({
    API: {
        ...await vi.importActual("./src/config/API"),
        get: vi.fn()
    }
}));

describe("RecipeDisplay", () => {
    it("should render the recipeData", async () => {
        const mockAPI = vi.spyOn(API, "get").mockImplementation(
            () => Promise.resolve({data: recipeData})
        );

        render(
            <MemoryRouter initialEntries={[`/recipes/1234`]}>
                <Routes>
                    <Route path="/recipes/:recipeId" element={<RecipeDisplay/>}/>
                </Routes>
            </MemoryRouter>
        );

        expect(await waitFor(() => mockAPI)).toBeCalledTimes(1);
        expect(mockAPI).toBeCalledWith("/api/v1/recipes/id/" + recipeData.id);
        expect(screen.getByText(recipeData.title)).toBeInTheDocument();
        expect(screen.getByText(recipeData.description)).toBeInTheDocument();
        expect(screen.getAllByTestId("ingredient")).toHaveLength(3);
        expect(screen.getAllByTestId("instruction")).toHaveLength(3);
        recipeData.ingredientInfos.forEach((ingredient) => {
            expect(screen.getByText(ingredient)).toBeInTheDocument();
        });
        recipeData.instructions.forEach((instruction) => {
            expect(screen.getByText(instruction)).toBeInTheDocument();
        });
    });

    it("should log error after API get call fails", async() => {
        vi.spyOn(console, "log");
        const mockAPI = vi.spyOn(API, "get").mockImplementation(() => {
            return Promise.reject({error: {status: 404}});
        });

        render(<RecipeDisplay/>);

        expect(await waitFor(() => mockAPI)).toBeCalledTimes(1);
        expect(console.log).toBeCalledTimes(1);
        expect(console.log).toHaveBeenLastCalledWith({error: {status: 404}});
    });
})