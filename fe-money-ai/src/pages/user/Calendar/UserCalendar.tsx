import React, { useState } from "react";
import { UtensilsCrossed, ChevronLeft, ChevronRight } from "lucide-react";
import DashboardLayout from "../../../components/layout/DashboardLayout";
import { userNavItems } from "../config";

const WEEK_HEADERS = ["T2", "T3", "T4", "T5", "T6", "T7", "CN"];

function getDaysInMonth(year: number, month: number) { return new Date(year, month + 1, 0).getDate(); }
function getFirstDayOfWeek(year: number, month: number) {
    const day = new Date(year, month, 1).getDay();
    return day === 0 ? 6 : day - 1;
}

const mockTransactions: Record<number, { id: number; cat: string; amount: number; type: "thu" | "chi" }[]> = {
    6: [
        { id: 1, cat: "Ăn uống", amount: 5000000, type: "chi" },
        { id: 2, cat: "Ăn uống", amount: 120000, type: "chi" },
    ],
    10: [
        { id: 3, cat: "Tiền lương", amount: 15000000, type: "thu" },
    ],
};

const UserCalendar: React.FC = () => {
    const today = new Date();
    const [year, setYear] = useState(today.getFullYear());
    const [month, setMonth] = useState(today.getMonth());
    const [selectedDay, setSelectedDay] = useState<number>(today.getDate());

    const daysInMonth = getDaysInMonth(year, month);
    const firstDayOffset = getFirstDayOfWeek(year, month);

    const prevMonth = () => { if (month === 0) { setMonth(11); setYear(y => y - 1); } else setMonth(m => m - 1); };
    const nextMonth = () => { if (month === 11) { setMonth(0); setYear(y => y + 1); } else setMonth(m => m + 1); };
    const monthLabel = `${String(month + 1).padStart(2, "0")}/${year}`;

    const selectedTxs = mockTransactions[selectedDay] ?? [];
    const thu = selectedTxs.filter(t => t.type === "thu").reduce((s, t) => s + t.amount, 0);
    const chi = selectedTxs.filter(t => t.type === "chi").reduce((s, t) => s + t.amount, 0);
    const tongThu = Object.values(mockTransactions).flat().filter(t => t.type === "thu").reduce((s, t) => s + t.amount, 0);
    const tongChi = Object.values(mockTransactions).flat().filter(t => t.type === "chi").reduce((s, t) => s + t.amount, 0);

    const isToday = (d: number) => d === today.getDate() && month === today.getMonth() && year === today.getFullYear();

    return (
        <DashboardLayout navItems={userNavItems} pageTitle="Lịch" userName="Người dùng" brandName="Money AI">
            {/* Header */}
            <div className="flex items-center justify-between mb-6">
                <div>
                    <h1 className="text-xl font-bold text-gray-800">Lịch giao dịch</h1>
                    <p className="text-sm text-gray-400 mt-0.5">Theo dõi thu chi theo ngày</p>
                </div>
                {/* Month summary */}
                <div className="hidden md:flex items-center gap-6 bg-white border border-gray-200 rounded-xl px-5 py-3">
                    <div className="text-center">
                        <p className="text-xs text-gray-400">Thu nhập</p>
                        <p className="text-sm font-bold text-blue-500">{tongThu.toLocaleString("vi-VN")}đ</p>
                    </div>
                    <div className="w-px h-8 bg-gray-100" />
                    <div className="text-center">
                        <p className="text-xs text-gray-400">Chi tiêu</p>
                        <p className="text-sm font-bold text-orange-500">{tongChi.toLocaleString("vi-VN")}đ</p>
                    </div>
                    <div className="w-px h-8 bg-gray-100" />
                    <div className="text-center">
                        <p className="text-xs text-gray-400">Tổng</p>
                        <p className={`text-sm font-bold ${tongThu - tongChi >= 0 ? "text-green-500" : "text-red-500"}`}>
                            {(tongThu - tongChi).toLocaleString("vi-VN")}đ
                        </p>
                    </div>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* Left: Calendar */}
                <div className="lg:col-span-2">
                    <div className="bg-white rounded-xl border border-gray-200 overflow-hidden">
                        {/* Month nav */}
                        <div className="flex items-center justify-between px-5 py-4 border-b border-gray-100">
                            <button onClick={prevMonth} className="w-8 h-8 rounded-lg hover:bg-gray-100 flex items-center justify-center text-gray-500 transition-colors">
                                <ChevronLeft size={18} />
                            </button>
                            <span className="text-base font-bold text-gray-800">{monthLabel}</span>
                            <button onClick={nextMonth} className="w-8 h-8 rounded-lg hover:bg-gray-100 flex items-center justify-center text-gray-500 transition-colors">
                                <ChevronRight size={18} />
                            </button>
                        </div>

                        {/* Week headers */}
                        <div className="grid grid-cols-7 border-b border-gray-50">
                            {WEEK_HEADERS.map((h) => (
                                <div key={h} className={`py-3 text-center text-xs font-semibold ${h === "T7" ? "text-blue-500" : h === "CN" ? "text-red-500" : "text-gray-400"}`}>{h}</div>
                            ))}
                        </div>

                        {/* Days */}
                        <div className="grid grid-cols-7">
                            {Array.from({ length: firstDayOffset }).map((_, i) => (
                                <div key={`e-${i}`} className="h-20 border-b border-r border-gray-50" />
                            ))}
                            {Array.from({ length: daysInMonth }, (_, i) => i + 1).map((day) => {
                                const colIdx = (firstDayOffset + day - 1) % 7;
                                const isSat = colIdx === 5, isSun = colIdx === 6;
                                const txs = mockTransactions[day];
                                const dayAmt = txs ? txs.reduce((s, t) => s + (t.type === "chi" ? -t.amount : t.amount), 0) : null;
                                return (
                                    <button
                                        key={day}
                                        onClick={() => setSelectedDay(day)}
                                        className={`h-20 border-b border-r border-gray-50 flex flex-col items-center pt-2 gap-1 transition-colors ${selectedDay === day ? "bg-orange-50" : "hover:bg-gray-50"
                                            }`}
                                    >
                                        <span className={`text-sm font-medium leading-none ${isToday(day)
                                                ? "w-7 h-7 rounded-full bg-orange-500 text-white flex items-center justify-center font-bold"
                                                : isSat ? "text-blue-500" : isSun ? "text-red-500" : "text-gray-700"
                                            }`}>{day}</span>
                                        {dayAmt !== null && (
                                            <span className={`text-[10px] font-semibold ${dayAmt >= 0 ? "text-blue-500" : "text-orange-500"}`}>
                                                {Math.abs(dayAmt) >= 1000000
                                                    ? `${(Math.abs(dayAmt) / 1000000).toFixed(1)}tr`
                                                    : Math.abs(dayAmt).toLocaleString("vi-VN")}
                                            </span>
                                        )}
                                    </button>
                                );
                            })}
                        </div>
                    </div>
                </div>

                {/* Right: Day detail panel */}
                <div className="space-y-4">
                    <div className="bg-white rounded-xl border border-gray-200 overflow-hidden">
                        <div className="px-4 py-3 bg-orange-50 border-b border-orange-100">
                            <p className="text-sm font-bold text-orange-600">
                                Ngày {String(selectedDay).padStart(2, "0")}/{String(month + 1).padStart(2, "0")}/{year}
                            </p>
                            <div className="flex items-center gap-4 mt-1">
                                <span className="text-xs text-gray-500">Thu: <span className="text-blue-500 font-semibold">{thu.toLocaleString("vi-VN")}đ</span></span>
                                <span className="text-xs text-gray-500">Chi: <span className="text-orange-500 font-semibold">{chi.toLocaleString("vi-VN")}đ</span></span>
                            </div>
                        </div>

                        {selectedTxs.length === 0 ? (
                            <div className="px-4 py-8 text-center">
                                <p className="text-sm text-gray-300">Không có giao dịch</p>
                            </div>
                        ) : (
                            <div className="divide-y divide-gray-50">
                                {selectedTxs.map((tx) => (
                                    <div key={tx.id} className="flex items-center gap-3 px-4 py-3 hover:bg-gray-50 transition-colors">
                                        <div className="w-8 h-8 rounded-full bg-orange-100 flex items-center justify-center shrink-0">
                                            <UtensilsCrossed size={15} className="text-orange-500" strokeWidth={1.5} />
                                        </div>
                                        <div className="flex-1">
                                            <p className="text-sm font-semibold text-gray-700">{tx.cat}</p>
                                        </div>
                                        <p className={`text-sm font-bold ${tx.type === "chi" ? "text-orange-500" : "text-blue-500"}`}>
                                            {tx.type === "chi" ? "-" : "+"}{tx.amount.toLocaleString("vi-VN")}đ
                                        </p>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>

                    {/* Quick stats for month */}
                    <div className="bg-white rounded-xl border border-gray-200 p-4 space-y-3">
                        <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide">Tháng này</p>
                        {[
                            { label: "Thu nhập", value: tongThu, color: "text-blue-500" },
                            { label: "Chi tiêu", value: tongChi, color: "text-orange-500" },
                            { label: "Còn lại", value: tongThu - tongChi, color: tongThu - tongChi >= 0 ? "text-green-500" : "text-red-500" },
                        ].map((s) => (
                            <div key={s.label} className="flex items-center justify-between">
                                <span className="text-sm text-gray-500">{s.label}</span>
                                <span className={`text-sm font-bold ${s.color}`}>{s.value.toLocaleString("vi-VN")}đ</span>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </DashboardLayout>
    );
};

export default UserCalendar;
