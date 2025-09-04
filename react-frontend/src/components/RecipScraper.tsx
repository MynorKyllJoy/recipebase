import { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../config/API";

function RecipeScraper() {
    // TODO: Add security measures
    const [recipeSite, setRecipeSite] = useState(""); 
    const navigate = useNavigate();

    const scrapeSite = (event: React.FormEvent) => {
        event.preventDefault();
        API.post(
            "/api/v1/recipes/scrape", {
                recipeSite
            }
        ).then((response) => {
            console.log(response);
            navigate(`/recipes/${response.data.id}`, {replace: true})
        }).catch((error) => {
            // TODO: Error Handling, wrong username password or jwt token expired
            console.log(error);
        })
    }

    return (<form onSubmit={scrapeSite}>
        <input type="text" value={recipeSite} onChange={(e) => setRecipeSite(e.target.value)}/>
        <input type="submit" value="Scrape"/>
    </form>)
}

export default RecipeScraper;