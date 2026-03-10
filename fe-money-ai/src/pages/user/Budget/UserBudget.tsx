import React, { useState } from "react";
import {
    UtensilsCrossed, ShoppingBag, Shirt, Sparkles,
    Users, HeartPulse, BookOpen, Zap, ChevronRight,
    ChevronLeft, Plus, Target,
} from "lucide-react";
import DashboardLayout from "../../../components/layout/DashboardLayout";
import { userNavItems } from "../config";

interface BudgetItem {
    label: string;
    icon: React.ElementType;
    iconColor: string;
    iconBg: string;
    budget: number | null;
    spent: number;
}

const budgetItems: BudgetItem[] = [
    { label: "Ăn uống", icon: UtensilsCrossed, iconColor: "text-orange-500", iconBg: "bg-orange-100", budget: null, spent: 5000000 },
    { label: "Chi tiêu hàng ngày", icon: ShoppingBag, iconColor: "text-green-500", iconBg: "bg-green-100", budget: null, spent: 0 },
    { label: "Quần áo", icon: Shirt, iconColor: "text-blue-500", iconBg: "bg-blue-100", budget: null, spent: 0 },
    { label: "Mỹ phẩm", icon: Sparkles, iconColor: "text-pink-500", iconBg: "bg-pink-100", budget: null, spent: 0 },
    { label: "Phí giao lưu", icon: Users, iconColor: "text-yellow-600", iconBg: "bg-yellow-100", budget: null, spent: 0 },
    { label: "Y tế", icon: HeartPulse, iconColor: "text-teal-500", iconBg: "bg-teal-100", budget: null, spent: 0 },
    { label: "Giáo dục", icon: BookOpen, iconColor: "text-orange-600", iconBg: "bg-orange-100", budget: null, spent: 0 },
    { label: "Tiền điện", icon: Zap, iconColor: "text-cyan-500", iconBg: "bg-cyan-100", budget: null, spent: 0 },
];

const UserBudget: React.FC = () => {
    const today = new Date();
    const [year, setYear] = useState(today.getFullYear());
    const [month, setMonth] = useState(today.getMonth());

    const prevMonth = () => { if (month === 0) { setMonth(11); setYear(y => y - 1); } else setMonth(m => m - 1); };
    const nextMonth = () => { if (month === 11) { setMonth(0); setYear(y => y + 1); } else setMonth(m => m + 1); };
    const monthLabel = `${String(month + 1).padStart(2, "0")}/${year}`;

    const totalSpent = budgetItems.reduce((s, b) => s + b.spent, 0);

    return (
        <DashboardLayout navItems={userNavItems} pageTitle="Ngân sách" userName="Người dùng" brandName="Money AI">
            {/* Header */}
            <div className="flex items-center justify-between mb-6">
                <div>
                    <h1 className="text-xl font-bold text-gray-800">Ngân sách</h1>
                    <p className="text-sm text-gray-400 mt-0.5">Quản lý hạn mức chi tiêu theo tháng</p>
                </div>
                <div className="flex items-center gap-3">
                    <div className="flex items-center gap-2 bg-orange-50 rounded-xl px-4 py-2">
                        <button onClick={prevMonth} className="text-gray-400 hover:text-orange-500 transition-colors"><ChevronLeft size={18} /></button>
                        <span className="text-sm font-bold text-gray-700 min-w-16 text-center">{monthLabel}</span>
                        <button onClick={nextMonth} className="text-gray-400 hover:text-orange-500 transition-colors"><ChevronRight size={18} /></button>
                    </div>
                    <button className="flex items-center gap-2 bg-orange-500 hover:bg-orange-600 text-white rounded-xl px-4 py-2 text-sm font-semibold transition-colors">
                        <Plus size={15} />
                        Thêm ngân sách
                    </button>
                </div>
            </div>

            {/* Overview card */}
            <div className="bg-white rounded-xl border border-gray-200 p-5 mb-6">
                <div className="flex items-start justify-between mb-4">
                    <div className="flex items-center gap-3">
                        <div className="w-10 h-10 rounded-xl bg-orange-100 flex items-center justify-center">
                            <Target size={20} className="text-orange-500" />
                        </div>
                        <div>
                            <p className="text-sm font-bold text-gray-800">Tổng ngân sách tháng</p>
                            <p className="text-xs text-gray-400">Ngân sách Chưa đặt</p>
                        </div>
                    </div>
                    <div className="text-right">
                        <p className="text-xs text-gray-400">Đã chi tiêu</p>
                        <p className="text-lg font-bold text-orange-500">{totalSpent.toLocaleString("vi-VN")}đ</p>
                    </div>
                </div>
                <div className="w-full h-3 bg-gray-100 rounded-full overflow-hidden">
                    <div className="h-full rounded-full bg-orange-500" style={{ width: "100%" }} />
                </div>
                <div className="flex items-center justify-between mt-2">
                    <span className="text-xs text-gray-400">100% đã sử dụng</span>
                    <span className="text-xs text-red-500 font-semibold">Còn lại: {(-totalSpent).toLocaleString("vi-VN")}đ</span>
                </div>
            </div>

            {/* Budget list — 2 columns on large screens */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {budgetItems.map((item) => {
                    const pct = item.budget ? Math.min((item.spent / item.budget) * 100, 100) : (item.spent > 0 ? 100 : 0);
                    const over = item.budget ? item.spent > item.budget : false;
                    return (
                        <div key={item.label} className="bg-white rounded-xl border border-gray-200 p-4 hover:shadow-sm transition-shadow">
                            <div className="flex items-center gap-3 mb-3">
                                <div className={`w-9 h-9 rounded-xl ${item.iconBg} flex items-center justify-center shrink-0`}>
                                    <item.icon size={17} className={item.iconColor} strokeWidth={1.5} />
                                </div>
                                <div className="flex-1 min-w-0">
                                    <div className="flex items-center justify-between">
                                        <span className="text-sm font-semibold text-gray-800">{item.label}</span>
                                        {item.budget ? (
                                            <span className={`text-sm font-bold ${over ? "text-red-500" : "text-gray-800"}`}>
                                                {(item.budget - item.spent).toLocaleString("vi-VN")}đ
                                            </span>
                                        ) : (
                                            <button className="text-xs text-orange-500 hover:text-orange-600 font-semibold flex items-center gap-1">
                                                <Plus size={12} /> Đặt ngân sách
                                            </button>
                                        )}
                                    </div>
                                </div>
                                <ChevronRight size={14} className="text-gray-300 shrink-0" />
                            </div>

                            {/* Progress */}
                            <div className="w-full h-2 bg-gray-100 rounded-full overflow-hidden">
                                <div
                                    className={`h-full rounded-full transition-all ${over ? "bg-red-500" : item.spent > 0 ? "bg-orange-500" : "bg-gray-200"}`}
                                    style={{ width: `${pct}%` }}
                                />
                            </div>
                            <div className="flex items-center justify-between mt-1.5">
                                <span className="text-xs text-gray-400">
                                    {item.budget ? `Ngân sách: ${item.budget.toLocaleString("vi-VN")}đ` : "Chưa đặt ngân sách"}
                                </span>
                                <span className="text-xs text-gray-400">
                                    {item.spent > 0 ? `Chi: ${item.spent.toLocaleString("vi-VN")}đ` : "—"}
                                </span>
                            </div>
                        </div>
                    );
                })}
            </div>
        </DashboardLayout>
    );
};

export default UserBudget;
