import React, { useState } from "react";
import { crearEtiqueta, borrarEtiqueta } from "../services/TagService";
import styles from "../styles/TagManager.module.css";

const TagManager = ({ etiquetas, alActualizarEtiquetas }) => {
  const [nuevaEtiqueta, setNuevaEtiqueta] = useState("");
  const [error, setError] = useState("");

  const manejarCrear = async (e) => {
    e.preventDefault();
    if (!nuevaEtiqueta.trim()) {
      setError("El nombre no puede estar vacío");
      return;
    }

    try {
      const etiquetaCreada = await crearEtiqueta({ text: nuevaEtiqueta });
      alActualizarEtiquetas([...etiquetas, etiquetaCreada]);
      setNuevaEtiqueta("");
      setError("");
    } catch (err) {
      console.error("Error al crear etiqueta:", err);
      setError("Hubo un error al guardar la etiqueta");
    }
  };

  const manejarBorrar = async (id) => {
    try {
      await borrarEtiqueta(id);
      const etiquetasRestantes = etiquetas.filter((e) => e.id !== id);
      alActualizarEtiquetas(etiquetasRestantes);
    } catch (err) {
      console.error("Error al borrar etiqueta:", err);
      setError("No se pudo borrar la etiqueta");
    }
  };

  return (
    <div className={styles.container}>
      <h3 className={styles.title}>Gestor de Etiquetas</h3>

      {error && <p className={styles.error}>{error}</p>}

      <form onSubmit={manejarCrear} className={styles.formGroup}>
        <input
          type="text"
          placeholder="Nueva etiqueta..."
          value={nuevaEtiqueta}
          onChange={(e) => setNuevaEtiqueta(e.target.value)}
          className={styles.input}
        />
        <button type="submit" className={styles.submitBtn}>
          Añadir
        </button>
      </form>

      <div className={styles.tagList}>
        {etiquetas.map((etiqueta) => (
          <span key={etiqueta.id} className={styles.tagBadge}>
            #{etiqueta.text}
            <button
              type="button"
              onClick={() => manejarBorrar(etiqueta.id)}
              className={styles.deleteBtn}
              title="Eliminar etiqueta"
            >
              x
            </button>
          </span>
        ))}
        {etiquetas.length === 0 && (
          <span className={styles.emptyText}>No hay etiquetas creadas.</span>
        )}
      </div>
    </div>
  );
};

export default TagManager;
