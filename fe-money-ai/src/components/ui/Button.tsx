import React, { type ButtonHTMLAttributes } from 'react';
import { type LucideIcon } from 'lucide-react';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
    variant?: 'primary' | 'outline' | 'ghost';
    icon?: LucideIcon;
    iconPosition?: 'left' | 'right';
    fullWidth?: boolean;
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
    (
        {
            className = '',
            variant = 'primary',
            icon: Icon,
            iconPosition = 'left',
            children,
            fullWidth = false,
            ...props
        },
        ref
    ) => {
        const baseStyles =
            'inline-flex items-center justify-center rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#f5d57b] disabled:pointer-events-none disabled:opacity-50 h-12 px-4 py-2';

        const variants = {
            primary: 'bg-[#fcd34d] text-gray-900 hover:bg-[#fbbf24] font-semibold',
            outline: 'border border-[#e5e7eb] bg-white hover:bg-gray-50 text-gray-700 font-semibold',
            ghost: 'hover:bg-gray-100 hover:text-gray-900',
        };

        const widthStyle = fullWidth ? 'w-full' : '';

        return (
            <button
                ref={ref}
                className={`${baseStyles} ${variants[variant]} ${widthStyle} ${className}`}
                {...props}
            >
                {Icon && iconPosition === 'left' && <Icon className="mr-2 h-5 w-5" />}
                {children}
                {Icon && iconPosition === 'right' && <Icon className="ml-2 h-5 w-5" />}
            </button>
        );
    }
);
Button.displayName = 'Button';

export { Button };
