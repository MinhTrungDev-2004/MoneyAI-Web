import React from "react";
import {
    LayoutDashboard,
    CreditCard,
    BarChart2,
    FileText,
    Bell,
    Settings,
    type LucideIcon,
} from "lucide-react";
import DashboardLayout from "../../../components/layout/DashboardLayout";
import { type NavItem } from "../../../components/layout/Sidebar";

/* ── Nav items cho User ── */
const userNavItems: NavItem[] = [
    { label: "Trang chủ", icon: LayoutDashboard, path: "/user/home" },
    { label: "Giao dịch", icon: CreditCard, path: "/user/transactions" },
    { label: "Thống kê", icon: BarChart2, path: "/user/stats" },
    { label: "Báo cáo", icon: FileText, path: "/user/reports" },
    { label: "Thông báo", icon: Bell, path: "/user/notifications" },
    { label: "Cài đặt", icon: Settings, path: "/user/settings" },
];

/* ── Feature card data ── */
interface FeatureCard {
    label: string;
    icon: LucideIcon;
    path: string;
    color: string;
}

const featureCards: FeatureCard[] = [
    { label: "Giao dịch", icon: CreditCard, path: "/user/transactions", color: "from-emerald-400 to-teal-600" },
    { label: "Thống kê", icon: BarChart2, path: "/user/stats", color: "from-sky-400 to-blue-600" },
    { label: "Báo cáo", icon: FileText, path: "/user/reports", color: "from-violet-500 to-purple-600" },
    { label: "Thông báo", icon: Bell, path: "/user/notifications", color: "from-orange-400 to-amber-500" },
    { label: "Cài đặt", icon: Settings, path: "/user/settings", color: "from-slate-400 to-gray-600" },
];

/* ── Page Component ── */
const UserHome: React.FC = () => {
    return (
        <DashboardLayout
            navItems={userNavItems}
            pageTitle="Trang chủ"
            userName="Người dùng"
        >
            <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4">
                {featureCards.map((card) => (
                    <button
                        key={card.label}
                        className={`bg-gradient-to-br ${card.color} rounded-lg p-6 flex flex-col items-center justify-center gap-4 text-white shadow hover:shadow-lg hover:scale-[1.02] active:scale-100 transition-all duration-200 aspect-square`}
                    >
                        <card.icon size={48} strokeWidth={1.2} className="opacity-90" />
                        <span className="text-sm font-semibold text-center leading-tight">
                            {card.label}
                        </span>
                    </button>
                ))}
            </div>
        </DashboardLayout>
    );
};

export default UserHome;
