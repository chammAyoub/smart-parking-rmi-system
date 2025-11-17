import React, { useState, useEffect } from 'react';
import { Search, X } from 'lucide-react';

const SearchBar = ({ onSearch, placeholder = "Rechercher un parking..." }) => {
  const [value, setValue] = useState('');

  useEffect(() => {
    const timer = setTimeout(() => {
      onSearch(value);
    }, 500);

    return () => clearTimeout(timer);
  }, [value, onSearch]);

  const handleClear = () => {
    setValue('');
    onSearch('');
  };

  return (
    <div className="relative w-full max-w-md">
      <div className="relative">
        <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 w-5 h-5" />
        <input
          type="text"
          value={value}
          onChange={(e) => setValue(e.target.value)}
          placeholder={placeholder}
          className="
            w-full
    pl-12 pr-12 py-3
    border border-gray-300
    rounded-full
    shadow-sm
    text-gray-800
    placeholder-gray-400
    bg-white
    focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent
    transition
          "
        />
        {value && (
          <button
            onClick={handleClear}
            className="
              absolute right-4 top-1/2 -translate-y-1/2
              text-gray-400 hover:text-gray-600
              transition
            "
          >
            <X className="w-5 h-5" />
          </button>
        )}
      </div>
    </div>
  );
};

export default SearchBar;