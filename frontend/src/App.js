import './App.css';

function App() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-primary to-secondary flex items-center justify-center">
      <div className="bg-white p-8 rounded-lg shadow-2xl max-w-md">
        <h1 className="text-4xl font-bold text-primary mb-4">
          🚗 Smart Parking
        </h1>
        <p className="text-gray-600 mb-6">
          Système de gestion de parking intelligent
        </p>
        <button className="bg-primary hover:bg-secondary text-white font-bold py-3 px-6 rounded-full transition duration-300">
          Démarrer
        </button>
      </div>
    </div>
  );
}

export default App;