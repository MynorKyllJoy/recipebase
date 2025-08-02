import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import Register from "./components/Register";
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
            <a href="/login">Login</a>
            <a href="/register">Register</a>
            <a href="/logout">Logout</a>
        </div>
        <BrowserRouter>
            <Routes>
                <Route path="/register" element={<Register/>}/>
                <Route path="/login" element={<Login/>}/>
            </Routes>
        </BrowserRouter>
    </>)
}

export default App;