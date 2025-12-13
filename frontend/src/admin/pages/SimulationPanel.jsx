import React, { useState, useEffect } from "react";
import {
  getAllParkingLots,
  getParkingSpots,
  getParkingById,
} from "../../services/apiService";
import { simulateCarEnter, simulateCarExit } from "../services/adminService";
import { Car } from "lucide-react";
import ParkingGridAdmin from "../components/ParkingGridAdmin";

const SimulationPanel = () => {
  const [parkings, setParkings] = useState([]);
  const [selectedParking, setSelectedParking] = useState(null);
  const [spots, setSpots] = useState([]);

  useEffect(() => {
    getAllParkingLots().then(setParkings);
  }, []);

  useEffect(() => {
    if (selectedParking) {
      const fetchSpots = async () => {
        const parkingData = await getParkingById(selectedParking);
        setSpots(parkingData.spots);
        console.log(parkingData);
      };

      fetchSpots();
    }
  }, [selectedParking]);

  const handleAction = async (spot, action) => {
    try {
      if (action === "enter") await simulateCarEnter(spot.id);
      else await simulateCarExit(spot.id);
      const updatedData = await getParkingSpots(selectedParking);
      setSpots(updatedData);
    } catch (err) {
      alert("Erreur simulation: " + err.message);
    }
  };

  return (
    <div
      className="bg-white rounded-2xl shadow-lg p-6 border 
      border-gray-100"
    >
      <h2
        className="text-xl font-bold text-gray-800 mb-6 flex 
        items-center gap-2"
      >
        <Car className="text-primary" /> Simulateur IoT
      </h2>

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
