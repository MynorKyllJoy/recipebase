import { useState } from "react";

function Register() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");


    return (
        <form>
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