import React, { useState, type InputHTMLAttributes } from 'react';
import { type LucideIcon, Eye, EyeOff } from 'lucide-react';

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
    icon?: LucideIcon;
}

const Input = React.forwardRef<HTMLInputElement, InputProps>(
    ({ className = '', icon: Icon, type, ...props }, ref) => {
        const [showPassword, setShowPassword] = useState(false);
        const isPassword = type === 'password';
        const inputType = isPassword ? (showPassword ? 'text' : 'password') : type;

        return (
            <div className="relative w-full">
                <input
                    type={inputType}
                    className={`flex h-12 w-full rounded-md border border-[#c4c4c4] bg-white px-4 py-2 text-sm ring-offset-white file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-gray-400 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#f5d57b] disabled:cursor-not-allowed disabled:opacity-50 pr-10 ${className}`}
                    ref={ref}
                    {...props}
                />

                {/* Icon bên phải: eye toggle nếu là password, icon thường nếu không phải */}
                <div className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400">
                    {isPassword ? (
                        <button
                            type="button"
                            onClick={() => setShowPassword((prev) => !prev)}
                            className="flex items-center justify-center text-gray-400 hover:text-gray-600 transition-colors focus:outline-none"
                            tabIndex={-1}
                            aria-label={showPassword ? 'Ẩn mật khẩu' : 'Hiện mật khẩu'}
                        >
                            {showPassword
                                ? <EyeOff size={20} strokeWidth={1.5} />
                                : <Eye size={20} strokeWidth={1.5} />
                            }
                        </button>
                    ) : Icon ? (
                        <div className="pointer-events-none">
                            <Icon size={20} strokeWidth={1.5} />
                        </div>
                    ) : null}
                </div>
            </div>
        );
    }
);
Input.displayName = 'Input';

export { Input };
