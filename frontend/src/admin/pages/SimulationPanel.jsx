import React, { useState, useEffect } from "react";
import { getAllParkingLots, getParkingSpots } from "../../services/apiService";
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

  // useEffect(() => {
  //   setParkings([
  //     { id: 1, name: "Agdal", totalSpots: 20 },
  //     { id: 2, name: "Hassan", totalSpots: 20 },
  //     { id: 3, name: "Océan", totalSpots: 20 },
  //   ]);
  // }, []);

  useEffect(() => {
    if (selectedParking) {
      const fetchSpots = () => getParkingSpots(selectedParking).then(setSpots);
      fetchSpots();
      const interval = setInterval(fetchSpots, 2000);
      return () => clearInterval(interval);
    }
  }, [selectedParking]);

  // useEffect(() => {
  //   if (selectedParking) {
  //     setSpots([
  //       { id: 1, spotNumber: "A1", status: "available" },
  //       { id: 2, spotNumber: "A2", status: "occupied" },
  //       { id: 3, spotNumber: "A3", status: "reserved" },
  //       { id: 4, spotNumber: "A4", status: "available" },
  //       { id: 5, spotNumber: "A5", status: "occupied" },
  //       { id: 6, spotNumber: "B1", status: "available" },
  //       { id: 7, spotNumber: "B2", status: "occupied" },
  //       { id: 8, spotNumber: "B3", status: "available" },
  //       { id: 9, spotNumber: "B4", status: "reserved" },
  //       { id: 10, spotNumber: "B5", status: "occupied" },
  //       { id: 11, spotNumber: "C1", status: "available" },
  //       { id: 12, spotNumber: "C2", status: "occupied" },
  //       { id: 13, spotNumber: "C3", status: "available" },
  //       { id: 14, spotNumber: "C4", status: "reserved" },
  //       { id: 15, spotNumber: "C5", status: "available" },
  //       { id: 16, spotNumber: "D1", status: "occupied" },
  //       { id: 17, spotNumber: "D2", status: "available" },
  //       { id: 18, spotNumber: "D3", status: "occupied" },
  //       { id: 19, spotNumber: "D4", status: "available" },
  //       { id: 20, spotNumber: "D5", status: "reserved" },
  //     ]);
  //   }
  // }, [selectedParking]);

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
