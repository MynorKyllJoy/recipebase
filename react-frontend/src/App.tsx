import "./App.css"
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import Register from "./components/Register";
import Logout from "./components/Logout";
import Homepage from "./components/Homepage";
import { useEffect, useState } from "react";
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
        const jwtToken = localStorage.getItem("recipebase-user-token");
        if(jwtToken !== null) {
            try {
                let expirationDate = atob(jwtToken.split(".")[1]);
                expirationDate = atob(expirationDate);
                if (Number(expirationDate)*1000 < Date.now()) {
                    localStorage.removeItem("recipebase-user-token")
                }
            } catch(error) {
                // TODO: JWT token is expired, number decoding was wrong
                console.log(error)
            }
        }
        setLoginStatus(localStorage.getItem("recipebase-user-token") != null);
    }
    useEffect(() => {
        handleLoginStatus()
    }, [isLoggedIn]);

    return (<>
        <div className="navbar">
            <div className="navOptions">
                <a className="navButton" href="/">Home</a>
                <a className="navButton" href="/recipes/all">All Recipes</a>
                <a className="navButton" href="/recipes/upload">Upload</a>
                <a className="navButton" href="/recipes/scrape">Scrape</a>
                <a className="navButton" href="/recipes/filter">Filter</a>
            </div>
            <div className="navSettings">
                {
                    isLoggedIn ? (
                        <>
                            <a className="navButton" href="/profile">Profile</a>
                            <a className="navButton" href="/logout">Logout</a>
                        </>
                    ) : (
                        <>
                            <a className="navButton" href="/register">Register</a>
                            <a className="navButton" href="/login">Login</a>
                        </>
                    )
                }
            </div>
        </div>
        
        <div className="mainContent">
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
        </div>

    </>)
}

export default App;