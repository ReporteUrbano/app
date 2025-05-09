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

function LocationMarkerWithOcorrencias() {
  const navigate = useNavigate();
  const [position, setPosition] = useState(null);
  const [ocorrencias, setOcorrencias] = useState([]);
  const map = useMap();
  const token = localStorage.getItem("token");
  const idUsuarioLogado = localStorage.getItem("userId");

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
        const response = await axios.get("http://localhost:8081/api/ocorrencias/all/" + idUsuarioLogado, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setOcorrencias(response.data);
      } catch (error) {
        console.error("Erro ao buscar ocorrências:", error);
      }
    };

    fetchOcorrencias();
  }, [idUsuarioLogado, token]);

  return (
    <>
      {position && (
        <Marker position={position}>
          <Popup>Você está aqui: {position.lat}, {position.lng}<button onClick={() => navigate("/dashboard")}>Dashboard</button> </Popup>
        </Marker>
      )}

      {ocorrencias.map((ocorrencia, index) => {
        // Supondo que o campo localização seja uma string como "LatLng(-23.657689, -52.605386)"
        const match = ocorrencia.localizacao?.match(/-?\d+\.\d+/g);
        if (!match || match.length < 2) return null;

        const lat = parseFloat(match[0]);
        const lng = parseFloat(match[1]);

        return (
          <Marker key={index} position={[lat, lng]}>
            <Popup>
              <strong>{ocorrencia.tituloProblema}</strong><br />
              {ocorrencia.descricao}
            </Popup>
          </Marker>
        );
      })}
    </>
  );
}

export default function MapPage() {
  return (
    <div className="map-page" style={{ height: '100vh', width: '100vw', margin: 0, padding: 0 }}>
      <MapContainer center={[-23.65, -52.60]} zoom={13} style={{ height: '100vh', width: '100%' }}>
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution="© OpenStreetMap"
          maxZoom={19}
        />
        <LocationMarkerWithOcorrencias />
      </MapContainer>
    </div>
  );
}
