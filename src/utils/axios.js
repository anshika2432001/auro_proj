import axios from "axios";


const API = axios.create({
//baseURL: "https://auro.one1sewa.com/auro",   //production
baseURL: "http://localhost:8091/auro",   //Local url

});



API.interceptors.request.use(
    config => {
      config.headers["Authorization"] = `Bearer ${localStorage.getItem('token')}`;
      return config;
    },
    error => {
      alert("sd")
      Promise.reject(error);
      
    }
  );
export default API;
