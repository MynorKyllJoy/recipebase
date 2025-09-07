import { render, screen, waitFor } from "@testing-library/react";
import RecipeScraper from "../../src/components/RecipScraper";
import API from "../../src/config/API";
import userEvent from "@testing-library/user-event";


const mockUseNavigate = vi.fn();
vi.mock("./src/config/API", async () => ({
    post: vi.fn()
}));
vi.mock("react-router-dom", async () => ({
    useNavigate: () => mockUseNavigate
}));


describe("RecipeScraper", () => {
    it("should render initial state", async () => {
        render(<RecipeScraper/>);
        const button = screen.getByRole("button");
        const textField = screen.getByRole("textbox");

        expect(button).toBeInTheDocument();
        expect(button).toHaveValue("Scrape");
        expect(textField).toBeInTheDocument();
        expect(textField).toHaveValue("");
    });


    it("should render user typed/pasted recipe site link", async () => {
        render(<RecipeScraper/>);
        const textField = screen.getByRole("textbox");
        
        await userEvent.type(textField, "https://example.com");

        expect(textField).toHaveValue("https://example.com");
    });


    it("should redirect to scraped recipe", async () => {
        render(<RecipeScraper/>);
        
        const button = screen.getByRole("button");
        const mockAPI = vi.spyOn(API, "post").mockImplementation(
            () => Promise.resolve({data: {id: "1234"}})
        );

        await userEvent.click(button);

        expect(await waitFor(() => mockAPI)).toBeCalledTimes(1);
        expect(mockUseNavigate).toBeCalledTimes(1);
        expect(await waitFor(() => mockUseNavigate)).toBeCalledWith("/recipes/1234", {replace: true})
    });


    it("should log error after API.get fails", async () => {
        render(<RecipeScraper/>);
        const button = screen.getByRole("button");
        const mockAPI = vi.spyOn(API, "post").mockImplementation(
            () => Promise.reject({error: {status: 404}})
        );
        vi.spyOn(console, "log");

        await userEvent.click(button);
        
        expect(await waitFor(() => mockAPI)).toBeCalledTimes(1);
        expect(console.log).toHaveBeenLastCalledWith({error: {status: 404}});
    });
});
