import { useState } from "react";
import api from "./axios_config";
import { useNavigate } from "react-router-dom";
import type { LoginStatusProps } from "./LoginStatusProps";


function Login({setLoginStatus}: LoginStatusProps) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const redirect = useNavigate();

    const login = (event: React.FormEvent) => {
        event.preventDefault();
        api.post("/auth/login", {
            username,
            password
        }).then((response) => {
            localStorage.setItem("recipebase-user-token", response.data);
            setLoginStatus();
            redirect("/");
        }).catch((error) => {
            // TODO: Error Handling, wrong username password or jwt token expired
            console.log(error);
        })
    }

    return (<>
        <form onSubmit={login}>
            <label>Username
                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)}/><br/>
            </label>
            <label>Password
                <input type="text" value={password} onChange={(e) => setPassword(e.target.value)}/><br/>
            </label>
            <input type="submit"/>
        </form>
    </>);
}

export default Login;