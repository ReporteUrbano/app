import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./NovaOcorrencia.css"; // Importando o arquivo de estilo

const NovaOcorrencia = () => {
  const navigate = useNavigate();

  const [tituloProblema, setTituloProblema] = useState("");
  const [descricao, setDescricao] = useState("");
  const [localizacao, setLocalizacao] = useState("");
  const [foto, setFoto] = useState(""); // Foto em Base64
  const [mensagem, setMensagem] = useState("");
  const [respostaIA, setRespostaIA] = useState("");

  // Função para converter imagem em Base64
  const handleFileChange = (e) => {
    const file = e.target.files[0]; // Pegue o primeiro arquivo
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setFoto(reader.result); // Armazena o Base64 da imagem
      };
      reader.readAsDataURL(file); // Converte o arquivo para Base64
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const novaOcorrencia = {
      tituloProblema,
      descricao,
      localizacao,
      foto,
    };

    try {
      const response = await axios.post(
        "http://localhost:8081/api/ocorrencias",
        novaOcorrencia,
        { withCredentials: true }
      );

      // Verifica se a resposta tem a descrição da IA
      const descricaoIA =
        response.data?.descricaoIa || 
        response.data?.ocorrencia?.descricaoIa || 
        "";

      setRespostaIA(descricaoIA);
      setMensagem("Ocorrência enviada com sucesso!");

      // Limpa campos
      setTituloProblema("");
      setDescricao("");
      setLocalizacao("");
      setFoto("");
    } catch (error) {
      console.error("Erro ao criar ocorrência:", error);
      setMensagem("Erro ao criar ocorrência.");
    }
  };

  const handleBackToDashboard = () => {
    navigate("/dashboard");
  };

  return (
    <div className="nova-ocorrencia-container">
      <h2 className="header">Criar Nova Ocorrência</h2>

      <form onSubmit={handleSubmit} className="form">
        <input
          type="text"
          placeholder="Título do problema"
          value={tituloProblema}
          onChange={(e) => setTituloProblema(e.target.value)}
          className="input"
        />
        <textarea
          placeholder="Descrição do problema"
          value={descricao}
          onChange={(e) => setDescricao(e.target.value)}
          className="input"
        />
        <input
          type="text"
          placeholder="Localização"
          value={localizacao}
          onChange={(e) => setLocalizacao(e.target.value)}
          className="input"
        />
        <input
          type="file"
          accept="image/*"
          onChange={handleFileChange}
          className="inputFile"
        />
        <button type="submit" className="button">Enviar Ocorrência</button>
      </form>

      {mensagem && <p className="message">{mensagem}</p>}
      {respostaIA && (
        <div className="iaResponseContainer">
          <h3 className="iaHeader">Orientação da IA:</h3>
          <p className="iaText">{respostaIA}</p>
        </div>
      )}

      {respostaIA && (
        <button 
          onClick={handleBackToDashboard} 
          className="backButton"
        >
          Voltar para o Início
        </button>
      )}
    </div>
  );
};

export default NovaOcorrencia;
