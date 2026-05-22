import axios from "axios";

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.config && error.config.url.includes("/auth/login")) {
      return Promise.reject(error);
    }

    if (error.response && error.response.status === 401) {
      const token = localStorage.getItem("token");
      if (token && error.config && error.config.url.includes("/auth")) {
        localStorage.removeItem("token");
        localStorage.removeItem("loggedUser");
        window.location.href = "/login";
      }
      return Promise.reject(error);
    }

    return Promise.reject(error);
  },
);

export default api;
