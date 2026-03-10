import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/auth/Login/Login';
import Register from './pages/auth/Register/Register';
import ForgotPassword from './pages/auth/ForgotPassword/ForgotPassword';
import AdminHome from './pages/admin/Home/AdminHome';
import UserEntry from './pages/user/Entry/UserEntry';
import UserCalendar from './pages/user/Calendar/UserCalendar';
import UserReport from './pages/user/Report/UserReport';
import UserBudget from './pages/user/Budget/UserBudget';
import './App.css';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/login" replace />} />

                {/* Auth routes */}
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/forgot-password" element={<ForgotPassword />} />

                {/* Admin routes */}
                <Route path="/admin/home" element={<AdminHome />} />

                {/* User routes */}
                <Route path="/user/home" element={<Navigate to="/user/entry/chi" replace />} />
                <Route path="/user/entry" element={<Navigate to="/user/entry/chi" replace />} />
                <Route path="/user/entry/chi" element={<UserEntry defaultType="Tiền chi" />} />
                <Route path="/user/entry/thu" element={<UserEntry defaultType="Tiền thu" />} />
                <Route path="/user/calendar" element={<UserCalendar />} />
                <Route path="/user/report" element={<UserReport />} />
                <Route path="/user/budget" element={<UserBudget />} />
            </Routes>
        </Router>
    );
}

export default App;
