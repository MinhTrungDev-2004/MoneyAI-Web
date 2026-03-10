import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Mail, Lock } from "lucide-react";
import { Input } from "../../../components/ui/Input";
import { Button } from "../../../components/ui/Button";
import { Checkbox } from "../../../components/ui/Checkbox";
import { GoogleButton } from "../../../components/auth/GoogleButton";
import AuthLayout from "../../../components/auth/AuthLayout";
import AuthFormCard from "../../../components/auth/AuthFormCard";

const LoginIllustration: React.FC = () => (
    <div className="relative w-72 h-72 flex items-center justify-center">
        <div className="absolute inset-0 bg-blue-100 rounded-full opacity-60" />
        <div className="absolute -left-4 top-1/2 -translate-y-1/2 w-32 h-40 bg-purple-500 rounded-lg -rotate-12 shadow-lg" />
        <div className="absolute right-8 top-12 w-28 h-36 bg-blue-400 rounded-lg rotate-6 shadow-md" />
    </div>
);

function extractRole(data: any, token?: string): string | undefined {
    let role =
        data?.role || data?.data?.role || data?.user?.role || data?.data?.user?.role;

    if (token && !role) {
        try {
            const base64 = token.split(".")[1].replace(/-/g, "+").replace(/_/g, "/");
            const payload = JSON.parse(
                decodeURIComponent(
                    atob(base64)
                        .split("")
                        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
                        .join(""),
                ),
            );
            role = payload?.role || payload?.roles || payload?.Role || payload?.authorities;
            if (Array.isArray(role)) {
                role = role.find((r) => r.toLowerCase().includes("admin")) || role[0];
            }
        } catch {
        }
    }
    return role;
}

const Login: React.FC = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState("");

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        setIsLoading(true);

        try {
            const { data } = await axios.post("/public/auth/login", { email, password });

            const token =
                data?.accessToken || data?.access_token ||
                data?.data?.accessToken || data?.data?.token || data?.token;

            const role = extractRole(data, token);
            console.log("Login role:", role);

            if (typeof role === "string" && (role.toLowerCase() === "admin" || role.includes("ADMIN"))) {
                navigate("/admin/home");
            } else {
                navigate("/user/home");
            }
        } catch (err: any) {
            setError(
                err.response?.data?.message || err.message || "Có lỗi xảy ra khi kết nối đến máy chủ.",
            );
        } finally {
            setIsLoading(false);
        }
    };

    const footer = (
        <p className="text-sm text-gray-500">
            Bạn chưa có tài khoản?{" "}
            <button
                type="button"
                onClick={() => navigate("/register")}
                className="font-semibold text-blue-600 hover:text-blue-500 transition-colors"
            >
                Đăng ký
            </button>
        </p>
    );

    return (
        <AuthLayout
            illustration={<LoginIllustration />}
            caption="Chúng tôi mang đến cho bạn giá trị tài chính bền vững thông qua nền tảng quản lý chất lượng cao và các tính năng AI đột phá."
        >
            <AuthFormCard
                title="Đăng nhập"
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
                    </div>

                    <div className="flex items-center justify-between">
                        <Checkbox label="Remember me" />
                        <button
                            type="button"
                            onClick={() => navigate("/forgot-password")}
                            className="text-sm font-semibold text-blue-600 hover:text-blue-500 transition-colors"
                        >
                            Quên mật khẩu?
                        </button>
                    </div>

                    <div className="space-y-4 pt-2">
                        <Button type="submit" fullWidth disabled={isLoading}>
                            {isLoading ? "Đang xử lý..." : "ĐĂNG NHẬP"}
                        </Button>
                        <GoogleButton disabled={isLoading} />
                    </div>
                </form>
            </AuthFormCard>
        </AuthLayout>
    );
};

export default Login;
