import React, { useState, useEffect } from 'react';
import { X, MapPin, Car, Clock } from 'lucide-react';
import { getParkingSpots } from '../services/apiService';
import { mockSpots } from '../services/mockData';
import LoadingSpinner from './LoadingSpinner';

const ParkingDetails = ({ parking, onClose, onReserve }) => {
  const [spots, setSpots] = useState([]);
  const [selectedSpot, setSelectedSpot] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSpots = async () => {
      try {
        setLoading(true);
        // recuperation depuis api
        // const data = await getParkingSpots(parking.id);
        // setSpots(data);
        
        // utiliser mock data
        setTimeout(() => {
          setSpots(mockSpots);
          setLoading(false);
        }, 500);
      } catch (error) {
        console.error('Erreur lors du chargement des places:', error);
        setSpots(mockSpots);
        setLoading(false);
      }
    };

    fetchSpots();
  }, [parking.id]);

  const getSpotColor = (status) => {
    switch (status) {
      case 'available':
        return 'bg-green-500 hover:bg-green-600 cursor-pointer';
      case 'occupied':
        return 'bg-red-500 cursor-not-allowed';
      case 'reserved':
        return 'bg-yellow-500 cursor-not-allowed';
      default:
        return 'bg-gray-300';
    }
  };

  const getSpotBorder = (spot) => {
    return selectedSpot?.id === spot.id ? 'border-4 border-blue-600' : 'border-2 border-gray-300';
  };

  const handleSpotClick = (spot) => {
    if (spot.status === 'available') {
      setSelectedSpot(spot);
    }
  };

  const handleReserve = () => {
    if (selectedSpot) {
      onReserve(selectedSpot);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4">
      <div className="bg-white rounded-2xl shadow-2xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="sticky top-0 bg-gradient-to-r from-primary to-secondary text-white p-6 rounded-t-2xl">
          <div className="flex justify-between items-start">
            <div>
              <h2 className="text-2xl font-bold mb-2">{parking.name}</h2>
              <div className="flex items-center gap-2 text-white/90">
                <MapPin className="w-4 h-4" />
                <p className="text-sm">{parking.address}</p>
              </div>
            </div>
            <button
              onClick={onClose}
              className="bg-white/20 hover:bg-white/30 rounded-full p-2 transition"
            >
              <X className="w-6 h-6" />
            </button>
          </div>
        </div>

        <div className="p-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
            <div className="bg-green-50 border border-green-200 rounded-lg p-4">
              <div className="flex items-center gap-3">
                <div className="bg-green-500 rounded-full p-2">
                  <Car className="w-5 h-5 text-white" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Disponibles</p>
                  <p className="text-2xl font-bold text-green-600">{parking.availableSpots}</p>
                </div>
              </div>
            </div>

            <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
              <div className="flex items-center gap-3">
                <div className="bg-blue-500 rounded-full p-2">
                  <Car className="w-5 h-5 text-white" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Total</p>
                  <p className="text-2xl font-bold text-blue-600">{parking.totalSpots}</p>
                </div>
              </div>
            </div>

            <div className="bg-purple-50 border border-purple-200 rounded-lg p-4">
              <div className="flex items-center gap-3">
                <div className="bg-purple-500 rounded-full p-2">
                  <Clock className="w-5 h-5 text-white" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Tarif/heure</p>
                  <p className="text-2xl font-bold text-purple-600">10 DH</p>
                </div>
              </div>
            </div>
          </div>

        
          <div className="flex flex-wrap gap-4 mb-6 p-4 bg-gray-50 rounded-lg">
            <div className="flex items-center gap-2">
              <div className="w-6 h-6 bg-green-500 rounded border-2 border-gray-300"></div>
              <span className="text-sm">Disponible</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-6 h-6 bg-red-500 rounded border-2 border-gray-300"></div>
              <span className="text-sm">Occupée</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-6 h-6 bg-yellow-500 rounded border-2 border-gray-300"></div>
              <span className="text-sm">Réservée</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-6 h-6 bg-green-500 rounded border-4 border-blue-600"></div>
              <span className="text-sm">Sélectionnée</span>
            </div>
          </div>

      
          {loading ? (
            <div className="py-12">
              <LoadingSpinner size="lg" />
            </div>
          ) : (
            <div className="grid grid-cols-5 gap-3 mb-6">
              {spots.map((spot) => (
                <button
                  key={spot.id}
                  onClick={() => handleSpotClick(spot)}
                  disabled={spot.status !== 'available'}
                  className={`
                    ${getSpotColor(spot.status)}
                    ${getSpotBorder(spot)}
                    aspect-square
                    rounded-lg
                    flex flex-col items-center justify-center
                    text-white font-bold
                    transition-all duration-200
                    transform hover:scale-105
                  `}
                >
                  <Car className="w-6 h-6 mb-1" />
                  <span className="text-sm">{spot.spotNumber}</span>
                </button>
              ))}
            </div>
          )}

        
          {selectedSpot && (
            <div className="bg-blue-50 border-2 border-blue-500 rounded-lg p-4 mb-4">
              <p className="text-blue-800 font-semibold">
                ✓ Place {selectedSpot.spotNumber} sélectionnée
              </p>
            </div>
          )}

  
          <div className="flex gap-4">
            <button
              onClick={onClose}
              className="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold py-3 px-6 rounded-lg transition"
            >
              Annuler
            </button>
            <button
              onClick={handleReserve}
              disabled={!selectedSpot}
              className={`
                flex-1 font-semibold py-3 px-6 rounded-lg transition
                ${selectedSpot
                  ? 'bg-primary hover:bg-secondary text-white'
                  : 'bg-gray-300 text-gray-500 cursor-not-allowed'
                }
              `}
            >
              Réserver cette place
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ParkingDetails;