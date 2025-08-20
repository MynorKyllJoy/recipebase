import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import Register from "./components/Register";
import Logout from "./components/Logout";
import Homepage from "./components/Homepage";
import { useState } from "react";
import Upload from "./components/Upload";

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
    // FIX ERROR: JWT Token expired
    const [isLoggedIn, setLoginStatus] = useState(localStorage.getItem("recipebase-user-token") != null)

    const handleLoginStatus = () => {
        setLoginStatus(localStorage.getItem("recipebase-user-token") != null);
    }

    return (<>
        <div>
            <div>
                <a href="/recipes/upload">Upload</a>
            </div>
            <div>
                <a href="/">Home</a>
                {
                    isLoggedIn ? (
                        <>
                            <a href="/profile">Profile</a>
                            <a href="/logout">Logout</a>
                        </>
                    ) : (
                        <>
                            <a href="/register">Register</a>
                            <a href="/login">Login</a>
                        </>
                    )
                }
            </div>
        </div>
        
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Homepage/>}/>
                <Route path="/register" element={<Register setLoginStatus={handleLoginStatus}/>}/>
                <Route path="/login" element={<Login setLoginStatus={handleLoginStatus}/>}/>
                <Route path="/logout" element={<Logout setLoginStatus={handleLoginStatus}/>}/>

                <Route path="/recipes/upload" element={<Upload/>}/>
            </Routes>
        </BrowserRouter>
    </>)
}

export default App;