import { render, screen, waitFor } from "@testing-library/react";
import Register from "../../src/components/Register";
import API from "../../src/config/API";
import userEvent from "@testing-library/user-event";


const mockUseNavigate = vi.fn();
vi.mock("./src/config/API", async () => ({
    post: vi.fn()
}));
vi.mock("react-router-dom", async () => ({
    useNavigate: () => mockUseNavigate
}));


describe("Register", () => {
    // test input type behaviour
    it("should display the name the user types in",  async () => {
        render(<Register setLoginStatus={() => {}}/>);
        const nameInput = screen.getByTestId("name");

        expect(nameInput).toHaveValue("");
        
        await userEvent.type(nameInput, "name");
        
        expect(nameInput).toHaveValue("name");
    });


    it("should display the email the user types in",  async () => {
        render(<Register setLoginStatus={() => {}}/>);
        const emailInput = screen.getByTestId("email");

        expect(emailInput).toHaveValue("");
        
        await userEvent.type(emailInput, "email");
        
        expect(emailInput).toHaveValue("email");
    });


    it("should display the username the user types in",  async () => {
        render(<Register setLoginStatus={() => {}}/>);
        const usernameInput = screen.getByTestId("username");

        expect(usernameInput).toHaveValue("");
        
        await userEvent.type(usernameInput, "username");
        
        expect(usernameInput).toHaveValue("username");
    });


    it("should display the password the user types in",  async () => {
        render(<Register setLoginStatus={() => {}}/>);
        const passwordInput = screen.getByTestId("password");

        expect(passwordInput).toHaveValue("");
        
        await userEvent.type(passwordInput, "password");
        
        expect(passwordInput).toHaveValue("password");
    });


    // test API call behavior
    it("should save JWT token in local storage and redirect to / after clicking register button", async () => {
        const mockLocalStorage = vi.spyOn(Storage.prototype, "setItem");
        const mockSetLoginStatus = vi.fn();
        const mockAPI = vi.spyOn(API, "post").mockImplementation(() => {
            return Promise.resolve({data: "1234"})
        });

        render(<Register setLoginStatus={mockSetLoginStatus}/>);

        await userEvent.click(screen.getByText(/register/i));

        expect(mockLocalStorage).toBeCalledTimes(1);
        expect(mockLocalStorage).toBeCalledWith("recipebase-user-token", "1234");
        expect(await waitFor(() => mockAPI)).toBeCalledTimes(1)
        expect(mockUseNavigate).toBeCalledTimes(1);
        expect(mockUseNavigate).toBeCalledWith("/");
    });


    it("should log error after API.post fails", async () => {
        vi.spyOn(console, "log");
        const mockSetLoginStatus = vi.fn();
        const mockAPI = vi.spyOn(API, "post").mockImplementation(() => {
            return Promise.reject({error: {status: 403}});
        });

        render(<Register setLoginStatus={() => {}}/>);

        await userEvent.click(screen.getByText(/register/i));

        expect(mockAPI).toBeCalledTimes(1);
        expect(mockSetLoginStatus).toBeCalledTimes(0);
        expect(console.log).toHaveBeenLastCalledWith({error: {status: 403}});
    });
});
