import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Mail, Lock } from "lucide-react";
import { Input } from "../../../components/ui/Input";
import { Button } from "../../../components/ui/Button";
import { GoogleButton } from "../../../components/auth/GoogleButton";
import AuthLayout from "../../../components/auth/AuthLayout";
import AuthFormCard from "../../../components/auth/AuthFormCard";

const RegisterIllustration: React.FC = () => (
    <div className="relative w-72 h-72 flex items-center justify-center">
        <div className="absolute inset-0 bg-blue-100 rounded-full opacity-60" />

        <div className="absolute -left-8 top-12 -rotate-12 w-28 h-40 bg-purple-500 rounded-xl shadow-lg flex flex-col p-2">
            <div className="w-full h-20 bg-purple-400 rounded-md mb-2" />
            <div className="space-y-1">
                <div className="w-3/4 h-2 bg-purple-300 rounded" />
                <div className="w-1/2 h-2 bg-purple-300 rounded" />
            </div>
        </div>

        <div className="absolute top-4 w-28 h-40 bg-blue-500 rounded-xl shadow-lg flex flex-col p-2 rotate-6 z-10">
            <div className="w-full h-20 bg-blue-400 rounded-md mb-2" />
            <div className="space-y-1">
                <div className="w-3/4 h-2 bg-blue-300 rounded" />
                <div className="w-1/2 h-2 bg-blue-300 rounded" />
            </div>
        </div>

        <div className="absolute z-20 right-8 top-1/2 -translate-y-1/2 flex flex-col items-center">
            <div className="w-10 h-10 bg-orange-200 rounded-full mb-1" />
            <div className="w-16 h-16 bg-blue-600 rounded-t-xl" />
            <div className="w-16 h-8 bg-yellow-400 rounded-b-md" />
        </div>

        <div className="absolute top-10 left-10 w-3 h-3 bg-yellow-400 rounded-full" />
        <div className="absolute bottom-16 right-20 w-4 h-4 bg-purple-300 rounded-full" />
    </div>
);

const Register: React.FC = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState("");

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError("");

        if (password !== confirmPassword) {
            setError("Mật khẩu xác nhận không khớp.");
            return;
        }

        setIsLoading(true);

        try {
            await axios.post("http://localhost:8081/public/auth/register", {
                email,
                password,
            });
            alert("Đăng ký thành công!");
            navigate("/login");
        } catch (err: any) {
            setError(
                err.response?.data?.message || err.message || "Có lỗi xảy ra khi đăng ký.",
            );
        } finally {
            setIsLoading(false);
        }
    };

    const footer = (
        <p className="text-sm text-gray-500">
            Đã có tài khoản?{" "}
            <button
                type="button"
                onClick={() => navigate("/login")}
                className="font-semibold text-blue-600 hover:text-blue-500 transition-colors"
            >
                Đăng nhập
            </button>
        </p>
    );

    return (
        <AuthLayout
            illustration={<RegisterIllustration />}
            caption="The best of luxury brand values, high quality products, and innovative services."
        >
            <AuthFormCard
                title="Đăng ký"
                subtitle=""
                footer={footer}
            >
                <form onSubmit={handleSubmit} className="space-y-6 mt-8">
                    {error && (
                        <div className="p-3 text-sm text-red-600 bg-red-50 rounded-md border border-red-100">
                            {error}
                        </div>
                    )}

                    <div className="space-y-4">
                        <Input
                            type="email"
                            placeholder="Email"
                            icon={Mail}
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                        <Input
                            type="password"
                            placeholder="Mật khẩu"
                            icon={Lock}
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                        <Input
                            type="password"
                            placeholder="Xác nhận mật khẩu"
                            icon={Lock}
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </div>

                    <div className="space-y-4 pt-2">
                        <Button
                            type="submit"
                            fullWidth
                            disabled={isLoading}
                            className="bg-yellow-400 hover:bg-yellow-500 text-gray-900 border-0"
                        >
                            {isLoading ? "Đang xử lý..." : "ĐĂNG KÝ"}
                        </Button>
                        <GoogleButton disabled={isLoading} />
                    </div>
                </form>
            </AuthFormCard>
        </AuthLayout>
    );
};

export default Register;
