import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./Login.css";

const Login = () => {  
  const [nome, setNome] = useState("");
  const [cpf, setCpf] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
  
    const response = await fetch("http://192.168.5.116:8081/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ cpf, nome }),
    });
  
    const data = await response.json();
  
    if (response.ok) {
      console.log("Usuário logado:", data);
      localStorage.setItem("userId", data.userId); // Armazena o id no localStorage
      localStorage.setItem("token", data.token); // Armazena o token no localStorage
      navigate("/dashboard"); // Redireciona para o dashboard após login
    } else {
      setError(data.error || "Erro desconhecido ao fazer login");
    }
  };
  

  return (
    <div className="login-container">
      <h1 className="logo">ReporteUrbano</h1>

      <form className="login-form" onSubmit={handleLogin}>
        <input
          type="text"
          placeholder="Nome"
          value={nome}
          onChange={(e) => setNome(e.target.value)}
        />
        <input
          type="text"
          placeholder="CPF"
          value={cpf}
          onChange={(e) => setCpf(e.target.value)}
        />
        <button type="submit">Entrar</button>
      </form>

      {error && <p className="error-message">{error}</p>}

      <p className="register-link">
        Não tem conta? <Link to="/cadastro">Cadastre-se</Link>
      </p>
    </div>
  );
};

export default Login;
