import React, { useState } from 'react';
import { Routes, Route, useNavigate, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import AvaliacaoPage from './pages/AvaliacaoPage';

export default function App() {
  // O token agora é o nosso estado global de autenticação
  const [token, setToken] = useState(localStorage.getItem('jwtToken'));
  const navigate = useNavigate();

  const handleLoginSuccess = (newToken) => {
    localStorage.setItem('jwtToken', newToken);
    setToken(newToken);
    navigate('/dashboard'); // Navega para o dashboard após o login
  };

  const handleLogout = () => {
    localStorage.removeItem('jwtToken');
    setToken(null);
    // O Navigate no return já cuidará do redirecionamento
  };

  return (
    <Routes>
      <Route 
        path="/login" 
        element={
          !token ? <LoginPage onLoginSuccess={handleLoginSuccess} /> : <Navigate to="/dashboard" />
        } 
      />
      <Route 
        path="/dashboard" 
        element={
          token ? <DashboardPage token={token} onLogout={handleLogout} /> : <Navigate to="/login" />
        } 
      />
      <Route 
        path="/avaliacao/:id" 
        element={
          token ? <AvaliacaoPage token={token} /> : <Navigate to="/login" />
        } 
      />
      <Route 
        path="*"
        element={<Navigate to={token ? "/dashboard" : "/login"} />}
      />
    </Routes>
  );
}