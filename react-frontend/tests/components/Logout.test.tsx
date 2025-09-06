import { render } from "@testing-library/react";
import Logout from "../../src/components/Logout";
import { MemoryRouter } from "react-router-dom";


describe("Login", () => {
    it("should remove JWT token from storage and call setLoginStatus", () => {
        const mockSetLoginStatus = vi.fn();
        const mockLocalStorage = vi.spyOn(Storage.prototype, "removeItem");

        render(<MemoryRouter><Logout setLoginStatus={mockSetLoginStatus}/></MemoryRouter>);

        expect(mockSetLoginStatus).toBeCalledTimes(1);
        expect(mockLocalStorage).toBeCalledTimes(1);
    })
})