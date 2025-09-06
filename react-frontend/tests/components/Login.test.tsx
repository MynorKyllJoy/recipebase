import { render, screen } from "@testing-library/react"
import Login from "../../src/components/Login"
import userEvent from "@testing-library/user-event"
import API from "../../src/config/API"
import { useNavigate } from "react-router-dom";


describe("Login", () => {
    // mock API, useNavigate, and localStorage
    vi.mock("./src/config/API", async () => ({
        API: {
            ...await vi.importActual("./src/config/API"),
            post: vi.fn()
        }
    }));
    vi.mock("react-router-dom", async () => ({
        useNavigate: vi.fn()
    }));
    vi.mock("localStorage", () => ({
        setItem: vi.fn(),
    }));


    afterEach(() => vi.restoreAllMocks());


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
        const mockNavigate = vi.fn();
        const mockSetLoginStatus = vi.fn();
        (useNavigate as ReturnType<typeof vi.fn>).mockReturnValue(mockNavigate);
        const mockAPI = vi.spyOn(API, "post").mockImplementation(() => {
            return Promise.resolve({data: "JWTToken"});
        });
        const mockLocalStorage = vi.spyOn(Storage.prototype, "setItem");

        render(<Login setLoginStatus={mockSetLoginStatus}/>)

        await userEvent.click(screen.getByText(/login/i));

        // test if API, localStorage, setLoginStatus, and useNavigate were called correctly
        expect(mockAPI).toBeCalledTimes(1);
        expect(mockLocalStorage).toBeCalledTimes(1);
        expect(mockLocalStorage).toHaveBeenCalledWith("recipebase-user-token", "JWTToken");
        expect(mockSetLoginStatus).toBeCalledTimes(1);
        expect(mockNavigate).toBeCalledTimes(1);
        expect(mockNavigate).toHaveBeenCalledWith("/");
    });


    it("should log error after clicking Login button", async () => {
        vi.spyOn(console, "log");
        const mockAPI = vi.spyOn(API, "post").mockImplementation(() => {
            return Promise.reject({error: {status: 403}});
        });
        render(<Login setLoginStatus={() => {}}/>)

        await userEvent.click(screen.getByText(/login/i));

        expect(mockAPI).toBeCalledTimes(1);
        expect(console.log).toHaveBeenLastCalledWith({error: {status: 403}})
    });
});