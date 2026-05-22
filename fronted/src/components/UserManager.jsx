import React, { useState, useEffect } from "react";
import {
  obtenerUsuarios,
  ascenderUsuario,
  degradarUsuario,
  actualizarUsuario, // <- Asegúrate de añadir esta función en tu servicio
} from "../services/UserService";
import styles from "../styles/CategoryManager.module.css";
import UserTaskForm from "./UserTaskForm"; // Importamos el nuevo formulario

const UserManager = () => {
  const [usuarios, setUsuarios] = useState([]);
  const [error, setError] = useState("");
  const [cargando, setCargando] = useState(true);

  // Nuevo estado para saber a qué usuario estamos editando.
  // Si es 'null', el formulario estará cerrado.
  const [usuarioEnEdicion, setUsuarioEnEdicion] = useState(null);

  const usuarioActual = JSON.parse(localStorage.getItem("loggedUser"));

  useEffect(() => {
    cargarUsuarios();
  }, []);

  const cargarUsuarios = async () => {
    try {
      const data = await obtenerUsuarios();
      setUsuarios(data);
    } catch (err) {
      setError("Error al cargar usuarios.");
    } finally {
      setCargando(false);
    }
  };

  const manejarAscenso = async (id) => {
    if (
      !window.confirm("¿Seguro que quieres ascender a este usuario a GESTOR?")
    )
      return;
    try {
      await ascenderUsuario(id);
      cargarUsuarios();
    } catch (err) {
      setError("Error al ascender usuario.");
    }
  };

  const manejarDegradacion = async (id) => {
    if (!window.confirm("¿Seguro que quieres degradar a este usuario a USER?"))
      return;
    try {
      await degradarUsuario(id);
      cargarUsuarios();
    } catch (err) {
      setError("Error al degradar usuario.");
    }
  };

  // Nueva función para enviar los datos a la API
  const manejarActualizacionUsuario = async (id, datosActualizados) => {
    try {
      // Enviamos el objeto con correo, nombreCompleto y contrasena a tu API
      await actualizarUsuario(id, datosActualizados);
      // Cerramos el formulario estableciendo el estado a null
      setUsuarioEnEdicion(null);
      // Recargamos la lista para ver los cambios
      cargarUsuarios();
    } catch (err) {
      console.error(err);
      throw err; // Lanzamos el error para que el 'catch' dentro del Formulario lo atrape y muestre el mensaje visual
    }
  };

  return (
    <div className={styles.managerCard}>
      <h3 className={styles.title}>Gestión de Usuarios (Admin)</h3>
      {error && (
        <div style={{ color: "red", marginBottom: "10px" }}>{error}</div>
      )}

      {cargando ? (
        <p>Cargando usuarios...</p>
      ) : (
        <ul className={styles.list}>
          {usuarios.map((user) => (
            <li key={user.id} className={styles.listItem}>
              <div>
                <strong>{user.nombreUsuario || user.nombreCompleto}</strong>
                <span
                  style={{
                    marginLeft: "10px",
                    fontSize: "0.8em",
                    color: "#888",
                  }}
                >
                  ({user.Rol})
                </span>
                <br />
                {/* Mostramos el correo para dar más contexto en la lista */}
                <span style={{ fontSize: "0.85em", color: "#555" }}>
                  {user.correo}
                </span>
              </div>

              <div
                style={{ display: "flex", gap: "10px", alignItems: "center" }}
              >
                {/* BOTÓN EDITAR */}
                <button
                  onClick={() => setUsuarioEnEdicion(user)}
                  style={{
                    background: "#2196f3",
                    border: "none",
                    color: "white",
                    padding: "5px 10px",
                    borderRadius: "5px",
                    cursor: "pointer",
                  }}
                >
                  Editar
                </button>

                {user.Rol === "USER" && (
                  <button
                    onClick={() => manejarAscenso(user.id)}
                    style={{
                      background: "#4caf50",
                      border: "none",
                      color: "white",
                      padding: "5px 10px",
                      borderRadius: "5px",
                      cursor: "pointer",
                    }}
                  >
                    Ascender
                  </button>
                )}
                {user.Rol === "GESTOR" && (
                  <button
                    onClick={() => manejarDegradacion(user.id)}
                    style={{
                      background: "#ff9800",
                      border: "none",
                      color: "white",
                      padding: "5px 10px",
                      borderRadius: "5px",
                      cursor: "pointer",
                    }}
                  >
                    Degradar
                  </button>
                )}
                {user.id === usuarioActual?.id && (
                  <span style={{ fontSize: "0.8em", color: "#4caf50" }}>
                    Tú
                  </span>
                )}
              </div>
            </li>
          ))}
        </ul>
      )}

      {/* Renderizado condicional del Formulario */}
      {usuarioEnEdicion && (
        <UserTaskForm
          usuarioEnEdicion={usuarioEnEdicion}
          alActualizarUsuario={manejarActualizacionUsuario}
          alCancelarEdicion={() => setUsuarioEnEdicion(null)}
        />
      )}
    </div>
  );
};

export default UserManager;
