import React from 'react';
import { MapPin, Car } from 'lucide-react';

const ParkingCard = ({ parking, onSelect }) => {
  const availabilityPercentage = (parking.availableSpots / parking.totalSpots) * 100;
  
  const getAvailabilityColor = () => {
    if (availabilityPercentage > 50) return 'bg-green-500';
    if (availabilityPercentage > 20) return 'bg-yellow-500';
    return 'bg-red-500';
  };

  const getAvailabilityText = () => {
    if (availabilityPercentage > 50) return 'Bonne disponibilité';
    if (availabilityPercentage > 20) return 'Disponibilité moyenne';
    return 'Peu de places';
  };

  return (
    <div className="bg-white rounded-lg shadow-lg hover:shadow-xl transition-all duration-300 overflow-hidden cursor-pointer transform hover:-translate-y-1">
      <div className="p-6">
        {/* Header */}
        <div className="flex items-start justify-between mb-4">
          <h3 className="text-xl font-bold text-gray-800">{parking.name}</h3>
          <span className={`${getAvailabilityColor()} text-white text-xs px-3 py-1 rounded-full`}>
            {getAvailabilityText()}
          </span>
        </div>

        {/* Address */}
        <div className="flex items-start gap-2 text-gray-600 mb-4">
          <MapPin className="w-5 h-5 mt-0.5 flex-shrink-0" />
          <p className="text-sm">{parking.address}</p>
        </div>

        {/* Availability */}
        <div className="flex items-center gap-2 mb-4">
          <Car className="w-5 h-5 text-primary" />
          <span className="text-lg font-semibold text-gray-800">
            {parking.availableSpots}/{parking.totalSpots}
          </span>
          <span className="text-gray-600 text-sm">places disponibles</span>
        </div>

        {/* Progress Bar */}
        <div className="w-full bg-gray-200 rounded-full h-2 mb-4">
          <div
            className={`${getAvailabilityColor()} h-2 rounded-full transition-all duration-300`}
            style={{ width: `${availabilityPercentage}%` }}
          />
        </div>

        {/* Distance (placeholder) */}
        <div className="text-sm text-gray-500 mb-4">
          📍 Distance : 2.5 km
        </div>

        {/* Button */}
        <button
          onClick={() => onSelect(parking)}
          className="w-full bg-primary hover:bg-secondary text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-300"
        >
          Voir les places disponibles
        </button>
      </div>
    </div>
  );
};

export default ParkingCard;