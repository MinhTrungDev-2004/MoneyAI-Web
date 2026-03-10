import React from "react";

interface AuthFormCardProps {
    title: string;
    subtitle?: string;
    children: React.ReactNode;
    footer?: React.ReactNode;
    className?: string;
}

const AuthFormCard: React.FC<AuthFormCardProps> = ({
    title,
    subtitle,
    children,
    footer,
    className = "",
}) => {
    return (
        <div className={`w-full max-w-md space-y-8 ${className}`}>
            <div className="text-center">
                <h1 className="text-2xl font-bold text-gray-900 mb-2">{title}</h1>
                {subtitle && (
                    <p className="text-gray-500 text-sm">{subtitle}</p>
                )}
            </div>

            {children}

            {footer && (
                <div className="text-center pt-4">{footer}</div>
            )}
        </div>
    );
};

export default AuthFormCard;
