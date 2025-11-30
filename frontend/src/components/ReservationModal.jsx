import React, { useState } from 'react';
import { X, User, Mail, Phone, Car, Clock, Calendar } from 'lucide-react';
import { validateEmail, validatePhone, validateName, validateLicensePlate } from '../utils/validators';
import { calculateEndTime, getCurrentDateTime } from '../utils/dateUtils';
import { DURATION_OPTIONS } from '../utils/constants';

const ReservationModal = ({ isOpen, onClose, parking, spot, onConfirm }) => {
  const [formData, setFormData] = useState({
    userName: '',
    userEmail: '',
    userPhone: '',
    licensePlate: '',
    duration: 2,
    startTime: getCurrentDateTime()
  });

  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    //clear error when user types
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    const nameValidation = validateName(formData.userName);
    if (!nameValidation.isValid) {
      newErrors.userName = nameValidation.error;
    }

    const emailValidation = validateEmail(formData.userEmail);
    if (!emailValidation.isValid) {
      newErrors.userEmail = emailValidation.error;
    }

    const phoneValidation = validatePhone(formData.userPhone);
    if (!phoneValidation.isValid) {
      newErrors.userPhone = phoneValidation.error;
    }

    const plateValidation = validateLicensePlate(formData.licensePlate);
    if (!plateValidation.isValid) {
      newErrors.licensePlate = plateValidation.error;
    }

    if (!formData.startTime) {
      newErrors.startTime = 'Date et heure requises';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      const endTime = calculateEndTime(formData.startTime, formData.duration);

      const reservationData = {
        parkingId: parking.id,
        spotId: spot.id,
        spotNumber: spot.spotNumber,
        userName: formData.userName,
        userEmail: formData.userEmail,
        userPhone: formData.userPhone,
        licensePlate: formData.licensePlate,
        startTime: formData.startTime,
        endTime: endTime.toISOString(),
        duration: formData.duration,
        parkingName: parking.name
      };

      await onConfirm(reservationData);
    } catch (error) {
      console.error('Erreur lors de la réservation:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4">
      <div className="bg-white rounded-2xl shadow-2xl max-w-md w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="sticky top-0 bg-gradient-to-r from-primary to-secondary text-white p-6 rounded-t-2xl">
          <div className="flex justify-between items-start">
            <div>
              <h2 className="text-2xl font-bold mb-1">Réservation</h2>
              <p className="text-white/90 text-sm">{parking.name} - Place {spot.spotNumber}</p>
            </div>
            <button
              onClick={onClose}
              className="bg-white/20 hover:bg-white/30 rounded-full p-2 transition"
              disabled={loading}
            >
              <X className="w-6 h-6" />
            </button>
          </div>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          {/* Nom complet */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              <User className="w-4 h-4 inline mr-2" />
              Nom complet *
            </label>
            <input
              type="text"
              name="userName"
              value={formData.userName}
              onChange={handleChange}
              placeholder="Ex: Ahmed Bennani"
              className={`
                w-full px-4 py-3 border rounded-lg
                focus:outline-none focus:ring-2 focus:ring-primary
                ${errors.userName ? 'border-red-500' : 'border-gray-300'}
              `}
            />
            {errors.userName && (
              <p className="text-red-500 text-sm mt-1">{errors.userName}</p>
            )}
          </div>

          {/* Email */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              <Mail className="w-4 h-4 inline mr-2" />
              Email *
            </label>
            <input
              type="email"
              name="userEmail"
              value={formData.userEmail}
              onChange={handleChange}
              placeholder="ahmed@example.com"
              className={`
                w-full px-4 py-3 border rounded-lg
                focus:outline-none focus:ring-2 focus:ring-primary
                ${errors.userEmail ? 'border-red-500' : 'border-gray-300'}
              `}
            />
            {errors.userEmail && (
              <p className="text-red-500 text-sm mt-1">{errors.userEmail}</p>
            )}
          </div>

          {/* Téléphone */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              <Phone className="w-4 h-4 inline mr-2" />
              Téléphone *
            </label>
            <input
              type="tel"
              name="userPhone"
              value={formData.userPhone}
              onChange={handleChange}
              placeholder="0612345678"
              className={`
                w-full px-4 py-3 border rounded-lg
                focus:outline-none focus:ring-2 focus:ring-primary
                ${errors.userPhone ? 'border-red-500' : 'border-gray-300'}
              `}
            />
            {errors.userPhone && (
              <p className="text-red-500 text-sm mt-1">{errors.userPhone}</p>
            )}
          </div>

          {/* Immatriculation */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              <Car className="w-4 h-4 inline mr-2" />
              Immatriculation *
            </label>
            <input
              type="text"
              name="licensePlate"
              value={formData.licensePlate}
              onChange={handleChange}
              placeholder="12345-A-67"
              className={`
                w-full px-4 py-3 border rounded-lg
                focus:outline-none focus:ring-2 focus:ring-primary
                ${errors.licensePlate ? 'border-red-500' : 'border-gray-300'}
              `}
            />
            {errors.licensePlate && (
              <p className="text-red-500 text-sm mt-1">{errors.licensePlate}</p>
            )}
          </div>

          {/* Date et heure */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              <Calendar className="w-4 h-4 inline mr-2" />
              Date et heure de début *
            </label>
            <input
              type="datetime-local"
              name="startTime"
              value={formData.startTime}
              onChange={handleChange}
              min={getCurrentDateTime()}
              className={`
                w-full px-4 py-3 border rounded-lg
                focus:outline-none focus:ring-2 focus:ring-primary
                ${errors.startTime ? 'border-red-500' : 'border-gray-300'}
              `}
            />
            {errors.startTime && (
              <p className="text-red-500 text-sm mt-1">{errors.startTime}</p>
            )}
          </div>

          {/* Durée */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              <Clock className="w-4 h-4 inline mr-2" />
              Durée de stationnement *
            </label>
            <select
              name="duration"
              value={formData.duration}
              onChange={handleChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            >
              {DURATION_OPTIONS.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>

          {/* Prix estimé */}
          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
            <div className="flex justify-between items-center">
              <span className="text-gray-700 font-semibold">Prix estimé :</span>
              <span className="text-2xl font-bold text-primary">
                {formData.duration * 10} DH
              </span>
            </div>
            <p className="text-xs text-gray-600 mt-1">Tarif : 10 DH/heure</p>
          </div>

          {/* Buttons */}
          <div className="flex gap-3 pt-4">
            <button
              type="button"
              onClick={onClose}
              disabled={loading}
              className="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold py-3 px-6 rounded-lg transition disabled:opacity-50"
            >
              Annuler
            </button>
            <button
              type="submit"
              disabled={loading}
              className={`
                flex-1 font-semibold py-3 px-6 rounded-lg transition
                ${loading
                  ? 'bg-gray-400 cursor-not-allowed'
                  : 'bg-primary hover:bg-secondary text-white'
                }
              `}
            >
              {loading ? 'Réservation...' : 'Confirmer'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ReservationModal;