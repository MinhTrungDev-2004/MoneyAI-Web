import React, { useState } from "react";
import Sidebar, { type NavItem } from "./Sidebar";
import Header from "./Header";

interface DashboardLayoutProps {
    children: React.ReactNode;
    navItems: NavItem[];
    pageTitle?: string;
    userName?: string;
    brandName?: string;
}

/**
 * DashboardLayout — layout chính cho các trang admin và user.
 * Bao gồm: Sidebar (trái) + Header (trên) + nội dung (phải/dưới).
 */
const DashboardLayout: React.FC<DashboardLayoutProps> = ({
    children,
    navItems,
    pageTitle,
    userName,
    brandName,
}) => {
    const [collapsed, setCollapsed] = useState(false);

    return (
        <div className="flex h-screen bg-[#f5f5f5] overflow-hidden">
            {/* Sidebar */}
            <Sidebar
                navItems={navItems}
                collapsed={collapsed}
                onToggle={() => setCollapsed(!collapsed)}
                brandName={brandName}
            />

            {/* Khu vực phải: Header + nội dung */}
            <div className="flex flex-col flex-1 overflow-hidden">
                <Header
                    title={pageTitle}
                    userName={userName}
                    onToggleSidebar={() => setCollapsed(!collapsed)}
                />

                {/* Main content */}
                <main className="flex-1 overflow-y-auto p-6">
                    {children}
                </main>
            </div>
        </div>
    );
};

export default DashboardLayout;
