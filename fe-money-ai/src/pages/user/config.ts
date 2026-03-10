import {
    PenSquare,
    Calendar,
    PieChart,
    Wallet,
    Settings,
    TrendingDown,
    TrendingUp,
} from "lucide-react";
import { type NavItem } from "../../components/layout/Sidebar";

export const userNavItems: NavItem[] = [
    {
        label: "Nhập vào",
        icon: PenSquare,
        path: "/user/entry",
        children: [
            { label: "Khoản chi", icon: TrendingDown, path: "/user/entry/chi" },
            { label: "Khoản thu", icon: TrendingUp,   path: "/user/entry/thu" },
        ],
    },
    { label: "Lịch",      icon: Calendar,  path: "/user/calendar" },
    { label: "Báo cáo",   icon: PieChart,  path: "/user/report" },
    { label: "Ngân sách", icon: Wallet,    path: "/user/budget" },
    { label: "Cài đặt",   icon: Settings,  path: "/user/settings" },
];
