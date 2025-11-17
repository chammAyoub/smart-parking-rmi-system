import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import '../styles/ParkingMap.css';


import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

let DefaultIcon = L.icon({
  iconUrl: icon,
  shadowUrl: iconShadow,
  iconSize: [25, 41],
  iconAnchor: [12, 41]
});

L.Marker.prototype.options.icon = DefaultIcon;

const ParkingMap = ({ parkings, center, zoom = 13, onMarkerClick }) => {
  const getMarkerColor = (availableSpots, totalSpots) => {
    const percentage = (availableSpots / totalSpots) * 100;
    if (percentage > 50) return 'green';
    if (percentage > 20) return 'yellow';
    return 'red';
  };

  const createCustomIcon = (availableSpots, totalSpots) => {
    const color = getMarkerColor(availableSpots, totalSpots);
    
    return L.divIcon({
      className: 'custom-icon',
      html: `
        <div class="custom-marker ${color}">
          ${availableSpots}
        </div>
      `,
      iconSize: [40, 40],
      iconAnchor: [20, 40],
      popupAnchor: [0, -40]
    });
  };

  return (
    <MapContainer
      center={[center.lat, center.lng]}
      zoom={zoom}
      className="leaflet-container"
      scrollWheelZoom={true}
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      
      {parkings.map((parking) => (
        <Marker
          key={parking.id}
          position={[parking.latitude, parking.longitude]}
          icon={createCustomIcon(parking.availableSpots, parking.totalSpots)}
        >
          <Popup>
            <div className="custom-popup">
              <h3>{parking.name}</h3>
              <p className="text-sm text-gray-600 mb-2">{parking.address}</p>
              <p className="text-lg font-semibold mb-3">
                <span className={`text-${getMarkerColor(parking.availableSpots, parking.totalSpots)}-600`}>
                  {parking.availableSpots}/{parking.totalSpots}
                </span>
                {' '}places disponibles
              </p>
              <button onClick={() => onMarkerClick(parking)}>
                Voir les détails
              </button>
            </div>
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
};

export default ParkingMap;