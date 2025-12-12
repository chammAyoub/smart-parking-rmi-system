import axios from "axios";
import { API_BASE_URL, ENDPOINTS } from "../utils/constants";

// Configuration Axios
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

// Intercepteur de requête
api.interceptors.request.use(
  (config) => {
    console.log("🚀 API Request:", config.method.toUpperCase(), config.url);
    return config;
  },
  (error) => {
    console.error("❌ Request Error:", error);
    return Promise.reject(error);
  }
);

// Intercepteur de réponse
api.interceptors.response.use(
  (response) => {
    console.log("✅ API Response:", response.status, response.config.url);
    return response;
  },
  (error) => {
    console.error("❌ Response Error:", error.response?.status, error.message);
    const errorMessage =
      error.response?.data?.message ||
      error.message ||
      "Une erreur est survenue";
    return Promise.reject(new Error(errorMessage));
  }
);

// API Functions

/**
 * Récupérer tous les parkings
 */
export const getAllParkingLots = async () => {
  const response = await api.get(ENDPOINTS.PARKING_LOTS);
  return response.data;
};

/**
 * Récupérer un parking par ID
 */
export const getParkingById = async (id) => {
  const response = await api.get(ENDPOINTS.PARKING_BY_ID(id));
  return response.data;
};

/**
 * Récupérer les places d'un parking
 */
export const getParkingSpots = async (parkingId) => {
  const response = await api.get(ENDPOINTS.PARKING_SPOTS(parkingId));
  return response.data;
};

/**
 * Créer une réservation
 */
export const createReservation = async (reservationData) => {
  const response = await api.post(ENDPOINTS.RESERVATIONS, reservationData);
  return response.data;
};

/**
 * Récupérer les réservations d'un utilisateur
 */
export const getUserReservations = async (email) => {
  const response = await api.get(ENDPOINTS.USER_RESERVATIONS(email));
  return response.data;
};

/**
 * Annuler une réservation
 */
export const cancelReservation = async (id) => {
  await api.delete(ENDPOINTS.CANCEL_RESERVATION(id));
};

// login
export const authenticateUser = async (credentials) => {
  const response = await api.post(ENDPOINTS.AUTHENTIFICATION, credentials);
  return response.data;
};

// register
export const registerUser = async (credentials) => {
  const response = await api.post(ENDPOINTS.REGISTER, credentials);
  return response.data;
};

export default api;
