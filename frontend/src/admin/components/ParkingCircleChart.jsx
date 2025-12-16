import React from "react";
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip } from "recharts";
import { MapPin, Car } from "lucide-react";

const COLORS = ["#ef4444", "#10b981"]; // Red = occupied, Green = free

const ParkingCircleChart = ({ parking, onClick }) => {
  const occupied = parking.occupied;
  const free = parking.total - parking.occupied;
  const data = [
    { name: "Occupées", value: occupied },
    { name: "Libres", value: free },
  ];

  const occupancyRate = Math.round((occupied / parking.total) * 100);

  return (
    <div
     
      className="cursor-pointer bg-white rounded-2xl shadow-lg border border-gray-100 overflow-hidden hover:shadow-2xl hover:scale-105 transition-all duration-300"
    >
      {/* Header */}
      <div className="bg-gradient-to-r from-blue-600 to-blue-500 p-4">
        <div className="flex items-center justify-between text-white">
          <div className="flex items-center gap-2">
            <MapPin className="w-5 h-5" />
            <h4 className="font-bold text-lg">{parking.name}</h4>
          </div>
          <div className="bg-white/20 backdrop-blur-sm rounded-full px-3 py-1">
            <span className="text-sm font-semibold">{occupancyRate}%</span>
          </div>
        </div>
      </div>

      {/* Pie chart with center overlay */}
      <div className="p-6">
        <div className="relative">
          <div className="h-48">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={data}
                  innerRadius={60}
                  outerRadius={85}
                  dataKey="value"
                  startAngle={90}
                  endAngle={-270}
                >
                  {data.map((_, index) => (
                    <Cell key={index} fill={COLORS[index]} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </div>

          {/* Center overlay for free spots */}
          <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
            <div className="text-center">
              <Car className="w-8 h-8 mx-auto text-gray-400 mb-1" />
              <p className="text-3xl font-bold text-gray-800">{free}</p>
              <p className="text-xs text-gray-500 uppercase tracking-wide">Libres</p>
            </div>
          </div>
        </div>

        {/* Stats boxes below */}
        <div className="mt-6 grid grid-cols-2 gap-4">
          <div className="bg-red-50 rounded-xl p-3 border border-red-100">
            <div className="flex items-center gap-2 mb-1">
              <div className="w-3 h-3 rounded-full bg-red-500"></div>
              <span className="text-xs font-medium text-gray-600">Occupées</span>
            </div>
            <p className="text-2xl font-bold text-red-600">{occupied}</p>
          </div>

          <div className="bg-green-50 rounded-xl p-3 border border-green-100">
            <div className="flex items-center gap-2 mb-1">
              <div className="w-3 h-3 rounded-full bg-green-500"></div>
              <span className="text-xs font-medium text-gray-600">Libres</span>
            </div>
            <p className="text-2xl font-bold text-green-600">{free}</p>
          </div>
        </div>

        {/* Footer with total capacity */}
        <div className="mt-4 pt-4 border-t border-gray-200">
          <p className="text-center text-sm text-gray-500">
            Capacité totale: <span className="font-semibold text-gray-700">{parking.total}</span> places
          </p>
        </div>
      </div>
    </div>
  );
};

export default ParkingCircleChart;