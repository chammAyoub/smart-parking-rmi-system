import axios from 'axios';
import { API_BASE_URL, ENDPOINTS } from '../utils/constants';

// Configuration Axios
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Intercepteur de requête
api.interceptors.request.use(
  (config) => {
    console.log('🚀 API Request:', config.method.toUpperCase(), config.url);
    return config;
  },
  (error) => {
    console.error('❌ Request Error:', error);
    return Promise.reject(error);
  }
);

// Intercepteur de réponse
api.interceptors.response.use(
  (response) => {
    console.log('✅ API Response:', response.status, response.config.url);
    return response.data;
  },
  (error) => {
    console.error('❌ Response Error:', error.response?.status, error.message);
    
    const errorMessage = error.response?.data?.message || 
                        error.response?.data?.error || 
                        error.message || 
                        'Une erreur est survenue';
    
    return Promise.reject(new Error(errorMessage));
  }
);

// API Functions

/**
 * Récupérer tous les parkings
 */
export const getAllParkingLots = async () => {
  try {
    const data = await api.get(ENDPOINTS.PARKING_LOTS);
    return data;
  } catch (error) {
    throw error;
  }
};

/**
 * Récupérer un parking par ID
 */
export const getParkingById = async (id) => {
  try {
    const data = await api.get(ENDPOINTS.PARKING_BY_ID(id));
    return data;
  } catch (error) {
    throw error;
  }
};

/**
 * Récupérer les places d'un parking
 */
export const getParkingSpots = async (parkingId) => {
  try {
    const data = await api.get(ENDPOINTS.PARKING_SPOTS(parkingId));
    return data;
  } catch (error) {
    throw error;
  }
};

/**
 * Créer une réservation
 */
export const createReservation = async (reservationData) => {
  try {
    const data = await api.post(ENDPOINTS.RESERVATIONS, reservationData);
    return data;
  } catch (error) {
    throw error;
  }
};

/**
 * Récupérer une réservation par ID
 */
export const getReservationById = async (id) => {
  try {
    const data = await api.get(ENDPOINTS.RESERVATION_BY_ID(id));
    return data;
  } catch (error) {
    throw error;
  }
};

/**
 * Récupérer les réservations d'un utilisateur
 */
export const getUserReservations = async (email) => {
  try {
    const data = await api.get(ENDPOINTS.USER_RESERVATIONS(email));
    return data;
  } catch (error) {
    throw error;
  }
};

/**
 * Annuler une réservation
 */
export const cancelReservation = async (id) => {
  try {
    const data = await api.delete(ENDPOINTS.CANCEL_RESERVATION(id));
    return data;
  } catch (error) {
    throw error;
  }
};

export default api;