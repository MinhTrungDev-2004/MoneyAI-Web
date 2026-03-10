import React from "react";

interface TypeToggleProps {
    options: [string, string];
    value: string;
    onChange: (v: string) => void;
    className?: string;
}

const TypeToggle: React.FC<TypeToggleProps> = ({
    options,
    value,
    onChange,
    className = "",
}) => (
    <div className={`flex bg-gray-100 rounded-full p-1 gap-1 ${className}`}>
        {options.map((opt) => (
            <button
                key={opt}
                onClick={() => onChange(opt)}
                className={`flex-1 px-4 py-1.5 rounded-full text-sm font-semibold transition-all duration-200 ${value === opt
                        ? "bg-orange-500 text-white shadow-sm"
                        : "text-gray-400 hover:text-gray-600"
                    }`}
            >
                {opt}
            </button>
        ))}
    </div>
);

export default TypeToggle;
