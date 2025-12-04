import React, { useState, useEffect } from 'react';
import { Calendar, Clock, Car, Trash2 } from 'lucide-react';
import { getUserReservations, cancelReservation } from '../services/apiService';
import LoadingSpinner from './LoadingSpinner';
import Toast from './Toast';

const ReservationsPage = () => {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [toast, setToast] = useState(null);
  
  // Simulation: On utilise un email fixe pour l'instant (à remplacer par login)
  const userEmail = "ahmed@example.com"; 

  useEffect(() => {
    fetchReservations();
  }, []);

  const fetchReservations = async () => {
    try {
      setLoading(true);
      const data = await getUserReservations(userEmail);
      setReservations(data);
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
      setToast({ message: "Réservation annulée.", type: "success" });
      fetchReservations();
    } catch (error) {
      setToast({ message: "Erreur lors de l'annulation.", type: "error" });
    }
  };

  if (loading) return <div className="min-h-screen bg-gray-50 flex items-center justify-center"><LoadingSpinner size="lg" /></div>;

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
              <div key={res.id} className="bg-white rounded-lg shadow-lg p-6 border-l-4 border-primary">
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <h3 className="text-xl font-bold text-gray-800">{res.parkingName}</h3>
                    <p className="text-gray-600">Place <span className="font-bold">{res.spotNumber}</span></p>
                  </div>
                  <span className={`px-4 py-1 rounded-full text-sm font-semibold ${res.status === 'CONFIRMED' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                    {res.status}
                  </span>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                  <div className="flex items-center gap-2 text-gray-600"><Calendar className="w-5 h-5" /> <span>{new Date(res.startTime).toLocaleString()}</span></div>
                  <div className="flex items-center gap-2 text-gray-600"><Clock className="w-5 h-5" /> <span>{res.durationHours}h ({res.totalAmount} DH)</span></div>
                  <div className="flex items-center gap-2 text-gray-600"><Car className="w-5 h-5" /> <span>{res.licensePlate}</span></div>
                </div>

                {res.status === 'CONFIRMED' && (
                  <div className="mt-4 pt-4 border-t flex justify-end">
                    <button onClick={() => handleCancel(res.id)} className="bg-red-100 hover:bg-red-200 text-red-700 font-semibold py-2 px-4 rounded-lg transition flex items-center gap-2">
                      <Trash2 className="w-4 h-4" /> Annuler
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