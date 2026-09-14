import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { ThemeProvider } from './context/ThemeContext';
import Navbar from './components/Navbar';
import Sidebar from './components/Sidebar';

// Pages
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Catalog from './pages/Catalog';
import CirculationDesk from './pages/CirculationDesk';
import MyLoans from './pages/MyLoans';
import Fines from './pages/Fines';
import Reservations from './pages/Reservations';
import Reports from './pages/Reports';
import Profile from './pages/Profile';

const ProtectedRoute = ({ children, staffOnly = false }) => {
  const { user, loading, isLibrarian } = useAuth();
  if (loading) return null;
  if (!user) return <Navigate to="/login" replace />;
  if (staffOnly && !isLibrarian) return <Navigate to="/" replace />;
  return children;
};

const MainLayout = ({ children }) => {
  const { user } = useAuth();
  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col font-sans selection:bg-indigo-500 selection:text-white">
      <Navbar />
      <div className="flex-1 flex max-w-7xl w-full mx-auto">
        {user && <Sidebar />}
        <main className="flex-1 p-4 sm:p-6 lg:p-8 overflow-y-auto custom-scrollbar">
          {children}
        </main>
      </div>
    </div>
  );
};

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <Router>
          <MainLayout>
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
              <Route path="/catalog" element={<ProtectedRoute><Catalog /></ProtectedRoute>} />
              <Route path="/circulation-desk" element={<ProtectedRoute staffOnly><CirculationDesk /></ProtectedRoute>} />
              <Route path="/my-loans" element={<ProtectedRoute><MyLoans /></ProtectedRoute>} />
              <Route path="/fines" element={<ProtectedRoute><Fines /></ProtectedRoute>} />
              <Route path="/reservations" element={<ProtectedRoute><Reservations /></ProtectedRoute>} />
              <Route path="/reports" element={<ProtectedRoute staffOnly><Reports /></ProtectedRoute>} />
              <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </MainLayout>
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
