// Mock data pour développement sans backend

export const mockParkings = [
  {
    id: 1,
    name: "Parking Agdal",
    address: "Avenue Mehdi Ben Barka, Rabat",
    latitude: 33.9716,
    longitude: -6.8498,
    totalSpots: 20,
    availableSpots: 15
  },
  {
    id: 2,
    name: "Parking Hassan",
    address: "Boulevard Hassan II, Rabat",
    latitude: 34.0209,
    longitude: -6.8416,
    totalSpots: 20,
    availableSpots: 8
  },
  {
    id: 3,
    name: "Parking Océan",
    address: "Avenue de l'Océan, Rabat",
    latitude: 33.9939,
    longitude: -6.8503,
    totalSpots: 20,
    availableSpots: 3
  }
];

export const mockSpots = Array.from({ length: 20 }, (_, i) => {
  const row = String.fromCharCode(65 + Math.floor(i / 5)); // A, B, C, D
  const num = (i % 5) + 1;
  const statuses = ['available', 'occupied', 'reserved'];
  const randomStatus = statuses[Math.floor(Math.random() * statuses.length)];
  
  return {
    id: i + 1,
    spotNumber: `${row}${num}`,
    status: i < 12 ? 'available' : randomStatus,
    parkingLotId: 1
  };
});