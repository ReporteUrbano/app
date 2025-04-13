import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
  const navigate = useNavigate();
  const [ocorrencias, setOcorrencias] = useState([]);

  useEffect(() => {
    const idUsuarioLogado = localStorage.getItem("userId");
    if (!idUsuarioLogado) {
      navigate("/");
    } else {
      fetchOcorrencias();
    }
  }, [navigate]);

  const fetchOcorrencias = async () => {
    try {
      const response = await axios.get("http://localhost:8081/api/ocorrencias/all", {
        withCredentials: true, // importante para enviar o cookie com o JWT
      });
      setOcorrencias(response.data);
    } catch (error) {
      console.error("Erro ao buscar ocorrências:", error);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("userId");
    navigate("/");
  };

  return (
    <div className="dashboard-container">
      <h1>Bem-vindo ao ReporteUrbano! 🚀</h1>
      <p>Essas são suas ocorrências cadastradas:</p>

      {ocorrencias.length === 0 ? (
        <p>Você ainda não cadastrou nenhuma ocorrência.</p>
      ) : (
<ul>
  {ocorrencias.map((ocorrencia) => (
    <li key={ocorrencia.id}>
      <strong>Título:</strong> {ocorrencia.tituloProblema} <br />
      <strong>Descrição:</strong> {ocorrencia.descricao} <br />
      <strong>Localização:</strong> {ocorrencia.localizacao} <br />
      {ocorrencia.foto && (
        <img
          src={ocorrencia.foto}
          alt="Foto da ocorrência"
          style={{ width: "200px", marginTop: "10px" }}
        />
      )}
      <hr />
    </li>
  ))}
</ul>

      )}

      <button onClick={() => navigate("/nova-ocorrencia")}>
        Nova Ocorrência
      </button>

      <button onClick={handleLogout} className="logout-button">
        Sair
      </button>
    </div>
  );
};

export default Dashboard;
