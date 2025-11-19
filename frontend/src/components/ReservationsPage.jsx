import React from 'react';
import { Calendar, Clock, Car } from 'lucide-react';

const ReservationsPage = () => {
  //mock data pour l'instant
  const mockReservations = [
    {
      id: 1,
      parkingName: 'Parking Agdal',
      spotNumber: 'A3',
      startTime: '2024-11-04T10:00',
      duration: 2,
      status: 'active'
    }
  ];

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="container mx-auto px-4">
        <h1 className="text-3xl font-bold text-gray-800 mb-8">Mes Réservations</h1>

        {mockReservations.length === 0 ? (
          <div className="bg-white rounded-lg shadow-lg p-12 text-center">
            <div className="text-6xl mb-4">📅</div>
            <h2 className="text-2xl font-bold text-gray-800 mb-2">
              Aucune réservation
            </h2>
            <p className="text-gray-600 mb-6">
              Vous n'avez pas encore de réservation active
            </p>
            <a
              href="/"
              className="inline-block bg-primary hover:bg-secondary text-white font-semibold py-3 px-8 rounded-lg transition"
            >
              Réserver une place
            </a>
          </div>
        ) : (
          <div className="grid gap-6">
            {mockReservations.map((reservation) => (
              <div
                key={reservation.id}
                className="bg-white rounded-lg shadow-lg p-6"
              >
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <h3 className="text-xl font-bold text-gray-800">
                      {reservation.parkingName}
                    </h3>
                    <p className="text-gray-600">Place {reservation.spotNumber}</p>
                  </div>
                  <span className="bg-green-100 text-green-800 px-4 py-1 rounded-full text-sm font-semibold">
                    Active
                  </span>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="flex items-center gap-2 text-gray-600">
                    <Calendar className="w-5 h-5" />
                    <span>04/11/2024 10:00</span>
                  </div>
                  <div className="flex items-center gap-2 text-gray-600">
                    <Clock className="w-5 h-5" />
                    <span>{reservation.duration}h de stationnement</span>
                  </div>
                  <div className="flex items-center gap-2 text-gray-600">
                    <Car className="w-5 h-5" />
                    <span>20 DH</span>
                  </div>
                </div>

                <div className="mt-4 pt-4 border-t flex gap-3">
                  <button className="flex-1 bg-red-100 hover:bg-red-200 text-red-700 font-semibold py-2 px-4 rounded-lg transition">
                    Annuler
                  </button>
                  <button className="flex-1 bg-blue-100 hover:bg-blue-200 text-blue-700 font-semibold py-2 px-4 rounded-lg transition">
                    Modifier
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default ReservationsPage;
