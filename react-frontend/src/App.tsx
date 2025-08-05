import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import Register from "./components/Register";
import Logout from "./components/Logout";
import Homepage from "./components/Homepage";
/*
interface User {
    id: string,
    name: string,
    username: string,
    email: string,
    password: string
}
*/

function App() {
    return (<>
        <div>
            <a href="/">Home</a>
            <a href="/login">Login</a>
            <a href="/register">Register</a>
            <a href="/logout">Logout</a>
        </div>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Homepage/>}/>
                <Route path="/register" element={<Register/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/logout" element={<Logout/>}/>
            </Routes>
        </BrowserRouter>
    </>)
}

export default App;