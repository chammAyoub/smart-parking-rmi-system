import React, { useEffect, useState } from "react";
import ParkingCircleChart from "../components/ParkingCircleChart";
import { getAllParkingLots, parkingStats } from "../../services/apiService";

const AdminStats = () => {
  const [parkings, setParkings] = useState([]);
  const [selectedParking, setSelectedParking] = useState(null);

  // Fetch all parkings
  useEffect(() => {
    const fetchParkings = async () => {
      try {
        const data = await getAllParkingLots();
        
        // For each parking, fetch its stats (occupied, available, total)
        const parkingsWithStats = await Promise.all(
          data.map(async (p) => {
            const stats = await parkingStats(p.id);
            return {
              id: p.id,
              name: p.name,
              occupied: stats.occupiedSpots,
              total: stats.availableSpots + stats.occupiedSpots,
            };
          })
        );

        setParkings(parkingsWithStats);
      } catch (err) {
        console.error("Error fetching parkings:", err);
      }
    };

    fetchParkings();
  }, []);


  return (
    <div className="p-6 bg-gray-100 min-h-screen">
      <h3 className="text-lg font-bold text-gray-700 mb-6">
        Statistiques des parkings
      </h3>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {parkings.map((parking) => (
          <ParkingCircleChart
            key={parking.id}
            parking={parking}
            
          />
        ))}
      </div>

      
    </div>
  );
};

export default AdminStats;
