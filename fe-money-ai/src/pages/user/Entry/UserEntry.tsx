import React, { useState } from "react";
import {
    ChevronLeft, ChevronRight, Pencil,
    UtensilsCrossed, ShoppingBag, Shirt, Sparkles, Users,
    HeartPulse, BookOpen, Zap, Train, Phone, Home, Settings,
    Wallet, PiggyBank, Gift, Coins, TrendingUp, Briefcase,
} from "lucide-react";
import DashboardLayout from "../../../components/layout/DashboardLayout";
import { userNavItems } from "../config";

const expenseCategories = [
    { label: "Ăn uống", icon: UtensilsCrossed, color: "text-orange-500", bg: "bg-orange-50" },
    { label: "Chi tiêu hàng ngày", icon: ShoppingBag, color: "text-green-500", bg: "bg-green-50" },
    { label: "Quần áo", icon: Shirt, color: "text-blue-500", bg: "bg-blue-50" },
    { label: "Mỹ phẩm", icon: Sparkles, color: "text-pink-500", bg: "bg-pink-50" },
    { label: "Phí giao lưu", icon: Users, color: "text-yellow-600", bg: "bg-yellow-50" },
    { label: "Y tế", icon: HeartPulse, color: "text-teal-500", bg: "bg-teal-50" },
    { label: "Giáo dục", icon: BookOpen, color: "text-orange-600", bg: "bg-orange-50" },
    { label: "Tiền điện", icon: Zap, color: "text-cyan-500", bg: "bg-cyan-50" },
    { label: "Đi lại", icon: Train, color: "text-gray-600", bg: "bg-gray-100" },
    { label: "Phí liên lạc", icon: Phone, color: "text-indigo-500", bg: "bg-indigo-50" },
    { label: "Tiền nhà", icon: Home, color: "text-amber-600", bg: "bg-amber-50" },
    { label: "Chỉnh sửa", icon: Settings, color: "text-gray-400", bg: "bg-gray-50", isEdit: true },
];

const incomeCategories = [
    { label: "Tiền lương", icon: Wallet, color: "text-green-500", bg: "bg-green-50" },
    { label: "Tiền phụ cấp", icon: Briefcase, color: "text-orange-400", bg: "bg-orange-50" },
    { label: "Tiền thưởng", icon: Gift, color: "text-red-500", bg: "bg-red-50" },
    { label: "Thu nhập phụ", icon: Coins, color: "text-teal-500", bg: "bg-teal-50" },
    { label: "Đầu tư", icon: TrendingUp, color: "text-blue-500", bg: "bg-blue-50" },
    { label: "Thu nhập tạm thời", icon: PiggyBank, color: "text-pink-500", bg: "bg-pink-50" },
    { label: "Chỉnh sửa", icon: Settings, color: "text-gray-400", bg: "bg-gray-50", isEdit: true },
];

function fmtDate(d: Date) {
    const days = ["Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"];
    return `${String(d.getDate()).padStart(2, "0")}/${String(d.getMonth() + 1).padStart(2, "0")}/${d.getFullYear()} (${days[d.getDay()]})`;
}

interface UserEntryProps {
    defaultType?: "Tiền chi" | "Tiền thu";
}

