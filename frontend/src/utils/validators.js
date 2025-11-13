/**
 * Validate email format
 * @param {string} email - Email to validate
 * @returns {Object} { isValid: boolean, error: string }
 */
export const validateEmail = (email) => {
  if (!email || email.trim() === '') {
    return { isValid: false, error: 'Email requis' };
  }
  
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  
  if (!emailRegex.test(email)) {
    return { isValid: false, error: 'Format email invalide' };
  }
  
  return { isValid: true, error: '' };
};

/**
 * Validate phone number
 * @param {string} phone - Phone to validate
 * @returns {Object} { isValid: boolean, error: string }
 */
export const validatePhone = (phone) => {
  if (!phone || phone.trim() === '') {
    return { isValid: false, error: 'Téléphone requis' };
  }
  
  // Accept formats: +212612345678, 0612345678, etc.
  const phoneRegex = /^(\+212|0)[5-7]\d{8}$/;
  
  if (!phoneRegex.test(phone.replace(/\s/g, ''))) {
    return { isValid: false, error: 'Format téléphone invalide (ex: 0612345678)' };
  }
  
  return { isValid: true, error: '' };
};

/**
 * Validate duration
 * @param {number} hours - Duration in hours
 * @returns {Object} { isValid: boolean, error: string }
 */
export const validateDuration = (hours) => {
  if (!hours || hours <= 0) {
    return { isValid: false, error: 'Durée invalide' };
  }
  
  if (hours < 1 || hours > 24) {
    return { isValid: false, error: 'Durée doit être entre 1 et 24 heures' };
  }
  
  return { isValid: true, error: '' };
};

/**
 * Validate license plate
 * @param {string} plate - License plate to validate
 * @returns {Object} { isValid: boolean, error: string }
 */
export const validateLicensePlate = (plate) => {
  if (!plate || plate.trim() === '') {
    return { isValid: false, error: 'Immatriculation requise' };
  }
  
  // Moroccan format: 12345-A-67 or similar
  const plateRegex = /^\d{1,6}-[A-Z]{1,2}-\d{1,2}$/i;
  
  if (!plateRegex.test(plate.replace(/\s/g, ''))) {
    return { isValid: false, error: 'Format invalide (ex: 12345-A-67)' };
  }
  
  return { isValid: true, error: '' };
};

/**
 * Check if field is empty
 * @param {string} value - Value to check
 * @returns {Object} { isValid: boolean, error: string }
 */
export const isEmptyField = (value) => {
  if (!value || value.trim() === '') {
    return { isValid: false, error: 'Ce champ est requis' };
  }
  
  return { isValid: true, error: '' };
};

/**
 * Validate full name
 * @param {string} name - Name to validate
 * @returns {Object} { isValid: boolean, error: string }
 */
export const validateName = (name) => {
  if (!name || name.trim() === '') {
    return { isValid: false, error: 'Nom complet requis' };
  }
  
  if (name.trim().length < 3) {
    return { isValid: false, error: 'Nom trop court (minimum 3 caractères)' };
  }
  
  return { isValid: true, error: '' };
};