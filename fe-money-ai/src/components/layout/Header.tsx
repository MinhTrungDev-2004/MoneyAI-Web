import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
    Menu,
    Bell,
    ChevronDown,
    User,
    Settings,
    LogOut,
} from "lucide-react";

interface HeaderProps {
    title?: string;
    userName?: string;
    onToggleSidebar: () => void;
}

const Header: React.FC<HeaderProps> = ({
    title,
    userName = "Admin",
    onToggleSidebar,
}) => {
    const navigate = useNavigate();
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const dropdownRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const handleClickOutside = (e: MouseEvent) => {
            if (dropdownRef.current && !dropdownRef.current.contains(e.target as Node)) {
                setDropdownOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    const menuItems = [
        { icon: User, label: "Hồ sơ cá nhân", action: () => navigate("/admin/profile") },
        { icon: Settings, label: "Cài đặt tài khoản", action: () => navigate("/admin/settings") },
        { icon: LogOut, label: "Đăng xuất", action: () => navigate("/login"), danger: true },
    ];

    return (
        <header className="h-14 bg-white border-b border-gray-200 flex items-center justify-between px-5 shrink-0 relative z-50">
            {/* Trái: hamburger + breadcrumb */}
            <div className="flex items-center gap-3">
                <button
                    onClick={onToggleSidebar}
                    className="text-gray-400 hover:text-gray-600 transition-colors"
                    aria-label="Toggle sidebar"
                >
                    <Menu size={20} />
                </button>
                {title && (
                    <span className="text-gray-700 text-sm font-medium">{title}</span>
                )}
            </div>

            {/* Phải: bell + user dropdown */}
            <div className="flex items-center gap-3">
                <button className="relative text-gray-400 hover:text-gray-600 transition-colors p-1">
                    <Bell size={20} />
                    <span className="absolute -top-0.5 -right-0.5 w-4 h-4 bg-red-500 rounded-full text-[10px] text-white flex items-center justify-center font-bold">
                        3
                    </span>
                </button>

                {/* User dropdown */}
                <div className="relative" ref={dropdownRef}>
                    <button
                        onClick={() => setDropdownOpen(!dropdownOpen)}
                        className="flex items-center gap-2 rounded-lg px-2 py-1.5 hover:bg-gray-100 transition-colors"
                    >
                        <div className="w-7 h-7 rounded-full bg-gradient-to-br from-orange-400 to-red-500 flex items-center justify-center text-white text-xs font-bold uppercase shrink-0">
                            {userName.charAt(0)}
                        </div>
                        <span className="text-gray-700 text-sm font-medium hidden sm:block">
                            {userName}
                        </span>
                        <ChevronDown
                            size={14}
                            className={`text-gray-400 transition-transform duration-200 ${dropdownOpen ? "rotate-180" : ""}`}
                        />
                    </button>

                    {dropdownOpen && (
                        <div className="absolute right-0 top-full mt-2 w-56 bg-white rounded-xl shadow-xl border border-gray-100 overflow-hidden">
                            <div className="px-4 py-3 bg-gray-50 border-b border-gray-100">
                                <div className="flex items-center gap-3">
                                    <div className="w-9 h-9 rounded-full bg-gradient-to-br from-orange-400 to-red-500 flex items-center justify-center text-white text-sm font-bold uppercase">
                                        {userName.charAt(0)}
                                    </div>
                                    <div>
                                        <p className="text-sm font-semibold text-gray-900">{userName}</p>
                                        <p className="text-xs text-gray-400">Quản trị viên</p>
                                    </div>
                                </div>
                            </div>
                            <div className="py-1">
                                {menuItems.map((item) => (
                                    <button
                                        key={item.label}
                                        onClick={() => { item.action(); setDropdownOpen(false); }}
                                        className={`w-full flex items-center gap-3 px-4 py-2.5 text-sm transition-colors ${item.danger
                                                ? "text-red-500 hover:bg-red-50"
                                                : "text-gray-700 hover:bg-gray-50"
                                            }`}
                                    >
                                        <item.icon size={16} className="shrink-0" />
                                        {item.label}
                                    </button>
                                ))}
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </header>
    );
};

export default Header;
