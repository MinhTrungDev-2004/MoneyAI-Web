import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
    ChevronDown,
    LogOut,
    type LucideIcon,
} from "lucide-react";

export interface NavItem {
    label: string;
    icon: LucideIcon;
    path: string;
    children?: NavItem[];
}

interface SidebarProps {
    navItems: NavItem[];
    collapsed: boolean;
    onToggle: () => void;
    brandName?: string;
}

const Sidebar: React.FC<SidebarProps> = ({
    navItems,
    collapsed,
    brandName = "Money AI",
}) => {
    const navigate = useNavigate();
    const location = useLocation();
    const [expandedItem, setExpandedItem] = useState<string | null>(null);

    React.useEffect(() => {
        const activeParent = navItems.find((item) =>
            item.children?.some((c) => location.pathname === c.path)
        );
        if (activeParent) {
            setExpandedItem(activeParent.path);
        }
    }, [location.pathname, navItems]);

    const isActive = (path: string) => location.pathname === path;
    const isParentActive = (item: NavItem) =>
        item.children?.some((c) => location.pathname === c.path) || isActive(item.path);

    const handleItemClick = (item: NavItem) => {
        if (item.children) {
            setExpandedItem(expandedItem === item.path ? null : item.path);
        } else {
            navigate(item.path);
        }
    };

    return (
        <aside
            className={`h-screen flex flex-col bg-white border-r border-gray-200 transition-all duration-300 ${collapsed ? "w-14" : "w-56"
                } shrink-0`}
        >
            {/* Logo */}
            <div className="flex items-center gap-2 px-4 h-14 border-b border-gray-200 shrink-0">
                <div className="w-7 h-7 rounded bg-gradient-to-br from-orange-500 to-red-500 flex items-center justify-center shrink-0">
                    <span className="text-white text-xs font-extrabold">M</span>
                </div>
                {!collapsed && (
                    <span className="text-gray-800 font-bold text-sm tracking-tight">
                        {brandName}
                    </span>
                )}
            </div>

            {/* Nav */}
            <nav className="flex-1 overflow-y-auto py-2">
                {navItems.map((item) => {
                    const active = isParentActive(item);
                    const expanded = expandedItem === item.path;

                    return (
                        <div key={item.path}>
                            <button
                                onClick={() => handleItemClick(item)}
                                title={collapsed ? item.label : undefined}
                                className={`w-full flex items-center gap-3 px-4 py-2.5 text-sm transition-colors ${active
                                    ? "text-orange-500 font-semibold bg-orange-50"
                                    : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                                    } ${collapsed ? "justify-center" : "justify-between"}`}
                            >
                                <div className="flex items-center gap-3 min-w-0">
                                    <item.icon
                                        size={17}
                                        className={`shrink-0 ${active ? "text-orange-500" : "text-gray-400"}`}
                                    />
                                    {!collapsed && (
                                        <span className="truncate">{item.label}</span>
                                    )}
                                </div>
                                {!collapsed && item.children && (
                                    <ChevronDown
                                        size={14}
                                        className={`shrink-0 text-gray-400 transition-transform duration-200 ${expanded ? "rotate-180" : ""
                                            }`}
                                    />
                                )}
                            </button>

                            {/* Sub-items */}
                            {!collapsed && item.children && expanded && (
                                <div className="bg-gray-50 border-b border-gray-100">
                                    {item.children.map((child) => (
                                        <button
                                            key={child.path}
                                            onClick={() => navigate(child.path)}
                                            className={`w-full flex items-center gap-3 pl-10 pr-4 py-2 text-sm transition-colors ${isActive(child.path)
                                                ? "text-orange-500 font-semibold"
                                                : "text-gray-500 hover:text-gray-900 hover:bg-gray-100"
                                                }`}
                                        >
                                            <child.icon size={14} className="shrink-0" />
                                            <span className="truncate">{child.label}</span>
                                        </button>
                                    ))}
                                </div>
                            )}
                        </div>
                    );
                })}
            </nav>

            {/* Logout */}
            <div className="border-t border-gray-200 px-4 py-3">
                <button
                    onClick={() => navigate("/login")}
                    className={`flex items-center gap-3 text-sm text-gray-400 hover:text-red-500 transition-colors ${collapsed ? "justify-center w-full" : ""
                        }`}
                    title={collapsed ? "Đăng xuất" : undefined}
                >
                    <LogOut size={17} />
                    {!collapsed && <span>Đăng xuất</span>}
                </button>
            </div>
        </aside>
    );
};

export default Sidebar;
