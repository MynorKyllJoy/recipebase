import { useState } from "react";
import api from "../config/axios_config";
import { useNavigate } from "react-router-dom";
import type { LoginStatusProps } from "./LoginStatusProps";


function Register({setLoginStatus}: LoginStatusProps) {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const redirect = useNavigate();

    const register = (event: React.FormEvent) => {
        event.preventDefault();
        api.post("/auth/register", {
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
        })
    };

    return (
        <form onSubmit={register}>
            <label> Name
                <input type="text" value={name} onChange={(e) => setName(e.target.value)}/><br/>
            </label>
            <label>E-mail
                <input type="text" value={email} onChange={(e) => setEmail(e.target.value)}/><br/>
            </label>
            <label>Username
                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)}/><br/>
            </label>
            <label>Password
                <input type="text" value={password} onChange={(e) => setPassword(e.target.value)}/><br/>
            </label>
            <input type="submit"/>
        </form>
    );
}

export default Register;