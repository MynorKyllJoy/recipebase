import axios from "axios";

const API = axios.create({
    baseURL: "http://localhost:8080/",
    headers: {
        "Content-Type": "application/json",
    }
});

API.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("recipebase-user-token");
        if(token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config
    },
    (error) => {
        return Promise.reject(error);
    }
);

API.interceptors.response.use(
    (response) => response,
    (error) => {
        if(error.response.status === 401) {
            localStorage.removeItem("recipebase-user-token");
            window.location.href = "/login";
        } else if(error.response.status === 403) {
            localStorage.removeItem("recipebase-user-token");
            window.location.href = "/login";
        }
        return Promise.reject(error);
    }
);

export default API;