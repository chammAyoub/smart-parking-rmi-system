import React, { useState } from 'react';
import { X, User, Mail, Phone, Car, Clock, Calendar } from 'lucide-react';
import { validateEmail, validatePhone, validateName, validateLicensePlate } from '../utils/validators';
import { calculateEndTime, getCurrentDateTime } from '../utils/dateUtils';
import { DURATION_OPTIONS } from '../utils/constants';
import { createReservation } from '../services/apiService';

const ReservationModal = ({ isOpen, onClose, parking, spot, onSuccess }) => {
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
  const [apiError, setApiError] = useState(null);

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors(prev => ({ ...prev, [name]: '' }));
    setApiError(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setApiError(null);

    // Validation basique (tu peux garder tes validateurs ici)
    if (!formData.userName || !formData.userEmail) {
        setErrors({ userName: 'Requis', userEmail: 'Requis' });
        return;
    }

    setLoading(true);

    try {
      const endTime = calculateEndTime(formData.startTime, formData.duration);

      const reservationPayload = {
        parkingLotId: parking.id,
        parkingSpotId: spot.id,
        userName: formData.userName,
        userEmail: formData.userEmail,
        userPhone: formData.userPhone,
        licensePlate: formData.licensePlate,
        startTime: formData.startTime,
        endTime: endTime.toISOString(),
        durationHours: formData.duration,
        spotNumber: spot.spotNumber,
        parkingName: parking.name
      };

      await createReservation(reservationPayload);
      onSuccess(); // Ferme le modal et refresh
    } catch (error) {
      console.error('Erreur réservation:', error);
      setApiError(error.message || "Erreur lors de la réservation");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4 animate-fade-in">
      <div className="bg-white rounded-2xl shadow-2xl max-w-md w-full max-h-[90vh] overflow-y-auto">
        <div className="sticky top-0 bg-gradient-to-r from-primary to-secondary text-white p-6 rounded-t-2xl flex justify-between items-start">
          <div>
            <h2 className="text-2xl font-bold mb-1">Réservation</h2>
            <p className="text-white/90 text-sm">{parking.name} - Place {spot.spotNumber}</p>
          </div>
          <button onClick={onClose} disabled={loading} className="bg-white/20 hover:bg-white/30 rounded-full p-2 transition"><X className="w-6 h-6" /></button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          {apiError && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative">
              {apiError}
            </div>
          )}

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2"><User className="w-4 h-4 inline mr-2" /> Nom complet *</label>
            <input type="text" name="userName" value={formData.userName} onChange={handleChange} placeholder="Ex: Ahmed Bennani" className="w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-primary" required />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2"><Mail className="w-4 h-4 inline mr-2" /> Email *</label>
            <input type="email" name="userEmail" value={formData.userEmail} onChange={handleChange} placeholder="ahmed@example.com" className="w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-primary" required />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2"><Phone className="w-4 h-4 inline mr-2" /> Téléphone</label>
            <input type="tel" name="userPhone" value={formData.userPhone} onChange={handleChange} placeholder="0612345678" className="w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-primary" />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2"><Car className="w-4 h-4 inline mr-2" /> Immatriculation *</label>
            <input type="text" name="licensePlate" value={formData.licensePlate} onChange={handleChange} placeholder="12345-A-67" className="w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-primary" required />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2"><Calendar className="w-4 h-4 inline mr-2" /> Date début *</label>
            <input type="datetime-local" name="startTime" value={formData.startTime} onChange={handleChange} min={getCurrentDateTime()} className="w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-primary" required />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2"><Clock className="w-4 h-4 inline mr-2" /> Durée *</label>
            <select name="duration" value={formData.duration} onChange={handleChange} className="w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-primary">
              {DURATION_OPTIONS.map(opt => <option key={opt.value} value={opt.value}>{opt.label}</option>)}
            </select>
          </div>

          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 flex justify-between items-center">
            <span className="text-gray-700 font-semibold">Prix estimé :</span>
            <span className="text-2xl font-bold text-primary">{formData.duration * 10} DH</span>
          </div>

          <div className="flex gap-3 pt-4">
            <button type="button" onClick={onClose} disabled={loading} className="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold py-3 px-6 rounded-lg transition">Annuler</button>
            <button type="submit" disabled={loading} className={`flex-1 font-semibold py-3 px-6 rounded-lg transition ${loading ? 'bg-gray-400 cursor-not-allowed' : 'bg-primary hover:bg-secondary text-white'}`}>
              {loading ? 'Traitement...' : 'Confirmer'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ReservationModal;