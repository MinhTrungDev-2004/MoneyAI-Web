import React, { type InputHTMLAttributes } from 'react';

interface CheckboxProps extends InputHTMLAttributes<HTMLInputElement> {
    label: string;
}

const Checkbox = React.forwardRef<HTMLInputElement, CheckboxProps>(
    ({ className = '', label, id, ...props }, ref) => {
        const defaultId = React.useId();
        const inputId = id || defaultId;

        return (
            <div className="flex items-center space-x-2">
                <input
                    type="checkbox"
                    id={inputId}
                    ref={ref}
                    className={`peer h-4 w-4 shrink-0 rounded-sm border border-gray-300 ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#f5d57b] focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 data-[state=checked]:bg-[#fcd34d] data-[state=checked]:text-white ${className}`}
                    {...props}
                />
                <label
                    htmlFor={inputId}
                    className="text-sm font-medium leading-none text-gray-500 peer-disabled:cursor-not-allowed peer-disabled:opacity-70 cursor-pointer"
                >
                    {label}
                </label>
            </div>
        );
    }
);
Checkbox.displayName = 'Checkbox';

export { Checkbox };
