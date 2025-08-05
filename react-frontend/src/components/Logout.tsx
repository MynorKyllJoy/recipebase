import { Navigate } from "react-router-dom";

function Logout() {
    localStorage.removeItem("recipebase-user-token");
    return (<>
        <Navigate to="/login"/>
    </>)
}

export default Logout;