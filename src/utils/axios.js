import axios from "axios";


const API = axios.create({
 baseURL: "http://auro.one1sewa.com/auro",   //production
//baseURL: "http://localhost:8091/auro",   //Local

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
