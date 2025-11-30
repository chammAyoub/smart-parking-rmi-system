/**
 * Format a date to DD/MM/YYYY HH:mm
 * @param {Date|string} date - Date to format
 * @returns {string} Formatted date string
 */
export const formatDate = (date) => {
  if (!date) return '';
  
  const d = new Date(date);
  const day = String(d.getDate()).padStart(2, '0');
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const year = d.getFullYear();
  const hours = String(d.getHours()).padStart(2, '0');
  const minutes = String(d.getMinutes()).padStart(2, '0');
  
  return `${day}/${month}/${year} ${hours}:${minutes}`;
};

/**
 * Format duration in hours to human readable format
 * @param {number} hours - Duration in hours
 * @returns {string} Formatted duration (e.g., "2h30" or "30min")
 */
export const formatDuration = (hours) => {
  if (hours < 1) {
    return `${Math.round(hours * 60)}min`;
  }
  
  const h = Math.floor(hours);
  const m = Math.round((hours - h) * 60);
  
  if (m === 0) {
    return `${h}h`;
  }
  
  return `${h}h${m}`;
};

/**
 * Calculate end time based on start time and duration
 * @param {Date|string} startDate - Start date
 * @param {number} durationHours - Duration in hours
 * @returns {Date} End date
 */
export const calculateEndTime = (startDate, durationHours) => {
  const start = new Date(startDate);
  const end = new Date(start.getTime() + durationHours * 60 * 60 * 1000);
  return end;
};

/**
 * Check if a date has expired
 * @param {Date|string} endDate - End date to check
 * @returns {boolean} True if expired
 */
export const isExpired = (endDate) => {
  if (!endDate) return false;
  return new Date(endDate) < new Date();
};

/**
 * Get time remaining until end date
 * @param {Date|string} endDate - End date
 * @returns {string} Time remaining (e.g., "2h 15min restantes")
 */
export const getTimeRemaining = (endDate) => {
  if (!endDate) return '';
  
  const now = new Date();
  const end = new Date(endDate);
  const diff = end - now;
  
  if (diff <= 0) {
    return 'Expiré';
  }
  
  const hours = Math.floor(diff / (1000 * 60 * 60));
  const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
  
  if (hours > 0) {
    return `${hours}h ${minutes}min restantes`;
  }
  
  return `${minutes}min restantes`;
};

/**
 * Get current date time in format for datetime-local input
 * @returns {string} Formatted datetime string
 */
export const getCurrentDateTime = () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  
  return `${year}-${month}-${day}T${hours}:${minutes}`;
};