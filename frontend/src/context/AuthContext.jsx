import React, { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const currentUser = authService.getCurrentUser();
    if (currentUser) {
      setUser(currentUser);
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    const data = await authService.login(email, password);
    setUser(data);
    return data;
  };

  const register = async (userData) => {
    const data = await authService.register(userData);
    setUser(data);
    return data;
  };

  const logout = () => {
    authService.logout();
    setUser(null);
  };

  const role = user?.role || '';
  const isAdmin = role === 'ROLE_ADMIN';
  const isLibrarian = role === 'ROLE_LIBRARIAN' || role === 'ROLE_ADMIN';
  const isMember = role === 'ROLE_MEMBER';

  return (
    <AuthContext.Provider value={{ user, setUser, loading, login, register, logout, isAdmin, isLibrarian, isMember }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
