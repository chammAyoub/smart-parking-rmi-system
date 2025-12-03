import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, 
  Legend, ResponsiveContainer } from 'recharts';

const data = [
  { name: 'Agdal', occupied: 15, total: 20 }, 
  { name: 'Hassan', occupied: 8, total: 20 }, 
  { name: 'Océan', occupied: 3, total: 20 }
];

const AdminStats = () => {
  return (
    <div className="bg-white p-6 rounded-2xl shadow-lg border 
      border-gray-100">
      <h3 className="text-lg font-bold text-gray-700 mb-4">
        Taux d'Occupation
      </h3>
      <div className="h-80">
        <ResponsiveContainer width="100%" height="100%">
          <BarChart data={data}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Bar dataKey="occupied" fill="#667eea" name="Occupées" 
              radius={[4, 4, 0, 0]} />
            <Bar dataKey="total" fill="#e5e7eb" name="Total" 
              radius={[4, 4, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default AdminStats;