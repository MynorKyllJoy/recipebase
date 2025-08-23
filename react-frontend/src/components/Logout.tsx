import { useEffect } from "react";
import { Navigate } from "react-router-dom";
import type { LoginStatusProps } from "../types/LoginStatusProps";


function Logout({setLoginStatus}: LoginStatusProps) {
    localStorage.removeItem("recipebase-user-token");
    useEffect(() => { setLoginStatus(); });
    return (<>
        <Navigate to="/login"/>
    </>)
}

export default Logout;