import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import Register from "./components/Register";
import Logout from "./components/Logout";
import Homepage from "./components/Homepage";
import { useState } from "react";
import Upload from "./components/Upload";
import RecipeDisplay from "./components/RecipeDisplay";
import RecipeListDisplay from "./components/AllRecipeDisplay";
import RecipeScraper from "./components/RecipScraper";
import RecipeFilter from "./components/RecipeFilter";

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
                <a href="/">Home</a>
                <a href="/recipes/all">All Recipes</a>
                <a href="/recipes/upload">Upload</a>
                <a href="/recipes/scrape">Scrape</a>
                <a href="/recipes/filter">Filter</a>
            </div>
            <div>
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
                <Route path="/recipes/:recipeId" element={<RecipeDisplay/>}/>
                <Route path="/recipes/all" element={<RecipeListDisplay/>}/>
                <Route path="/recipes/scrape" element={<RecipeScraper/>}/>
                <Route path="/recipes/filter" element={<RecipeFilter/>}/>
            </Routes>
        </BrowserRouter>
    </>)
}

export default App;