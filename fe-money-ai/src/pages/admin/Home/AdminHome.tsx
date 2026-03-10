import React from "react";
import { useNavigate } from "react-router-dom";
import {
    Users,
    FileText,
    BarChart2,
    Settings,
    Bell,
    Shield,
    CreditCard,
    Activity,
    BookOpen,
    Calendar,
    Award,
    AlertCircle,
    type LucideIcon,
} from "lucide-react";
import DashboardLayout from "../../../components/layout/DashboardLayout";
import { type NavItem } from "../../../components/layout/Sidebar";

/* ── Nav items cho Admin ── */
const adminNavItems: NavItem[] = [
    { label: "Trang chủ", icon: BarChart2, path: "/admin/home" },
    {
        label: "Quản lý người dùng", icon: Users, path: "/admin/users",
        children: [
            { label: "Danh sách người dùng", icon: Users, path: "/admin/users/list" },
            { label: "Phân quyền", icon: Shield, path: "/admin/users/roles" },
            { label: "Hoạt động", icon: Activity, path: "/admin/users/activity" },
        ],
    },
    {
        label: "Quản lý giao dịch", icon: CreditCard, path: "/admin/transactions",
        children: [
            { label: "Tất cả giao dịch", icon: CreditCard, path: "/admin/transactions/all" },
            { label: "Chờ xử lý", icon: AlertCircle, path: "/admin/transactions/pending" },
        ],
    },
    { label: "Báo cáo & Thống kê", icon: BarChart2, path: "/admin/reports" },
    {
        label: "Nội dung & Tin tức", icon: BookOpen, path: "/admin/content",
        children: [
            { label: "Bài viết", icon: FileText, path: "/admin/content/posts" },
            { label: "Danh mục", icon: BookOpen, path: "/admin/content/categories" },
        ],
    },
    { label: "Lịch & Sự kiện", icon: Calendar, path: "/admin/calendar" },
    { label: "Khen thưởng", icon: Award, path: "/admin/rewards" },
    { label: "Cảnh báo & Log", icon: AlertCircle, path: "/admin/logs" },
    { label: "Thông báo", icon: Bell, path: "/admin/notifications" },
    { label: "Cài đặt hệ thống", icon: Settings, path: "/admin/settings" },
];

/* ── Feature card data ── */
interface FeatureCard {
    label: string;
    icon: LucideIcon;
    path: string;
    color: string;
}

const featureCards: FeatureCard[] = [
    { label: "Quản lý người dùng", icon: Users, path: "/admin/users", color: "from-violet-500 to-purple-600" },
    { label: "Quản lý giao dịch", icon: CreditCard, path: "/admin/transactions", color: "from-emerald-400 to-teal-600" },
    { label: "Báo cáo & Thống kê", icon: BarChart2, path: "/admin/reports", color: "from-sky-400 to-blue-600" },
    { label: "Tra cứu thông tin", icon: FileText, path: "/admin/content", color: "from-orange-400 to-amber-500" },
    { label: "Nội dung & Tin tức", icon: BookOpen, path: "/admin/content", color: "from-pink-400 to-rose-600" },
    { label: "Lịch & Sự kiện", icon: Calendar, path: "/admin/calendar", color: "from-indigo-400 to-violet-600" },
    { label: "Khen thưởng", icon: Award, path: "/admin/rewards", color: "from-yellow-400 to-orange-500" },
    { label: "Cảnh báo & Log", icon: AlertCircle, path: "/admin/logs", color: "from-red-400 to-rose-600" },
    { label: "Thông báo", icon: Bell, path: "/admin/notifications", color: "from-cyan-400 to-sky-600" },
    { label: "Phân quyền", icon: Shield, path: "/admin/roles", color: "from-teal-400 to-emerald-600" },
    { label: "Cài đặt hệ thống", icon: Settings, path: "/admin/settings", color: "from-slate-400 to-gray-600" },
    { label: "Hoạt động hệ thống", icon: Activity, path: "/admin/activity", color: "from-fuchsia-400 to-pink-600" },
];

/* ── Feature Card component ── */
const FeatureCardItem: React.FC<{ card: FeatureCard; onClick: () => void }> = ({
    card,
    onClick,
}) => (
    <button
        onClick={onClick}
        className={`bg-gradient-to-br ${card.color} rounded-lg p-6 flex flex-col items-center justify-center gap-4 text-white shadow hover:shadow-lg hover:scale-[1.02] active:scale-100 transition-all duration-200 aspect-square`}
    >
        <card.icon size={48} strokeWidth={1.2} className="opacity-90" />
        <span className="text-sm font-semibold text-center leading-tight">
            {card.label}
        </span>
    </button>
);

/* ── Page Component ── */
const AdminHome: React.FC = () => {
    const navigate = useNavigate();

    return (
        <DashboardLayout
            navItems={adminNavItems}
            pageTitle="Trang chủ"
            userName="Admin"
        >
            {/* Grid feature cards */}
            <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4">
                {featureCards.map((card) => (
                    <FeatureCardItem
                        key={card.path + card.label}
                        card={card}
                        onClick={() => navigate(card.path)}
                    />
                ))}
            </div>
        </DashboardLayout>
    );
};

export default AdminHome;
