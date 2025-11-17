import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Menu, X, Car } from 'lucide-react';
import LoginModal from './LoginModal';
import UserMenu from './UserMenu';

const Navbar = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  // Vérifier si l'utilisateur est connecté
  useEffect(() => {
    const userEmail = localStorage.getItem('userEmail');
    setIsLoggedIn(!!userEmail);
  }, []);

  const handleLogout = () => {
    setIsLoggedIn(false);
    // Rediriger vers l'accueil
    window.location.href = '/';
  };

  const handleLoginSuccess = () => {
    setShowLoginModal(false);
    setIsLoggedIn(true);
  };

  return (
    <>
      <nav className="sticky top-0 z-40 bg-gradient-to-r from-primary to-secondary backdrop-blur-lg shadow-lg">
        <div className="container mx-auto px-4">
          <div className="flex justify-between items-center h-16">
            {/* Logo */}
            <Link to="/" className="flex items-center gap-2 text-white font-bold text-xl">
              <Car className="w-6 h-6" />
              <span>Smart Parking</span>
            </Link>

            {/* Desktop Menu */}
            <div className="hidden md:flex items-center gap-6">
              <Link to="/" className="text-white hover:text-gray-200 transition">
                Accueil
              </Link>
              <Link to="/reservations" className="text-white hover:text-gray-200 transition">
                Mes Réservations
              </Link>
              <Link to="/admin" className="text-white hover:text-gray-200 transition">
                Admin
              </Link>
              
              {/* Bouton Connexion ou Menu User */}
              {isLoggedIn ? (
                <UserMenu onLogout={handleLogout} />
              ) : (
                <button 
                  onClick={() => setShowLoginModal(true)}
                  className="bg-white text-primary px-6 py-2 rounded-full font-semibold hover:bg-gray-100 transition"
                >
                  Se connecter
                </button>
              )}
            </div>

            {/* Mobile Menu Button */}
            <button
              onClick={() => setIsOpen(!isOpen)}
              className="md:hidden text-white"
            >
              {isOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
            </button>
          </div>

          {/* Mobile Menu */}
          {isOpen && (
            <div className="md:hidden py-4 space-y-3">
              <Link
                to="/"
                className="block text-white hover:text-gray-200 transition py-2"
                onClick={() => setIsOpen(false)}
              >
                Accueil
              </Link>
              <Link
                to="/reservations"
                className="block text-white hover:text-gray-200 transition py-2"
                onClick={() => setIsOpen(false)}
              >
                Mes Réservations
              </Link>
              <Link
                to="/admin"
                className="block text-white hover:text-gray-200 transition py-2"
                onClick={() => setIsOpen(false)}
              >
                Admin
              </Link>
              
              {isLoggedIn ? (
                <button 
                  onClick={handleLogout}
                  className="w-full bg-white text-primary px-6 py-2 rounded-full font-semibold"
                >
                  Se déconnecter
                </button>
              ) : (
                <button 
                  onClick={() => {
                    setShowLoginModal(true);
                    setIsOpen(false);
                  }}
                  className="w-full bg-white text-primary px-6 py-2 rounded-full font-semibold"
                >
                  Se connecter
                </button>
              )}
            </div>
          )}
        </div>
      </nav>

      {/* Login Modal */}
      <LoginModal 
        isOpen={showLoginModal} 
        onClose={() => setShowLoginModal(false)}
        onSuccess={handleLoginSuccess}
      />
    </>
  );
};

export default Navbar;