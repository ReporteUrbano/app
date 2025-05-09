import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./Cadastro.css";

const Cadastro = () => {
  const [nome, setNome] = useState("");
  const [cpf, setCpf] = useState("");
  const [cep, setCep] = useState("");
  const [genero, setGenero] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleCadastro = async (e) => {
    e.preventDefault();
    setError("");

    const response = await fetch("http://localhost:8081/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ nome, cpf, cep, genero }),
    });

    let data;
    try {
      data = await response.json();
    } catch (err) {
      setError("Erro inesperado do servidor.");
      return;
    }

    if (response.ok) {
      localStorage.setItem("userId", data.userId);
      localStorage.setItem("token", data.token);
      navigate("/dashboard");
    } else {
      setError(data.error || "Erro ao cadastrar. Tente novamente.");
    }
  };
  //   const data = await response.json();

  //   if (response.ok) {
  //     console.log("Cadastro realizado!");
  //     localStorage.setItem("userId", data.userId); // Armazena o id no localStorage
  //     localStorage.setItem("token", data.token); // Armazena o token no localStorage
  //     navigate("/dashboard"); // Redireciona para o dashboard após cadastro
  //   } else {
  //     setError("Erro ao cadastrar. Tente novamente.");
  //   }
  // };

  return (
    <div className="cadastro-container">
      <h1 className="logo">Cadastro - ReporteUrbano</h1>

      <form className="cadastro-form" onSubmit={handleCadastro}>
        <input
          type="text"
          placeholder="Nome Completo"
          value={nome}
          onChange={(e) => setNome(e.target.value)}
        />
        <input
          type="text"
          placeholder="CPF"
          value={cpf}
          onChange={(e) => setCpf(e.target.value)}
        />
        <input
          type="text"
          placeholder="CEP"
          value={cep}
          onChange={(e) => setCep(e.target.value)}
        />

        <div className="genero-group">
          <label>Gênero:</label>
          <div className="genero-options">
            <label>
              <input
                type="radio"
                value="Masculino"
                checked={genero === "Masculino"}
                onChange={(e) => setGenero(e.target.value)}
              />
              Masculino
            </label>
            <label>
              <input
                type="radio"
                value="Feminino"
                checked={genero === "Feminino"}
                onChange={(e) => setGenero(e.target.value)}
              />
              Feminino
            </label>
          </div>
        </div>

        <button type="submit">Cadastrar</button>
      </form>

      {error && <p className="error-message">{error}</p>}

      <p className="register-link">
        Já tem conta? <Link to="/">Entrar</Link>
      </p>
    </div>
  );
};

export default Cadastro;
