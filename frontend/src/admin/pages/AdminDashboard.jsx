import React, { useState, useEffect } from "react";
import { BarChart3, Car, Activity, X, Plus } from "lucide-react";
import AdminStats from "./AdminStats";
import SimulationPanel from "./SimulationPanel";
import { getAdminStats } from "../services/adminService";
import {addParking} from "../../services/apiService"

const CreateParkingModal = ({ isOpen, onClose, onSuccess }) => {
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    address: '',
    city: '',
    latitude: '',
    longitude: '',
    totalSpots: '',
    hourlyRate: '',
    openingTime: '08:00',
    closingTime: '23:59',
    rmiHost: 'localhost',
    rmiPort: '1099',
    rmiServiceName: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
    if (!formData.name || !formData.address || !formData.city || !formData.latitude || 
        !formData.longitude || !formData.totalSpots || !formData.hourlyRate) {
      alert('⚠️ Please fill all required fields');
      return;
    }

    setLoading(true);

    try {
      const payload = {
        name: formData.name,
        address: formData.address,
        city: formData.city,
        latitude: parseFloat(formData.latitude),
        longitude: parseFloat(formData.longitude),
        totalSpots: parseInt(formData.totalSpots),
        hourlyRate: parseFloat(formData.hourlyRate),
        openingTime: formData.openingTime,
        closingTime: formData.closingTime,
        rmiHost: formData.rmiHost,
        rmiPort: parseInt(formData.rmiPort),
        rmiServiceName: formData.rmiServiceName
      };
      
      

      const response = await addParking(payload)
      

      if (!response.ok) {
        alert('✅ Parking lot created successfully!');
        setFormData({
          name: '', address: '', city: '', latitude: '', longitude: '',
          totalSpots: '', hourlyRate: '', openingTime: '08:00',
          closingTime: '23:59'
        });
        onClose();
        if (onSuccess) onSuccess();
      } else {
        alert('❌ Failed to create parking lot');
      }
    } catch (err) {
      console.error('Error:', err);
      alert('❌ Error creating parking lot');
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-xl shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between p-6 border-b sticky top-0 bg-white">
          <h2 className="text-2xl font-bold text-gray-800">Create New Parking Lot</h2>
          <button onClick={onClose} className="text-gray-500 hover:text-gray-700 transition">
            <X size={24} />
          </button>
        </div>

        <div className="p-6 space-y-4">
          <div className="space-y-4">
            <h3 className="font-semibold text-lg text-gray-700 border-b pb-2">Basic Information</h3>
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Parking Lot Name *
              </label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Downtown Parking"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Address *</label>
              <input
                type="text"
                name="address"
                value={formData.address}
                onChange={handleChange}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="123 Main Street"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">City *</label>
              <input
                type="text"
                name="city"
                value={formData.city}
                onChange={handleChange}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Casablanca"
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Latitude *</label>
                <input
                  type="number"
                  step="any"
                  name="latitude"
                  value={formData.latitude}
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="33.5731"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Longitude *</label>
                <input
                  type="number"
                  step="any"
                  name="longitude"
                  value={formData.longitude}
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="-7.5898"
                />
              </div>
            </div>
          </div>

          <div className="space-y-4">
            <h3 className="font-semibold text-lg text-gray-700 border-b pb-2">Parking Details</h3>
            
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Total Spots *</label>
                <input
                  type="number"
                  name="totalSpots"
                  value={formData.totalSpots}
                  onChange={handleChange}
                  min="1"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="50"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Hourly Rate (DH) *</label>
                <input
                  type="number"
                  step="0.01"
                  name="hourlyRate"
                  value={formData.hourlyRate}
                  onChange={handleChange}
                  min="0"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="10.00"
                />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Opening Time *</label>
                <input
                  type="time"
                  name="openingTime"
                  value={formData.openingTime}
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Closing Time *</label>
                <input
                  type="time"
                  name="closingTime"
                  value={formData.closingTime}
                  onChange={handleChange}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
            </div>
          </div>

          

          <div className="flex justify-end gap-3 pt-4 border-t">
            <button
              onClick={onClose}
              className="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition"
              disabled={loading}
            >
              Cancel
            </button>
            <button
              onClick={handleSubmit}
              disabled={loading}
              className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Creating...' : 'Create Parking Lot'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

const AdminDashboard = () => {
  const role = localStorage.getItem("role");
  if (role !== "ADMIN") {
    window.location.href = "/";
  }
  const [activeTab, setActiveTab] = useState("simulation");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [stats, setStats] = useState({
    totalSpots: 0,
    occupiedSpots: 0,
    occupancyRate: 0,
  });

  // useEffect(() => {
  //   const fetchStats = () =>
  //     getAdminStats().then(setStats).catch(console.error);
  //   fetchStats();
  // }, []);

  const handleParkingCreated = () => {
    getAdminStats().then(setStats).catch(console.error);
  };

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Sidebar */}
      <aside
        className="w-64 bg-gradient-to-b from-primary to-secondary 
        text-white hidden md:block shadow-xl"
      >
        <div className="p-6">
          <h2 className="text-2xl font-bold flex items-center gap-2">
            <Activity /> Admin
          </h2>
        </div>
        <nav className="mt-6">
          <button
            onClick={() => setActiveTab("simulation")}
            className={`w-full text-left px-6 py-4 flex gap-3 
              ${
                activeTab === "simulation"
                  ? "bg-white/20 border-r-4"
                  : "hover:bg-white/10"
              }`}
          >
            <Car /> Simulation
          </button>
          <button
            onClick={() => setActiveTab("stats")}
            className={`w-full text-left px-6 py-4 flex gap-3 
              ${
                activeTab === "stats"
                  ? "bg-white/20 border-r-4"
                  : "hover:bg-white/10"
              }`}
          >
            <BarChart3 /> Statistiques
          </button>
        </nav>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-8 overflow-y-auto">
        <header className="mb-8 flex justify-between items-center">
          <h1 className="text-3xl font-bold text-gray-800">Tableau de Bord</h1>
          <div className="flex gap-4">
            <button
              onClick={() => setIsModalOpen(true)}
              className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition flex items-center gap-2 shadow-md font-semibold"
            >
              <Plus size={20} /> Add Parking
            </button>
          </div>
        </header>

        <div className="animate-fade-in">
          {activeTab === "simulation" ? <SimulationPanel /> : <AdminStats />}
        </div>
      </main>

      <CreateParkingModal 
        isOpen={isModalOpen} 
        onClose={() => setIsModalOpen(false)}
        onSuccess={handleParkingCreated}
      />
    </div>
  );
};

export default AdminDashboard;