import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/login";
import Cadastro from "./pages/Cadastro";
import Dashboard from "./pages/Dashboard";
import NovaOcorrencia from "./pages/NovaOcorrencia"; // importa aqui

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/cadastro" element={<Cadastro />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/nova-ocorrencia" element={<NovaOcorrencia />} /> {/* nova rota */}
      </Routes>
    </Router>
  );
}

export default App;
