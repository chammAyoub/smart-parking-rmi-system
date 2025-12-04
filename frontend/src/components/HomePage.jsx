import React, { useState, useEffect } from 'react';
import { Map, Grid, TrendingUp } from 'lucide-react';
import SearchBar from './SearchBar';
import ParkingMap from './ParkingMap';
import ParkingGrid from './ParkingGrid';
import ParkingDetails from './ParkingDetails';
import ReservationModal from './ReservationModal';
import Toast from './Toast';
import { getAllParkingLots } from '../services/apiService';
import { MAP_CONFIG } from '../utils/constants';

const HomePage = () => {
  const [parkings, setParkings] = useState([]);
  const [filteredParkings, setFilteredParkings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [view, setView] = useState('grid');
  const [selectedParking, setSelectedParking] = useState(null);
  const [selectedSpot, setSelectedSpot] = useState(null);
  const [showDetails, setShowDetails] = useState(false);
  const [showReservation, setShowReservation] = useState(false);
  const [toast, setToast] = useState(null);

  useEffect(() => {
    fetchParkings();
  }, []);

  const fetchParkings = async () => {
    try {
      setLoading(true);
      const data = await getAllParkingLots();
      setParkings(data);
      setFilteredParkings(data);
    } catch (error) {
      console.error('Erreur backend:', error);
      showToast('Impossible de charger les parkings. Vérifiez la connexion.', 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (searchTerm) => {
    if (!searchTerm.trim()) {
      setFilteredParkings(parkings);
      return;
    }
    const filtered = parkings.filter(parking =>
      parking.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      parking.address.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredParkings(filtered);
  };

  const handleParkingSelect = (parking) => {
    setSelectedParking(parking);
    setShowDetails(true);
  };

  const handleSpotReserve = (spot) => {
    setSelectedSpot(spot);
    setShowDetails(false);
    setShowReservation(true);
  };

  const handleReservationSuccess = () => {
    setShowReservation(false);
    setSelectedParking(null);
    setSelectedSpot(null);
    showToast('Réservation confirmée avec succès ! 🎉', 'success');
    fetchParkings(); // Rafraîchir les données
  };

  const showToast = (message, type) => {
    setToast({ message, type });
  };

  const closeToast = () => setToast(null);

  // Calculer les statistiques
  const totalSpots = parkings.reduce((sum, p) => sum + (p.totalSpots || 0), 0);
  const availableSpots = parkings.reduce((sum, p) => sum + (p.availableSpots || 0), 0);
  const occupancyRate = totalSpots > 0 ? (((totalSpots - availableSpots) / totalSpots) * 100).toFixed(0) : 0;

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Section */}
      <div className="bg-gradient-to-r from-primary to-secondary text-white py-16">
        <div className="container mx-auto px-4">
          <div className="max-w-3xl mx-auto text-center">
            <h1 className="text-4xl md:text-5xl font-bold mb-4">🚗 Trouvez Votre Place</h1>
            <p className="text-xl text-white/90 mb-8">Réservez en quelques clics.</p>
            <SearchBar onSearch={handleSearch} />
          </div>
        </div>
      </div>

      {/* Stats Section */}
      <div className="container mx-auto px-4 -mt-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white rounded-lg shadow-lg p-6 flex justify-between items-center">
            <div><p className="text-gray-600 text-sm">Parkings</p><p className="text-3xl font-bold text-primary">{parkings.length}</p></div>
            <div className="bg-primary/10 rounded-full p-3"><Map className="w-8 h-8 text-primary" /></div>
          </div>
          <div className="bg-white rounded-lg shadow-lg p-6 flex justify-between items-center">
            <div><p className="text-gray-600 text-sm">Places libres</p><p className="text-3xl font-bold text-green-600">{availableSpots}</p></div>
            <div className="bg-green-100 rounded-full p-3"><Grid className="w-8 h-8 text-green-600" /></div>
          </div>
          <div className="bg-white rounded-lg shadow-lg p-6 flex justify-between items-center">
            <div><p className="text-gray-600 text-sm">Occupation</p><p className="text-3xl font-bold text-orange-600">{occupancyRate}%</p></div>
            <div className="bg-orange-100 rounded-full p-3"><TrendingUp className="w-8 h-8 text-orange-600" /></div>
          </div>
        </div>
      </div>

      {/* View Toggle */}
      <div className="container mx-auto px-4 mb-6 flex justify-center gap-4">
        <button onClick={() => setView('grid')} className={`flex items-center gap-2 px-6 py-3 rounded-lg font-semibold transition ${view === 'grid' ? 'bg-primary text-white shadow-lg' : 'bg-white text-gray-700'}`}><Grid className="w-5 h-5" /> Grille</button>
        <button onClick={() => setView('map')} className={`flex items-center gap-2 px-6 py-3 rounded-lg font-semibold transition ${view === 'map' ? 'bg-primary text-white shadow-lg' : 'bg-white text-gray-700'}`}><Map className="w-5 h-5" /> Carte</button>
      </div>

      {/* Content */}
      <div className="container mx-auto px-4 pb-12">
        {view === 'grid' ? (
          <ParkingGrid parkings={filteredParkings} onParkingSelect={handleParkingSelect} loading={loading} />
        ) : (
          <ParkingMap parkings={filteredParkings} center={MAP_CONFIG.center} zoom={MAP_CONFIG.zoom} onMarkerClick={handleParkingSelect} />
        )}
      </div>

      {/* Modals */}
      {showDetails && selectedParking && (
        <ParkingDetails parking={selectedParking} onClose={() => { setShowDetails(false); setSelectedParking(null); }} onReserve={handleSpotReserve} />
      )}

      {showReservation && selectedParking && selectedSpot && (
        <ReservationModal isOpen={showReservation} parking={selectedParking} spot={selectedSpot} onClose={() => { setShowReservation(false); setSelectedSpot(null); }} onSuccess={handleReservationSuccess} />
      )}

      {/* Toast */}
      {toast && <Toast message={toast.message} type={toast.type} onClose={closeToast} />}
    </div>
  );
};

export default HomePage;