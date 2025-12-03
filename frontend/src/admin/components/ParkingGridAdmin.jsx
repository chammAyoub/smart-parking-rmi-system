                        
import React from 'react';
import { LogIn, LogOut } from 'lucide-react';
import '../styles/AdminPanel.css';

const ParkingGridAdmin = ({ spots, onAction }) => {
  return (
    <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-4">
      {spots.map(spot => (
        <div key={spot.id} className={`parking-spot-card relative p-4 
            rounded-xl border-2 flex flex-col items-center justify-center 
            gap-2 h-32 
            ${spot.status === 'available' ? 
              'status-available border-green-100 bg-green-50' : ''}
            ${spot.status === 'occupied' ? 
              'status-occupied border-red-100 bg-red-50' : ''}
            ${spot.status === 'reserved' ? 
              'status-reserved border-yellow-100 bg-yellow-50' : ''}
          `}>
          
          <span className="font-bold text-gray-700 text-lg">
            {spot.spotNumber}
          </span>
          
          <span className={`text-xs px-2 py-1 rounded-full font-semibold 
            ${spot.status === 'available' ? 
              'bg-green-200 text-green-800' : ''}
            ${spot.status === 'occupied' ? 
              'bg-red-200 text-red-800' : ''}
            ${spot.status === 'reserved' ? 
              'bg-yellow-200 text-yellow-800' : ''}`}>
            {spot.status.toUpperCase()}
          </span>

          <div className="flex gap-2 mt-1">
            {spot.status === 'available' && (
              <button 
                onClick={() => onAction(spot, 'enter')} 
                className="p-2 bg-red-100 text-red-600 rounded-full 
                  hover:bg-red-200 hover:scale-110 transition-transform" 
                title="Entrée">
                <LogIn size={16} />
              </button>
            )}
            {(spot.status === 'occupied' || spot.status === 'reserved') && (
              <button 
                onClick={() => onAction(spot, 'exit')} 
                className="p-2 bg-green-100 text-green-600 rounded-full 
                  hover:bg-green-200 hover:scale-110 transition-transform" 
                title="Sortie">
                <LogOut size={16} />
              </button>
            )}
          </div>
        </div>
      ))}
    </div>
  );
};

export default ParkingGridAdmin;