// API Configuration
export const API_BASE_URL = "http://localhost:8081/api";

// Parking Spot Status
export const PARKING_STATUS = {
  AVAILABLE: "AVAILABLE", // Backend uses Uppercase Enums
  OCCUPIED: "OCCUPIED",
  RESERVED: "RESERVED",
};

// Status Colors
export const STATUS_COLORS = {
  [PARKING_STATUS.AVAILABLE]: "#10b981", // green-500
  [PARKING_STATUS.OCCUPIED]: "#ef4444", // red-500
  [PARKING_STATUS.RESERVED]: "#f59e0b", // yellow-500
};

// Map Configuration (Rabat, Morocco)
export const MAP_CONFIG = {
  center: {
    lat: 33.9716,
    lng: -6.8498,
  },
  zoom: 13,
  maxZoom: 18,
  minZoom: 10,
};

// Reservation Duration Options (in hours)
export const DURATION_OPTIONS = [
  { value: 1, label: "1 heure" },
  { value: 2, label: "2 heures" },
  { value: 3, label: "3 heures" },
  { value: 4, label: "4 heures" },
  { value: 5, label: "5 heures" },
  { value: 6, label: "6 heures" },
  { value: 12, label: "12 heures" },
  { value: 24, label: "24 heures" },
];

// Toast Types
export const TOAST_TYPES = {
  SUCCESS: "success",
  ERROR: "error",
  INFO: "info",
  WARNING: "warning",
};

// API Endpoints
export const ENDPOINTS = {
  PARKING_LOTS: "/parking/lots",
  PARKING_BY_ID: (id) => `/parking/${id}`,
  PARKING_SPOTS: (id) => `/parking/${id}/spots/available`,
  RESERVATIONS: "/reservations",
  RESERVATION_BY_ID: (id) => `/reservations/${id}`,
  USER_RESERVATIONS: (email) => `/reservations/user/${email}`,
  CANCEL_RESERVATION: (id) => `/reservations/${id}`,
  AUTHENTIFICATION: "/auth/authenticate",
  REGISTER: "/auth/register",
};
