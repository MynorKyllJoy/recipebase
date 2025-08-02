import { useState } from "react";

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    return (<>
        <form>
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