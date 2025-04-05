import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const usuarioLogado = localStorage.getItem("usuario");
    if (!usuarioLogado) {
      navigate("/"); // Se não estiver logado, volta pro login
    }
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("usuario"); // Remove a sessão
    navigate("/"); // Redireciona para login
  };

  return (
    <div className="dashboard-container">
      <h1>Bem-vindo ao ReporteUrbano! 🚀</h1>
      <p>Aqui você poderá reportar problemas urbanos em breve.</p>

      <button onClick={handleLogout} className="logout-button">
        Sair
      </button>
    </div>
  );
};

export default Dashboard;
