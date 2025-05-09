import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { MapContainer, TileLayer, Marker, useMapEvents, Popup } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import "./NovaOcorrencia.css";

// Corrigindo o ícone do Leaflet
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
});

const ClickableMap = ({ setLocalizacao, userPosition }) => {
  useMapEvents({
    click(e) {
      const coords = `${e.latlng.lat},${e.latlng.lng}`;
      setLocalizacao(coords);
    }
  });

  return userPosition && (
    <Marker position={userPosition}>
      <Popup>Você está aqui</Popup>
    </Marker>
  );
};

const NovaOcorrencia = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const [userId, setUserId] = useState(localStorage.getItem("userId"));
  const [tituloProblema, setTituloProblema] = useState("");
  const [descricao, setDescricao] = useState("");
  const [localizacao, setLocalizacao] = useState(""); // Salva como "lat,lng"
  const [foto, setFoto] = useState("");
  const [mensagem, setMensagem] = useState("");
  const [respostaIA, setRespostaIA] = useState("");
  const [userPosition, setUserPosition] = useState(null);

  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          setUserPosition([latitude, longitude]);
        },
        (error) => console.error("Erro ao obter localização:", error),
        { enableHighAccuracy: true, timeout: 10000 }
      );
    }
  }, []);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => setFoto(reader.result);
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const novaOcorrencia = {
      tituloProblema,
      descricao,
      localizacao,
      foto,
      userId
    };

    try {
      const response = await axios.post(
        "http://localhost:8081/api/ocorrencias",
        novaOcorrencia,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      const descricaoIA = response.data?.descricaoIa || response.data?.ocorrencia?.descricaoIa || "";
      setRespostaIA(descricaoIA);
      setMensagem("Ocorrência enviada com sucesso!");
      setTituloProblema("");
      setDescricao("");
      setLocalizacao("");
      setFoto("");
    } catch (error) {
      console.error("Erro ao criar ocorrência:", error);
      setMensagem("Erro ao criar ocorrência.");
    }
  };

  return (
    <div className="nova-ocorrencia-container">
      <h2>Criar Nova Ocorrência</h2>

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

        <div style={{ height: "300px", marginBottom: "10px" }}>
          {userPosition && (
            <MapContainer center={userPosition} zoom={15} style={{ height: "100%", width: "100%" }}>
              <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
              <ClickableMap setLocalizacao={setLocalizacao} userPosition={userPosition} />
              {localizacao && (
                <Marker position={localizacao.split(',').map(Number)}>
                  <Popup>Local selecionado</Popup>
                </Marker>
              )}
            </MapContainer>
          )}
        </div>

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
        <>
          <h3>Orientação da IA:</h3>
          <p>{respostaIA}</p>
          <button onClick={() => navigate("/dashboard")} className="backButton">
            Voltar para o Início
          </button>
        </>
      )}
    </div>
  );
};

export default NovaOcorrencia;
