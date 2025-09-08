import { useState } from "react";
import API from "../config/API";
import { useNavigate } from "react-router-dom";
import type { LoginStatusProps } from "../types/LoginStatusProps";


function Register({setLoginStatus}: LoginStatusProps) {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const redirect = useNavigate();

    const register = (event: React.FormEvent) => {
        event.preventDefault();
        API.post("/auth/register", {
            name,
            username,
            password,
            email
        }).then((response) => {
            localStorage.setItem("recipebase-user-token", response.data);
            setLoginStatus();
            redirect("/");
        }).catch((error) => {
            // TODO: Error Handling, username taken, password conditions not met
            console.log(error);
        });
    };

    return (
        <form onSubmit={register}>
            <label> Name
                <input data-testid="name" type="text" value={name} onChange={(e) => setName(e.target.value)}/><br/>
            </label>
            <label>E-mail
                <input data-testid="email" type="text" value={email} onChange={(e) => setEmail(e.target.value)}/><br/>
            </label>
            <label>Username
                <input data-testid="username" type="text" value={username} onChange={(e) => setUsername(e.target.value)}/><br/>
            </label>
            <label>Password
                <input data-testid="password" type="text" value={password} onChange={(e) => setPassword(e.target.value)}/><br/>
            </label>
            <input type="submit" value={"Register"}/>
        </form>
    );
}

export default Register;
