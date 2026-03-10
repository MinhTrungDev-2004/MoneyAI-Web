import React from "react";

interface AuthLayoutProps {
    illustration: React.ReactNode;
    caption?: string;
    children: React.ReactNode;
    leftBg?: string;
}

const AuthLayout: React.FC<AuthLayoutProps> = ({
    illustration,
    caption,
    children,
    leftBg = "bg-gray-50",
}) => {
    return (
        <div className="flex min-h-screen bg-white">
            {/* Cột trái: Illustration */}
            <div
                className={`hidden lg:flex lg:w-1/2 flex-col items-center justify-center p-12 ${leftBg}`}
            >
                <div className="max-w-md w-full flex flex-col items-center text-center gap-8">
                    <div className="w-full flex items-center justify-center">
                        {illustration}
                    </div>
                    {caption && (
                        <p className="text-gray-600 text-base font-medium px-4">
                            {caption}
                        </p>
                    )}
                </div>
            </div>

            {/* Cột phải: Form */}
            <div className="w-full lg:w-1/2 flex flex-col justify-center items-center p-8 sm:p-12 lg:p-24">
                {children}
            </div>
        </div>
    );
};

export default AuthLayout;
