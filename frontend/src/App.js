import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import HomePage from './components/HomePage';
import ReservationsPage from './components/ReservationsPage';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Navbar />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/reservations" element={<ReservationsPage />} />
          <Route path="/admin" element={
            <div className="container mx-auto px-4 py-8 text-center">
              <h1 className="text-3xl font-bold mb-4">Panel Admin</h1>
              <p className="text-gray-600">Cette section sera développée par Zakariya</p>
            </div>
          } />
        </Routes>
      </div>
    </Router>
  );
}

export default App;