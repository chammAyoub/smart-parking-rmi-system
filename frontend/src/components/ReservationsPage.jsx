import React, { useState, useEffect } from 'react';
import { Calendar, Clock, Car, Trash2, AlertCircle } from 'lucide-react';
import { getUserReservations, cancelReservation } from '../services/apiService';
import { useNavigate } from 'react-router-dom';
import LoadingSpinner from './LoadingSpinner';
import Toast from './Toast';

const ReservationsPage = () => {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [toast, setToast] = useState(null);
  
  const navigate = useNavigate();
  const userEmail = localStorage.getItem('userEmail');

  useEffect(() => {
    if (!userEmail) {
        setLoading(false);
        return;
    }
    fetchReservations();
  }, [userEmail]);

  const fetchReservations = async () => {
    try {
      setLoading(true);
      const data = await getUserReservations(userEmail);
      // Tri par date décroissante (optionnel mais recommandé)
      const sortedData = data.sort((a, b) => new Date(b.startTime) - new Date(a.startTime));
      setReservations(sortedData);
    } catch (error) {
      console.error('Erreur historique:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (id) => {
    if (!window.confirm("Voulez-vous vraiment annuler cette réservation ?")) return;
    try {
      await cancelReservation(id);
      setToast({ message: "Réservation annulée avec succès.", type: "success" });
      fetchReservations(); // Refresh list
    } catch (error) {
      console.error(error);
      setToast({ message: "Impossible d'annuler cette réservation.", type: "error" });
    }
  };

  if (loading) return <div className="min-h-screen bg-gray-50 flex items-center justify-center"><LoadingSpinner size="lg" /></div>;

  if (!userEmail) {
      return (
        <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
            <div className="bg-white p-8 rounded-2xl shadow-xl text-center max-w-md">
                <AlertCircle className="w-16 h-16 text-yellow-500 mx-auto mb-4" />
                <h2 className="text-2xl font-bold text-gray-800 mb-2">Accès refusé</h2>
                <p className="text-gray-600 mb-6">Vous devez être connecté pour voir vos réservations.</p>
                <button onClick={() => navigate('/')} className="bg-primary text-white px-6 py-3 rounded-lg hover:bg-secondary transition">
                    Retour à l'accueil
                </button>
            </div>
        </div>
      );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="container mx-auto px-4">
        <h1 className="text-3xl font-bold text-gray-800 mb-8">Mes Réservations</h1>

        {reservations.length === 0 ? (
          <div className="bg-white rounded-lg shadow-lg p-12 text-center">
            <div className="text-6xl mb-4">📅</div>
            <h2 className="text-2xl font-bold text-gray-800 mb-2">Aucune réservation</h2>
            <p className="text-gray-600 mb-6">Vous n'avez pas encore de réservation active.</p>
            <a href="/" className="inline-block bg-primary hover:bg-secondary text-white font-semibold py-3 px-8 rounded-lg transition">Réserver maintenant</a>
          </div>
        ) : (
          <div className="grid gap-6">
            {reservations.map((res) => (
              <div key={res.id} className="bg-white rounded-lg shadow-lg p-6 border-l-4 border-primary hover:shadow-xl transition duration-300">
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <h3 className="text-xl font-bold text-gray-800">{res.parkingName}</h3>
                    <p className="text-gray-600">Place <span className="font-bold text-primary">{res.spotNumber}</span></p>
                  </div>
                  <span className={`px-4 py-1 rounded-full text-sm font-semibold 
                    ${res.status === 'CONFIRMED' ? 'bg-green-100 text-green-800' : 
                      res.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' : 
                      res.status === 'CANCELLED' ? 'bg-red-100 text-red-800' : 'bg-gray-100 text-gray-800'}`}>
                    {res.status}
                  </span>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                  <div className="flex items-center gap-2 text-gray-600">
                    <Calendar className="w-5 h-5 text-primary" /> 
                    <span>{new Date(res.startTime).toLocaleString()}</span>
                  </div>
                  <div className="flex items-center gap-2 text-gray-600">
                    <Clock className="w-5 h-5 text-primary" /> 
                    <span>{res.durationHours}h ({res.totalAmount} DH)</span>
                  </div>
                  <div className="flex items-center gap-2 text-gray-600">
                    <Car className="w-5 h-5 text-primary" /> 
                    <span>{res.licensePlate}</span>
                  </div>
                </div>

                {/* ✅ CORRECTION: AFFICHER BOUTON SI CONFIRMED OU PENDING */}
                {(res.status === 'CONFIRMED' || res.status === 'PENDING') && (
                  <div className="mt-4 pt-4 border-t flex justify-end">
                    <button 
                        onClick={() => handleCancel(res.id)} 
                        className="bg-red-50 hover:bg-red-100 text-red-600 border border-red-200 font-semibold py-2 px-4 rounded-lg transition flex items-center gap-2"
                    >
                      <Trash2 className="w-4 h-4" /> Annuler la réservation
                    </button>
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
      {toast && <Toast message={toast.message} type={toast.type} onClose={() => setToast(null)} />}
    </div>
  );
};

export default ReservationsPage;