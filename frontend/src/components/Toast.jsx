import React, { useEffect } from 'react';
import { X, CheckCircle, XCircle, Info, AlertTriangle } from 'lucide-react';

const Toast = ({ message, type = 'info', duration = 3000, onClose }) => {
  useEffect(() => {
    if (duration > 0) {
      const timer = setTimeout(() => {
        onClose();
      }, duration);
      
      return () => clearTimeout(timer);
    }
  }, [duration, onClose]);

  const types = {
    success: {
      bg: 'bg-green-500',
      icon: <CheckCircle className="w-5 h-5" />
    },
    error: {
      bg: 'bg-red-500',
      icon: <XCircle className="w-5 h-5" />
    },
    info: {
      bg: 'bg-blue-500',
      icon: <Info className="w-5 h-5" />
    },
    warning: {
      bg: 'bg-yellow-500',
      icon: <AlertTriangle className="w-5 h-5" />
    }
  };

  const currentType = types[type] || types.info;

  return (
    <div
      className={`
        fixed top-4 right-4 z-50
        ${currentType.bg} text-white
        px-6 py-4 rounded-lg shadow-lg
        flex items-center gap-3
        animate-slide-in
        max-w-md
      `}
    >
      {currentType.icon}
      <p className="flex-1">{message}</p>
      <button
        onClick={onClose}
        className="hover:bg-white/20 rounded p-1 transition"
      >
        <X className="w-4 h-4" />
      </button>
    </div>
  );
};

export default Toast;