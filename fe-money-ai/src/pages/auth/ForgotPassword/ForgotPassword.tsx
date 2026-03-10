import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Mail } from "lucide-react";
import { Input } from "../../../components/ui/Input";
import { Button } from "../../../components/ui/Button";
import AuthLayout from "../../../components/auth/AuthLayout";
import AuthFormCard from "../../../components/auth/AuthFormCard";

const ForgotIllustration: React.FC = () => (
    <div className="relative w-72 h-72 flex items-center justify-center">
        <div className="absolute bottom-0 left-1/2 -translate-x-1/2 w-64 h-24 bg-gray-200 rounded-full opacity-60" />

        <div className="absolute left-8 top-8 w-28 h-40 bg-gray-800 rounded-2xl shadow-xl flex flex-col items-center justify-center gap-3 p-3">
            <div className="w-10 h-10 rounded-full bg-teal-400 flex items-center justify-center">
                <svg viewBox="0 0 24 24" className="w-6 h-6 text-white" fill="currentColor">
                    <path d="M12 12c2.7 0 4.8-2.1 4.8-4.8S14.7 2.4 12 2.4 7.2 4.5 7.2 7.2 9.3 12 12 12zm0 2.4c-3.2 0-9.6 1.6-9.6 4.8v2.4h19.2v-2.4c0-3.2-6.4-4.8-9.6-4.8z" />
                </svg>
            </div>
            <div className="w-full">
                <p className="text-gray-400 text-xs mb-1 text-center">Mật khẩu</p>
                <div className="w-full h-2 bg-teal-400 rounded-full" />
            </div>
            <div className="flex gap-1">
                <div className="w-2 h-2 rounded-full bg-gray-500" />
                <div className="w-2 h-2 rounded-full bg-gray-500" />
                <div className="w-2 h-2 rounded-full bg-gray-500" />
            </div>
        </div>

        <div className="absolute right-4 bottom-6 flex flex-col items-center">
            <div className="relative">
                <div className="w-12 h-12 rounded-full bg-[#f4a886] border-2 border-[#e8956d]" />
                <div className="absolute -top-2 -left-1 w-14 h-8 bg-gray-800 rounded-t-full" />
            </div>
            <div className="w-14 bg-teal-400 rounded-t-2xl -mt-1 flex items-center justify-center relative" style={{ height: "4.5rem" }}>
                <div className="absolute -right-5 -top-3 w-7 h-12 bg-[#f4a886] rounded-full rotate-[25deg]" />
            </div>
            <div className="flex gap-1">
                <div className="w-6 h-10 bg-gray-700 rounded-b-lg" />
                <div className="w-6 h-10 bg-gray-700 rounded-b-lg" />
            </div>
        </div>

        <div className="absolute left-0 bottom-4">
            <svg width="36" height="46" viewBox="0 0 40 50" fill="none">
                <path d="M10 45 Q5 20 30 8 Q25 25 10 45Z" fill="#4ade80" opacity="0.8" />
                <path d="M15 45 Q20 25 35 15 Q28 30 15 45Z" fill="#22c55e" opacity="0.7" />
            </svg>
        </div>
        <div className="absolute right-0 bottom-8">
            <svg width="32" height="42" viewBox="0 0 35 45" fill="none">
                <path d="M25 40 Q30 18 8 5 Q14 22 25 40Z" fill="#4ade80" opacity="0.8" />
                <path d="M20 40 Q15 22 5 12 Q12 28 20 40Z" fill="#22c55e" opacity="0.7" />
            </svg>
        </div>
        <span className="absolute top-4 right-16 text-teal-400 font-bold text-2xl select-none animate-pulse">?</span>
        <span className="absolute top-10 right-24 text-teal-300 font-bold text-lg select-none opacity-70">?</span>
    </div>
);

const ForgotPassword: React.FC = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        setIsLoading(true);

        try {
            await axios.post("/public/auth/forgot-password", { email });
            setSuccess(true);
        } catch (err: any) {
            setError(
                err.response?.data?.message || err.message || "Có lỗi xảy ra. Vui lòng thử lại.",
            );
        } finally {
            setIsLoading(false);
        }
    };

    const footer = (
        <button
            type="button"
            onClick={() => navigate("/login")}
            className="text-sm font-semibold text-blue-600 hover:text-blue-500 transition-colors"
        >
            Quay lại đăng nhập
        </button>
    );

    return (
        <AuthLayout
            illustration={<ForgotIllustration />}
            caption="Đừng lo! Chúng tôi sẽ giúp bạn lấy lại quyền truy cập tài khoản."
        >
            <AuthFormCard
                title="Đặt lại mật khẩu"
                subtitle=""
                footer={footer}
            >
                {success ? (
                    <div className="p-4 text-sm text-teal-800 bg-teal-50 rounded-md border border-teal-200 text-center mt-6">
                        Đã gửi email đặt lại mật khẩu! Vui lòng kiểm tra hộp thư của bạn.
                    </div>
                ) : (
                    <form onSubmit={handleSubmit} className="space-y-6 mt-8">
                        {error && (
                            <div className="p-3 text-sm text-red-600 bg-red-50 rounded-md border border-red-100">
                                {error}
                            </div>
                        )}
                        <Input
                            type="email"
                            placeholder="Email"
                            icon={Mail}
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                        <Button
                            type="submit"
                            fullWidth
                            disabled={isLoading}
                            className="bg-yellow-400 hover:bg-yellow-500 text-gray-900 border-0"
                        >
                            {isLoading ? "Đang gửi..." : "TIẾP THEO"}
                        </Button>
                    </form>
                )}
            </AuthFormCard>
        </AuthLayout>
    );
};

export default ForgotPassword;
