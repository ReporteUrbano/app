import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

const Login = () => {
  const [nome, setNome] = useState("");
  const [cpf, setCpf] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    const response = await fetch("http://localhost:8081/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ cpf, nome }),
    });

    const data = await response.json();

    if (response.ok) {
      console.log("Usuário logado:", data);
      localStorage.setItem("userId", data.userId);
      localStorage.setItem("token", data.token);
      navigate("/dashboard");
    } else {
      setError(data.error || "Erro desconhecido ao fazer login");
    }
  };

  return (
    <div className="container d-flex flex-column justify-content-center align-items-center min-vh-100 bg-light px-4">
      <h1 className="mb-4 text-success text-center">ReporteUrbano</h1>

      <form className="w-100" style={{ maxWidth: "400px" }} onSubmit={handleLogin}>
        <div className="mb-3">
          <input
            type="text"
            className="form-control"
            placeholder="Nome"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
          />
        </div>

        <div className="mb-3">
          <input
            type="text"
            className="form-control"
            placeholder="CPF"
            value={cpf}
            onChange={(e) => setCpf(e.target.value)}
            required
          />
        </div>

        <button type="submit" className="btn btn-success w-100">
          Entrar
        </button>
      </form>

      {error && <div className="alert alert-danger mt-3 w-100 text-center">{error}</div>}

      <p className="mt-3 text-muted text-center">
        Não tem conta?{" "}
        <Link to="/cadastro" className="text-success fw-bold text-decoration-none">
          Cadastre-se
        </Link>
      </p>
    </div>
  );
};

export default Login;
