import React, { useState, useEffect, useRef } from 'react';
import { User, LogOut, Settings, Calendar, ChevronDown } from 'lucide-react';

/**
 * Menu utilisateur (dropdown après connexion)
 * 
 * @author Ayoub
 */
const UserMenu = ({ onLogout }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [user, setUser] = useState(null);
  const menuRef = useRef(null);

  useEffect(() => {
    // Récupérer les infos utilisateur du localStorage
    const userName = localStorage.getItem('userName') || 'Utilisateur';
    const userEmail = localStorage.getItem('userEmail') || '';
    
    setUser({ name: userName, email: userEmail });

    // Fermer le menu si on clique ailleurs
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('userName');
    localStorage.removeItem('userEmail');
    setIsOpen(false);
    if (onLogout) onLogout();
  };

  if (!user) return null;

  return (
    <div className="relative" ref={menuRef}>
      {/* Bouton User */}
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="flex items-center gap-2 px-4 py-2 bg-white text-primary rounded-full hover:bg-gray-100 transition"
      >
        <div className="w-8 h-8 bg-gradient-to-br from-primary to-secondary rounded-full flex items-center justify-center text-white font-bold">
          {user.name.charAt(0).toUpperCase()}
        </div>
        <span className="font-semibold hidden sm:block">{user.name}</span>
        <ChevronDown className={`w-4 h-4 transition-transform ${isOpen ? 'rotate-180' : ''}`} />
      </button>

      {/* Dropdown Menu */}
      {isOpen && (
        <div className="absolute right-0 mt-2 w-64 bg-white rounded-lg shadow-xl border border-gray-200 py-2 animate-fade-in z-50">
          {/* User Info */}
          <div className="px-4 py-3 border-b border-gray-200">
            <p className="font-semibold text-gray-800">{user.name}</p>
            <p className="text-sm text-gray-600 truncate">{user.email}</p>
          </div>

          {/* Menu Items */}
          <div className="py-2">
            <a
              href="/reservations"
              className="flex items-center gap-3 px-4 py-2 hover:bg-gray-100 transition"
              onClick={() => setIsOpen(false)}
            >
              <Calendar className="w-5 h-5 text-gray-600" />
              <span className="text-gray-700">Mes Réservations</span>
            </a>

            <a
              href="/profile"
              className="flex items-center gap-3 px-4 py-2 hover:bg-gray-100 transition"
              onClick={() => setIsOpen(false)}
            >
              <User className="w-5 h-5 text-gray-600" />
              <span className="text-gray-700">Mon Profil</span>
            </a>

            <a
              href="/settings"
              className="flex items-center gap-3 px-4 py-2 hover:bg-gray-100 transition"
              onClick={() => setIsOpen(false)}
            >
              <Settings className="w-5 h-5 text-gray-600" />
              <span className="text-gray-700">Paramètres</span>
            </a>
          </div>

          {/* Logout */}
          <div className="border-t border-gray-200 pt-2">
            <button
              onClick={handleLogout}
              className="flex items-center gap-3 px-4 py-2 w-full hover:bg-red-50 text-red-600 transition"
            >
              <LogOut className="w-5 h-5" />
              <span className="font-semibold">Se déconnecter</span>
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserMenu;
