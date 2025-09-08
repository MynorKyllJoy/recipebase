import { render, screen } from "@testing-library/react";
import Login from "../../src/components/Login";
import userEvent from "@testing-library/user-event";
import API from "../../src/config/API";


// mock API, useNavigate, and localStorage
const mockUseNavigate = vi.fn();
vi.mock("./src/config/API", async () => ({
    API: {
        ...await vi.importActual("./src/config/API"),
        post: vi.fn()
    }
}));
vi.mock("react-router-dom", async () => ({
    useNavigate: () => mockUseNavigate
}));
vi.mock("localStorage", () => ({
    setItem: vi.fn(),
}));


describe("Login", () => {
    it("should render the usename after the user types one in", async () => {
        render(<Login setLoginStatus={() => {}}/>);
        const usernameInput = screen.getByTestId("username");

        expect(usernameInput).toHaveValue("");
        await userEvent.type(usernameInput, "user");
        expect(usernameInput).toHaveValue("user");
    });


    it("should render the password after the user types one in", async () => {
        render(<Login setLoginStatus={() => {}}/>);
        const passwordInput = screen.getByTestId("password");

        expect(passwordInput).toHaveValue("");
        await userEvent.type(passwordInput, "password");
        expect(passwordInput).toHaveValue("password");
    });


    it("should store JWT and redirect to / after clicking Login button", async () => {
        const mockSetLoginStatus = vi.fn();
        const mockAPI = vi.spyOn(API, "post").mockImplementation(() => {
            return Promise.resolve({data: "JWTToken"});
        });
        const mockLocalStorage = vi.spyOn(Storage.prototype, "setItem");

        render(<Login setLoginStatus={mockSetLoginStatus}/>);

        await userEvent.click(screen.getByText(/login/i));

        // test if API, localStorage, setLoginStatus, and useNavigate were called correctly
        expect(mockAPI).toBeCalledTimes(1);
        expect(mockLocalStorage).toBeCalledTimes(1);
        expect(mockLocalStorage).toHaveBeenCalledWith("recipebase-user-token", "JWTToken");
        expect(mockSetLoginStatus).toBeCalledTimes(1);
        expect(mockUseNavigate).toBeCalledTimes(1);
        expect(mockUseNavigate).toHaveBeenCalledWith("/");
    });


    it("should log error after clicking Login button", async () => {
        vi.spyOn(console, "log");
        const mockAPI = vi.spyOn(API, "post").mockImplementation(() => {
            return Promise.reject({error: {status: 403}});
        });
        render(<Login setLoginStatus={() => {}}/>);

        await userEvent.click(screen.getByText(/login/i));

        expect(mockAPI).toBeCalledTimes(1);
        expect(console.log).toHaveBeenLastCalledWith({error: {status: 403}})
    });
});
