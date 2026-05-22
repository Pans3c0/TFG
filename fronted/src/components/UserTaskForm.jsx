import React, { useState, useEffect } from "react";
import styles from "../styles/TaskForm.module.css"; // Reutilizamos tus estilos del modal

const UserTaskForm = ({
  usuarioEnEdicion,
  alActualizarUsuario,
  alCancelarEdicion,
}) => {
  // Estado local para guardar lo que el usuario escribe en los inputs
  const [datosFormulario, setDatosFormulario] = useState({
    correo: "",
    nombreCompleto: "",
    contrasena: "",
  });
  const [error, setError] = useState("");

  // Este Hook se ejecuta cada vez que 'usuarioEnEdicion' cambia.
  // Sirve para rellenar los campos con los datos del usuario que queremos editar.
  useEffect(() => {
    if (usuarioEnEdicion) {
      setDatosFormulario({
        correo: usuarioEnEdicion.correo || "",
        nombreCompleto: usuarioEnEdicion.nombreCompleto || "",
        // Por seguridad, la contraseña suele dejarse en blanco al editar
        // para que solo se actualice si el administrador escribe una nueva.
        contrasena: "",
      });
      setError("");
    }
  }, [usuarioEnEdicion]);

  // Función que se ejecuta al darle al botón de Guardar
  const manejarEnvio = async (e) => {
    e.preventDefault(); // Evita que la página se recargue al enviar el formulario
    setError("");

    // Validaciones básicas
    if (
      !datosFormulario.correo.trim() ||
      !datosFormulario.nombreCompleto.trim()
    ) {
      setError("El correo y el nombre completo son obligatorios.");
      return;
    }

    try {
      // Llamamos a la función que nos pasó el componente padre (UserManager)
      await alActualizarUsuario(usuarioEnEdicion.id, datosFormulario);
    } catch (err) {
      console.error("Error al actualizar usuario:", err);
      setError("Error al guardar los cambios del usuario.");
    }
  };

  return (
    <div className={styles.overlay}>
      <div className={styles.form}>
        <div className={styles.header}>
          <h3>Editar Usuario</h3>
          <button className={styles.closeBtn} onClick={alCancelarEdicion}>
            X
          </button>
        </div>

        {error && <div className={styles.error}>{error}</div>}

        <form onSubmit={manejarEnvio}>
          <div className={styles.inputGroup}>
            <label className={styles.label}>Nombre Completo</label>
            <input
              type="text"
              placeholder="Ej: Juan Pérez"
              value={datosFormulario.nombreCompleto}
              onChange={(e) =>
                setDatosFormulario({
                  ...datosFormulario,
                  nombreCompleto: e.target.value,
                })
              }
              className={styles.input}
              autoFocus
            />
          </div>

          <div className={styles.inputGroup}>
            <label className={styles.label}>Correo Electrónico</label>
            <input
              type="email"
              placeholder="correo@ejemplo.com"
              value={datosFormulario.correo}
              onChange={(e) =>
                setDatosFormulario({
                  ...datosFormulario,
                  correo: e.target.value,
                })
              }
              className={styles.input}
            />
          </div>

          <div className={styles.inputGroup}>
            <label className={styles.label}>Nueva Contraseña (opcional)</label>
            <input
              type="password"
              placeholder="Escribe para cambiar la contraseña..."
              value={datosFormulario.contrasena}
              onChange={(e) =>
                setDatosFormulario({
                  ...datosFormulario,
                  contrasena: e.target.value,
                })
              }
              className={styles.input}
            />
          </div>

          <button type="submit" className={styles.submitBtn}>
            Guardar Cambios
          </button>
        </form>
      </div>
    </div>
  );
};

export default UserTaskForm;