const UserEntry: React.FC<UserEntryProps> = ({ defaultType = "Tiền chi" }) => {
    const type = defaultType;
    const [date, setDate] = useState(new Date());
    const [note, setNote] = useState("");
    const [amount, setAmount] = useState("");
    const [selectedCat, setSelectedCat] = useState<string | null>(null);

    const categories = type === "Tiền chi" ? expenseCategories : incomeCategories;
    const prevDay = () => { const d = new Date(date); d.setDate(d.getDate() - 1); setDate(d); };
    const nextDay = () => { const d = new Date(date); d.setDate(d.getDate() + 1); setDate(d); };

    const handleSubmit = () => {
        if (!amount || !selectedCat) {
            alert("Vui lòng chọn danh mục và nhập số tiền.");
            return;
        }
        alert(`Đã ghi ${type}: ${Number(amount).toLocaleString("vi-VN")}đ – ${selectedCat}`);
        setAmount(""); setNote(""); setSelectedCat(null);
    };

    return (
        <DashboardLayout navItems={userNavItems} pageTitle="Nhập vào" userName="Người dùng" brandName="Money AI">
            {/* 2-column layout */}
            <div className="grid grid-cols-1 lg:grid-cols-5 gap-6">
                {/* Left: Form details */}
                <div className="lg:col-span-2 space-y-4">
                    <div className="bg-white rounded-xl border border-gray-200 overflow-hidden">
                        <div className="px-5 py-3 bg-gray-50 border-b border-gray-100">
                            <span className="text-xs font-semibold text-gray-500 uppercase tracking-wide">Thông tin giao dịch</span>
                        </div>

                        {/* Date */}
                        <div className="flex items-center gap-3 px-5 py-4 border-b border-gray-50">
                            <span className="text-sm text-gray-500 w-20 shrink-0">Ngày</span>
                            <div className="flex items-center gap-2 bg-orange-50 rounded-lg px-3 py-2 flex-1">
                                <button onClick={prevDay} className="text-gray-400 hover:text-orange-500 transition-colors">
                                    <ChevronLeft size={16} />
                                </button>
                                <span className="flex-1 text-center text-sm font-semibold text-gray-700">{fmtDate(date)}</span>
                                <button onClick={nextDay} className="text-gray-400 hover:text-orange-500 transition-colors">
                                    <ChevronRight size={16} />
                                </button>
                            </div>
                        </div>

                        {/* Note */}
                        <div className="flex items-center gap-3 px-5 py-4 border-b border-gray-50">
                            <span className="text-sm text-gray-500 w-20 shrink-0">Ghi chú</span>
                            <input
                                type="text"
                                value={note}
                                onChange={(e) => setNote(e.target.value)}
                                placeholder="Thêm ghi chú..."
                                className="flex-1 text-sm text-gray-700 bg-transparent outline-none border-b border-transparent focus:border-orange-300 pb-1 transition-colors placeholder:text-gray-300"
                            />
                        </div>

                        {/* Amount */}
                        <div className="flex items-center gap-3 px-5 py-4">
                            <span className="text-sm text-gray-500 w-20 shrink-0">
                                {type === "Tiền chi" ? "Tiền chi" : "Tiền thu"}
                            </span>
                            <div className="flex-1 flex items-center gap-2">
                                <input
                                    type="number"
                                    value={amount}
                                    onChange={(e) => setAmount(e.target.value)}
                                    placeholder="0"
                                    className="flex-1 text-3xl font-bold text-gray-800 bg-transparent outline-none placeholder:text-gray-200 w-0"
                                />
                                <span className="text-lg text-gray-400 font-semibold shrink-0">đ</span>
                            </div>
                        </div>
                    </div>

                    {/* Selected category preview */}
                    {selectedCat && (
                        <div className="bg-orange-50 border border-orange-200 rounded-xl px-5 py-3 flex items-center justify-between">
                            <div>
                                <p className="text-xs text-orange-500 font-semibold uppercase tracking-wide">Danh mục đã chọn</p>
                                <p className="text-sm font-bold text-gray-800 mt-0.5">{selectedCat}</p>
                            </div>
                            <button onClick={() => setSelectedCat(null)} className="text-gray-400 hover:text-gray-600 text-xl leading-none">×</button>
                        </div>
                    )}

                    {/* Submit */}
                    <button
                        onClick={handleSubmit}
                        className="w-full py-4 bg-orange-500 hover:bg-orange-600 active:scale-95 text-white text-base font-bold rounded-xl shadow-md shadow-orange-100 transition-all"
                    >
                        {type === "Tiền chi" ? "Nhập khoản chi" : "Nhập khoản thu"}
                    </button>
                </div>

                {/* Right: Category grid */}
                <div className="lg:col-span-3">
                    <div className="bg-white rounded-xl border border-gray-200 overflow-hidden">
                        <div className="px-5 py-3 bg-gray-50 border-b border-gray-100">
                            <span className="text-xs font-semibold text-gray-500 uppercase tracking-wide">Danh mục</span>
                        </div>
                        <div className="p-4 grid grid-cols-3 sm:grid-cols-4 gap-3">
                            {categories.map((cat: any) => (
                                <button
                                    key={cat.label}
                                    onClick={() => !cat.isEdit && setSelectedCat(cat.label)}
                                    className={`flex flex-col items-center gap-2 p-4 rounded-xl border-2 transition-all text-center group ${selectedCat === cat.label && !cat.isEdit
                                        ? "border-orange-400 bg-orange-50 shadow-sm"
                                        : "border-gray-100 hover:border-gray-200 bg-white hover:bg-gray-50"
                                        }`}
                                >
                                    <div className={`w-10 h-10 rounded-full ${cat.bg} flex items-center justify-center`}>
                                        <cat.icon size={20} className={cat.color} strokeWidth={1.5} />
                                    </div>
                                    <span className="text-xs text-gray-600 leading-tight font-medium">{cat.label}</span>
                                </button>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </DashboardLayout>
    );
};

export default UserEntry;
