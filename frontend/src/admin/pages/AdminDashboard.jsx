import React, { useState, useEffect } from "react";
import { BarChart3, Car, Activity } from "lucide-react";
import AdminStats from "./AdminStats";
import SimulationPanel from "./SimulationPanel";
import { getAdminStats } from "../services/adminService";

const AdminDashboard = () => {
  const role = localStorage.getItem("role");
  if (role !== "ADMIN") {
    window.location.href = "/";
  }
  const [activeTab, setActiveTab] = useState("simulation");
  const [stats, setStats] = useState({
    totalSpots: 0,
    occupiedSpots: 0,
    occupancyRate: 0,
  });

  useEffect(() => {
    const fetchStats = () =>
      getAdminStats().then(setStats).catch(console.error);
    fetchStats();
  }, []);

  // useEffect(() => {
  //   setStats({
  //     totalSpots: 60,
  //     availableSpots: 27,
  //     occupiedSpots: 33,
  //     occupancyRate: 55,
  //   });
  // }, []);

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
            <div className="bg-white p-4 rounded-xl shadow-sm">
              <p className="text-xs text-gray-500">Occupation</p>
              <p className="text-2xl font-bold text-primary">
                {stats.occupancyRate}%
              </p>
            </div>
            <div className="bg-white p-4 rounded-xl shadow-sm">
              <p className="text-xs text-gray-500">Libres</p>
              <p className="text-2xl font-bold text-green-500">
                {stats.totalSpots - stats.occupiedSpots}
              </p>
            </div>
          </div>
        </header>

        <div className="animate-fade-in">
          {activeTab === "simulation" ? <SimulationPanel /> : <AdminStats />}
        </div>
      </main>
    </div>
  );
};

export default AdminDashboard;
