import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../services/api";
import styles from "../styles/Auth.module.css";

const LoginPage = () => {
  const [datosFormulario, setDatosFormulario] = useState({
    nombreUsuario: "",
    contrasena: "",
  });
  const [error, setError] = useState("");
  const navegar = useNavigate();

  const manejarCambio = (e) => {
    setDatosFormulario({ ...datosFormulario, [e.target.name]: e.target.value });
  };

  const manejarEnvio = async (e) => {
    e.preventDefault();
    try {
      const respuesta = await api.post("/auth/login", datosFormulario);
      const datos = respuesta.data;
      if (!datos.token || !datos.usuario) {
        console.error("❌ Respuesta incompleta del servidor:", datos);
        setError("Respuesta inválida del servidor");
        return;
      }

      localStorage.setItem("token", datos.token);
      localStorage.setItem("loggedUser", JSON.stringify(datos.usuario));

      navegar("/tareas");
    } catch (err) {
      if (err.response && err.response.status === 401) {
        setError("Usuario o contraseña incorrectos");
      } else {
        setError("No se pudo conectar con el servidor");
      }
    }
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>TODO-LIST</h1>
      <div className={styles.card}>
        <h2 className={styles.cardTitle}>Iniciar Sesión</h2>
        <form onSubmit={manejarEnvio}>
          <input
            type="text"
            name="nombreUsuario"
            placeholder="Usuario"
            onChange={manejarCambio}
            required
            className={styles.input}
          />
          <input
            type="password"
            name="contrasena"
            placeholder="Contraseña"
            onChange={manejarCambio}
            required
            className={styles.input}
          />
          <button type="submit" className={styles.button}>
            Entrar
          </button>
        </form>
        {error && <p className={styles.error}>{error}</p>}
      </div>
      <div className={styles.linkContainer}>
        <Link to="/register">¿No tienes cuenta? Regístrate</Link>
      </div>
    </div>
  );
};

export default LoginPage;
