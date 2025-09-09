import { useState } from "react";
import API from "../config/API";
import { useNavigate } from "react-router-dom";
import type { LoginStatusProps } from "../types/LoginStatusProps";


function Login({setLoginStatus}: LoginStatusProps) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const redirect = useNavigate();

    const login = (event: React.FormEvent) => {
        event.preventDefault();
        API.post("/auth/login", {
            username,
            password
        }).then((response) => {
            localStorage.setItem("recipebase-user-token", response.data);
            setLoginStatus();
            redirect("/");
        }).catch((error) => {
            // TODO: Error Handling, wrong username password or jwt token expired
            console.log(error);
        });
    };

    return (<div className="centerBox">
        <form className="centerForm" onSubmit={login}>

            <div className="centerInput">
                <label>Username</label>
                <input 
                    data-testid="username" 
                    type="text" 
                    value={username} onChange={(e) => setUsername(e.target.value)}
                />
            </div>
                
            <div className="centerInput">
                <label>Password</label>
                <input 
                    data-testid="password" 
                    type="text" 
                    value={password} 
                    onChange={(e) => setPassword(e.target.value)}
                />
            </div>
            <input type="submit" value="Login"/>
        </form>
    </div>);
}

export default Login;
