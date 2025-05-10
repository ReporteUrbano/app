import React, { useEffect, useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import axios from 'axios';
import './MapPage.css';
import { useNavigate } from "react-router-dom";


// Corrige o bug do ícone padrão do Leaflet
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
});

function LocationMarkerWithOcorrencias({ somenteMinhas, categoriaFiltro }) {
  const navigate = useNavigate();
  const [position, setPosition] = useState(null);
  const [ocorrencias, setOcorrencias] = useState([]);
  const map = useMap();
  const token = localStorage.getItem("token");
  const idUsuarioLogado = parseInt(localStorage.getItem("userId"));

  useEffect(() => {
    map.locate({
      setView: true,
      maxZoom: 18,
      enableHighAccuracy: true,
      timeout: 10000,
    });

    map.on("locationfound", (e) => {
      setPosition(e.latlng);
    });

    map.on("locationerror", (e) => {
      alert("Erro ao obter localização: " + e.message);
    });
  }, [map]);

  useEffect(() => {
    const fetchOcorrencias = async () => {
      try {
        const response = await axios.get("http://localhost:8081/api/ocorrencias", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setOcorrencias(response.data);
        console.log("UserId : " + idUsuarioLogado);
        console.log("Ocorrências:", response.data);
      } catch (error) {
        console.error("Erro ao buscar ocorrências:", error);
      }
    };

    fetchOcorrencias();
  }, [token]);

  // Filtragem das ocorrências
  const ocorrenciasFiltradas = ocorrencias.filter((ocorrencia) => {
    const pertenceAoUsuario = ocorrencia.idUsuario === parseInt(idUsuarioLogado);
    const categoriaCorresponde = categoriaFiltro === "" || ocorrencia.categoria === categoriaFiltro;

    return (!somenteMinhas || pertenceAoUsuario) && categoriaCorresponde;
  });

  return (
    <>
      {position && (
        <Marker position={position}>
          <Popup>
            Você está aqui: {position.lat}, {position.lng}
            <br />
            <button onClick={() => navigate("/dashboard")}>Dashboard</button>
          </Popup>
        </Marker>
      )}

      {ocorrenciasFiltradas.map((ocorrencia, index) => {
        const match = ocorrencia.localizacao?.match(/-?\d+\.\d+/g);
        if (!match || match.length < 2) return null;

        const lat = parseFloat(match[0]);
        const lng = parseFloat(match[1]);

        return (
          <Marker key={index} position={[lat, lng]}>
            <Popup>
              <strong>{ocorrencia.tituloProblema}</strong><br />
              {ocorrencia.descricao}<br />
              <em>Categoria: {ocorrencia.categoria}</em>
            </Popup>
          </Marker>
        );
      })}
    </>
  );
}

export default function MapPage() {
  const [somenteMinhas, setSomenteMinhas] = useState(false);
  const [categoriaFiltro, setCategoriaFiltro] = useState("");

  return (
    <div className="map-page" style={{ height: '100vh', width: '100vw', margin: 0, padding: 0 }}>
      <div className="filtros-container">
        <label>
          <input
            type="checkbox"
            checked={somenteMinhas}
            onChange={() => setSomenteMinhas(!somenteMinhas)}
          />
          {' '}Mostrar somente minhas ocorrências
        </label>

        <label>
          Categoria:
          <select
            value={categoriaFiltro}
            onChange={(e) => setCategoriaFiltro(e.target.value)}
          >
            <option value="">Todas</option>
            <option value="Trânsito e Acidentes">Trânsito e Acidentes</option>
            <option value="Saúde Pública">Saúde Pública</option>
            <option value="Iluminação Pública">Iluminação Pública</option>
            <option value="Buracos e Pavimentação">Buracos e Pavimentação</option>
            <option value="Coleta de Lixo e Entulho">Coleta de Lixo e Entulho</option>
            <option value="Água e Esgoto">Água e Esgoto</option>
            <option value="Segurança Pública">Segurança Pública</option>
            <option value="Poluição e Meio Ambiente">Poluição e Meio Ambiente</option>
            <option value="Animais na Via Pública">Animais na Via Pública</option>
            <option value="Infraestrutura Urbana">Infraestrutura Urbana</option>
          </select>
        </label>
      </div>

      <MapContainer center={[-23.65, -52.60]} zoom={13} style={{ height: '100vh', width: '100%' }}>
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution="© OpenStreetMap"
          maxZoom={19}
        />
        <LocationMarkerWithOcorrencias
          somenteMinhas={somenteMinhas}
          categoriaFiltro={categoriaFiltro}
        />
      </MapContainer>
    </div>
  );
}