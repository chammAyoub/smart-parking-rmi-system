import React from 'react';
import ParkingCard from './ParkingCard';
import LoadingSpinner from './LoadingSpinner';

const ParkingGrid = ({ parkings, onParkingSelect, loading }) => {
  if (loading) {
    return (
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {[...Array(6)].map((_, index) => (
          <div key={index} className="bg-white rounded-lg shadow-lg p-6 h-64 flex items-center justify-center">
            <LoadingSpinner size="lg" />
          </div>
        ))}
      </div>
    );
  }

  if (!parkings || parkings.length === 0) {
    return (
      <div className="text-center py-12">
        <div className="text-6xl mb-4">🚗</div>
        <h3 className="text-2xl font-bold text-gray-800 mb-2">Aucun parking disponible</h3>
        <p className="text-gray-600">Essayez de modifier vos critères de recherche</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 animate-fade-in">
      {parkings.map((parking) => (
        <ParkingCard
          key={parking.id}
          parking={parking}
          onSelect={onParkingSelect}
        />
      ))}
    </div>
  );
};

export default ParkingGrid;