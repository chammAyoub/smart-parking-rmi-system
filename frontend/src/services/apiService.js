import axios from "axios";
import { API_BASE_URL, ENDPOINTS } from "../utils/constants";

// ============================================================
// 1. INSTANCE PUBLIC (Bla Token) - L Reservation, Login, Register
// ============================================================
const publicApi = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

// Interceptor (Ghir Logs - Bla Token Logic)
publicApi.interceptors.request.use((config) => {
  console.log("🔓 Public Request:", config.method.toUpperCase(), config.url);
  return config;
});

// ============================================================
// 2. INSTANCE PRIVATE (Avec Token) - L Historique, Cancel...
// ============================================================
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

// Intercepteur de requête (HNA FIN KANZIDO TOKEN 🔐)
api.interceptors.request.use(
  (config) => {
    // Njibo token mn localStorage
    const user = JSON.parse(localStorage.getItem("user"));
    const token = user?.token || localStorage.getItem("token"); // Fallback

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    console.log("🔒 Private Request:", config.method.toUpperCase(), config.url);
    return config;
  },
  (error) => {
    console.error("❌ Request Error:", error);
    return Promise.reject(error);
  }
);

// Intercepteur de réponse (Gestion d'erreurs global)
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

// ============================================================
// API Functions
// ============================================================

/**
 * Récupérer tous les parkings (PUBLIC)
 */
export const getAllParkingLots = async () => {
  const response = await publicApi.get(ENDPOINTS.PARKING_LOTS);
  return response.data;
};

/**
 * Récupérer un parking par ID (PUBLIC)
 */
export const getParkingById = async (id) => {
  const response = await publicApi.get(ENDPOINTS.PARKING_BY_ID(id));
  return response.data;
};

/**
 * Récupérer les places d'un parking (PUBLIC)
 */
export const getParkingSpots = async (parkingId) => {
  const response = await publicApi.get(ENDPOINTS.PARKING_SPOTS(parkingId));
  return response.data;
};

/**
 * Créer une réservation (PUBLIC - Sans Token)
 * 
 */
export const createReservation = async (reservationData) => {
  const response = await publicApi.post(ENDPOINTS.RESERVATIONS, reservationData);
  return response.data;
};

/**
 * Récupérer les réservations d'un utilisateur (PRIVATE - Avec Token)
 *
 */
export const getUserReservations = async (email) => {
  const response = await api.get(ENDPOINTS.USER_RESERVATIONS(email));
  return response.data;
};

/**
 * Annuler une réservation (PRIVATE - Avec Token)
 */
export const cancelReservation = async (id) => {
  await api.post(ENDPOINTS.CANCEL_RESERVATION(id));
};

// Login (PUBLIC)
export const authenticateUser = async (credentials) => {
  const response = await publicApi.post(ENDPOINTS.AUTHENTIFICATION, credentials);
  return response.data;
};

// Register (PUBLIC)
export const registerUser = async (credentials) => {
  const response = await publicApi.post(ENDPOINTS.REGISTER, credentials);
  return response.data;
};

// Add parking 
export const addParking = async (parkingData) => {
  const response = await api.post(ENDPOINTS.ADD_PARKING, parkingData);
  return response.data;
};

// simulate enter 

export const simulateCarEnter = async (id) => {
  const response = await api.post(ENDPOINTS.ENTER_SIMULATION(id));
  return response.data;
};


// simulate exit 
export const simulateCarExit = async (id) => {
  const response = await api.post(ENDPOINTS.EXIT_SIMULATION(id));
  return response.data;
};

// pariking stats 
export const parkingStats = async (id) => {
  const response = await api.get(ENDPOINTS.PARKING_STATS(id));
  return response.data;
};


// Par défaut on export l'instance privée
export default api;