import axios from 'axios';
import { API_BASE_URL } from '../../utils/constants';

const api = axios.create({
  baseURL: `${API_BASE_URL}/admin`,
  headers: { 'Content-Type': 'application/json' }
});

export const simulateCarEnter = async (spotId) => 
  await api.post(`/simulate/enter/${spotId}`);

export const simulateCarExit = async (spotId) => 
  await api.post(`/simulate/exit/${spotId}`);

export const getAdminStats = async () => 
  (await api.get('/stats')).data;