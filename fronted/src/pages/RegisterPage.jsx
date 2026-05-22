import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../services/api";
import styles from "../styles/Auth.module.css";

const RegisterPage = () => {
  const [datosFormulario, setDatosFormulario] = useState({
    nombreUsuario: "",
    correo: "",
    nombreCompleto: "",
    contrasena: "",
    verificarContrasena: "",
  });
  const [error, setError] = useState("");
  const navegar = useNavigate();

  const manejarCambio = (e) => {
    setDatosFormulario({ ...datosFormulario, [e.target.name]: e.target.value });
  };

  const manejarEnvio = async (e) => {
    e.preventDefault();
    setError("");

    if (datosFormulario.contrasena !== datosFormulario.verificarContrasena) {
      setError("Las contraseñas no coinciden");
      return;
    }

    try {
      await api.post("/auth/register", datosFormulario);
      navegar("/login");
    } catch (err) {
      setError("Error al registrar. Intenta con otro usuario o email.");
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <h2 className={styles.title}>Bienvenido!</h2>
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
            type="email"
            name="correo"
            placeholder="Correo Electrónico"
            onChange={manejarCambio}
            required
            className={styles.input}
          />
          <input
            type="text"
            name="nombreCompleto"
            placeholder="Nombre Completo"
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
          <input
            type="password"
            name="verificarContrasena"
            placeholder="Repetir Contraseña"
            onChange={manejarCambio}
            required
            className={styles.input}
          />
          <button type="submit" className={styles.button}>
            Crear Cuenta
          </button>
        </form>
        {error && <p className={styles.error}>{error}</p>}
        <p className={styles.linkText}>
          ¿Ya tienes cuenta?{" "}
          <Link to="/login" className={styles.link}>
            Inicia Sesión
          </Link>
        </p>
      </div>
    </div>
  );
};

export default RegisterPage;
