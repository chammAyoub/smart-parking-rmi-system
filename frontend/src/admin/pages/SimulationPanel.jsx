import React, { useState, useEffect } from "react";
import {
  getAllParkingLots,
  getParkingSpots,
  getParkingById,
} from "../../services/apiService";
import { simulateCarEnter, simulateCarExit, parkingStats } from "../../services/apiService";
import { Car } from "lucide-react";
import ParkingGridAdmin from "../components/ParkingGridAdmin";

const SimulationPanel = () => {
  const [parkings, setParkings] = useState([]);
  const [selectedParking, setSelectedParking] = useState(null);
  const [spots, setSpots] = useState([]);
  const [stats, setStats] = useState({
    availableSpots: 0,
    occupiedSpots: 0,
    occupancyRate: 0,
  });

  useEffect(() => {
    getAllParkingLots().then(setParkings);
  }, []);

  useEffect(() => {
    if (selectedParking) {
      const fetchSpots = async () => {
        const parkingData = await getParkingById(selectedParking);
        const parkingStatsData = await parkingStats(selectedParking);
        setSpots(parkingData.spots);
        setStats(parkingStatsData)
      };

      fetchSpots();
    }
    if(setSelectedParking == null) {
      setStats({availableSpots: 0,
      occupiedSpots: 0,
      occupancyRate: 0,}) 
    }
  }, [selectedParking]);



  const handleAction = async (spot, action) => {
    const spotId = spot.id;
    console.log(spotId);
    
    
    try {
      if (action === "enter") await simulateCarEnter(spotId);
      else await simulateCarExit(spotId);
      const updatedData = await getParkingSpots(selectedParking);
      setSpots(updatedData);
      
    } catch (err) {
      alert("Erreur simulation: " + err.message);
    };
    }

  return (
    <div
      className="bg-white rounded-2xl shadow-lg p-6 border 
      border-gray-100"
    >
      <div className="flex justify-between items-center mb-6">
        <h2
        className="text-xl font-bold text-gray-800 flex 
        items-center gap-2"
      >
        <Car className="text-primary" /> Simulateur IoT
      </h2>
      <div className="flex gap-4">  
        <div className="bg-white p-4 rounded-xl shadow-sm">
          <p className="text-xs text-gray-500">Occupation</p>
          <p className="text-2xl font-bold text-primary">
            {stats.occupancyRate.toFixed(2)}%
          </p>
        </div>
        <div className="bg-white p-4 rounded-xl shadow-sm">
          <p className="text-xs text-gray-500">Libres</p>
          <p className="text-2xl font-bold text-green-500">
            {stats.availableSpots}
          </p>
        </div>
        <div className="bg-white p-4 rounded-xl shadow-sm">
          <p className="text-xs text-gray-500">Ocupied</p>
          <p className="text-2xl font-bold text-green-500">
            {stats.occupiedSpots}
          </p>
        </div>
      </div>
      
      </div>
      

      <div className="mb-8">
        <label className="block text-sm font-medium text-gray-700 mb-2">
          Sélectionner un Parking :
        </label>
        <select
          className="w-full md:w-1/2 p-3 border rounded-lg outline-none 
            focus:ring-2 focus:ring-primary"
          onChange={(e) => setSelectedParking(e.target.value)}
        >
          <option value="">-- Choisir --</option>
          {parkings.map((p) => (
            <option key={p.id} value={p.id}>
              {p.name}
            </option>
          ))}
        </select>
      </div>

      {selectedParking ? (
        <ParkingGridAdmin spots={spots} onAction={handleAction} />
      ) : (
        <div className="text-center py-10 text-gray-400">
          Veuillez sélectionner un parking.
        </div>
      )}
    </div>
  );
};

export default SimulationPanel;