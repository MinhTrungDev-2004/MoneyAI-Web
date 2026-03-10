import React from "react";
import { ChevronLeft, ChevronRight } from "lucide-react";

interface MonthNavigatorProps {
    label: string;          // e.g. "03/2026 (01/03–31/03)"
    onPrev: () => void;
    onNext: () => void;
    className?: string;
}

const MonthNavigator: React.FC<MonthNavigatorProps> = ({
    label,
    onPrev,
    onNext,
    className = "",
}) => (
    <div className={`flex items-center justify-between bg-[#fff8f0] rounded-md px-3 py-2 ${className}`}>
        <button onClick={onPrev} className="text-gray-500 hover:text-orange-500 transition-colors p-1">
            <ChevronLeft size={18} />
        </button>
        <span className="text-sm font-semibold text-gray-700">{label}</span>
        <button onClick={onNext} className="text-gray-500 hover:text-orange-500 transition-colors p-1">
            <ChevronRight size={18} />
        </button>
    </div>
);

export default MonthNavigator;
