import React, { useState, useEffect } from 'react';
import { X, MapPin, Car, Clock } from 'lucide-react';
import { getParkingSpots } from '../services/apiService';
import LoadingSpinner from './LoadingSpinner';

const ParkingDetails = ({ parking, onClose, onReserve }) => {
  const [spots, setSpots] = useState([]);
  const [selectedSpot, setSelectedSpot] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSpots = async () => {
      try {
        setLoading(true);
        console.log("Fetching spots for parking ID:", parking.id); // Debug
        
        const data = await getParkingSpots(parking.id);
        
        console.log("📦 Spots Data received:", data); 

        // Ila kan data array direct:
        if (Array.isArray(data)) {
            setSpots(data);
        } 
        // Ila kan Spring Data REST (content/pageable):
        else if (data.content) {
            setSpots(data.content);
        } else {
            console.error("Format de données inconnu", data);
            setSpots([]);
        }

      } catch (error) {
        console.error('Erreur loading spots:', error);
      } finally {
        setLoading(false);
      }
    };
    if (parking?.id) fetchSpots();
  }, [parking]);

  const getSpotColor = (status) => {
    // T2aked ana status majaych null wla undefined
    const normalizedStatus = status ? status.toUpperCase() : '';

    switch (normalizedStatus) {
      case 'AVAILABLE': return 'bg-green-500 hover:bg-green-600 cursor-pointer';
      case 'OCCUPIED': return 'bg-red-500 cursor-not-allowed';
      case 'RESERVED': return 'bg-yellow-500 cursor-not-allowed';
      case 'MAINTENANCE': return 'bg-gray-500 cursor-not-allowed'; // <-- Zdna hadi
      default: return 'bg-gray-300';
    }
  };

  const handleSpotClick = (spot) => {
    if (spot.status === 'AVAILABLE') {
      setSelectedSpot(spot);
    }
  };

  const handleReserve = () => {
    if (selectedSpot) onReserve(selectedSpot);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4 animate-fade-in">
      <div className="bg-white rounded-2xl shadow-2xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
        {/* Header - Kima kan... */}
        <div className="sticky top-0 bg-gradient-to-r from-primary to-secondary text-white p-6 rounded-t-2xl flex justify-between items-start z-10">
          <div>
            <h2 className="text-2xl font-bold mb-2">{parking.name}</h2>
            <div className="flex items-center gap-2 text-white/90"><MapPin className="w-4 h-4" /><p className="text-sm">{parking.address}</p></div>
          </div>
          <button onClick={onClose} className="bg-white/20 hover:bg-white/30 rounded-full p-2 transition"><X className="w-6 h-6" /></button>
        </div>

        {/* Body */}
        <div className="p-6">
          {/* Stats Section - Kima kan... */}
           <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
             {/* ... Code dyal stats ... */}
             {/* (Khalih kima howa) */}
           </div>

           {/* Légende */}
          <div className="flex flex-wrap gap-4 mb-6 p-4 bg-gray-50 rounded-lg">
            <div className="flex items-center gap-2"><div className="w-4 h-4 bg-green-500 rounded"></div><span className="text-sm">Disponible</span></div>
            <div className="flex items-center gap-2"><div className="w-4 h-4 bg-red-500 rounded"></div><span className="text-sm">Occupée</span></div>
            <div className="flex items-center gap-2"><div className="w-4 h-4 bg-yellow-500 rounded"></div><span className="text-sm">Réservée</span></div>
            <div className="flex items-center gap-2"><div className="w-4 h-4 bg-gray-500 rounded"></div><span className="text-sm">Maintenance</span></div>
          </div>

          {loading ? (
            <div className="py-12"><LoadingSpinner size="lg" /></div>
          ) : (
            <div className="grid grid-cols-5 gap-3 mb-6">
              {spots.length > 0 ? spots.map((spot) => (
                <button
                  key={spot.id}
                  onClick={() => handleSpotClick(spot)}
                  disabled={spot.status !== 'AVAILABLE'}
                  // Hna zedt check 3la spot properties
                  className={`${getSpotColor(spot.status)} ${selectedSpot?.id === spot.id ? 'ring-4 ring-blue-500 ring-offset-2' : ''} aspect-square rounded-lg flex flex-col items-center justify-center text-white font-bold transition-all transform hover:scale-105`}
                >
                  <Car className="w-6 h-6 mb-1" />
                  {/* HNA FIN KAYN LMOCHKIL GHALIBAN: Check wach spotNumber ola number */}
                  <span className="text-sm">
                    {spot.spotNumber || spot.number || "?"} 
                  </span>
                </button>
              )) : (
                <div className="col-span-5 text-center text-gray-500 py-4">
                  Aucune place trouvée (Vérifiez la console F12)
                </div>
              )}
            </div>
          )}

          {/* Footer Buttons */}
          <div className="flex gap-4 pt-4 border-t">
            <button onClick={onClose} className="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold py-3 px-6 rounded-lg transition">Annuler</button>
            <button onClick={handleReserve} disabled={!selectedSpot} className={`flex-1 font-semibold py-3 px-6 rounded-lg transition ${selectedSpot ? 'bg-primary hover:bg-secondary text-white' : 'bg-gray-300 text-gray-500 cursor-not-allowed'}`}>Réserver cette place</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ParkingDetails;