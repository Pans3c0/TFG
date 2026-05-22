import React, { useState, useEffect } from "react";
import styles from "../styles/TaskForm.module.css";

const TaskForm = ({
  alCrearTarea,
  alActualizarTarea,
  tareaEnEdicion,
  alCancelarEdicion,
  categorias,
  etiquetas = [],
}) => {
  const [datosFormulario, setDatosFormulario] = useState({
    titulo: "",
    descripcion: "",
    fechaLimite: "",
    categoriaId: "",
    prioridad: "MEDIA",
    estado: "PENDIENTE",
    etiquetasIds: [],
  });
  const [error, setError] = useState("");

  useEffect(() => {
    if (tareaEnEdicion) {
      let fechaFormateada = "";
      if (tareaEnEdicion.fechaLimite) {
        fechaFormateada = tareaEnEdicion.fechaLimite.substring(0, 16);
      }

      const idsEtiquetasActuales = tareaEnEdicion.etiquetas
        ? tareaEnEdicion.etiquetas.map((e) => e.id)
        : [];

      setDatosFormulario({
        titulo: tareaEnEdicion.titulo || "",
        descripcion: tareaEnEdicion.descripcion || "",
        fechaLimite: fechaFormateada,
        categoriaId: tareaEnEdicion.category ? tareaEnEdicion.category.id : "",
        prioridad: tareaEnEdicion.prioridad || "MEDIA",
        estado: tareaEnEdicion.estado || "PENDIENTE",
        etiquetasIds: idsEtiquetasActuales,
      });
      setError("");
    } else {
      setDatosFormulario({
        titulo: "",
        descripcion: "",
        fechaLimite: "",
        categoriaId: "",
        prioridad: "MEDIA",
        estado: "PENDIENTE",
        etiquetasIds: [],
      });
    }
  }, [tareaEnEdicion]);

  const manejarEnvio = async (e) => {
    e.preventDefault();
    setError("");

    if (!datosFormulario.titulo.trim()) {
      setError("El título no puede estar vacío.");
      return;
    }
    if (!datosFormulario.categoriaId) {
      setError("Debes seleccionar una lista/categoría.");
      return;
    }

    const datosEnvio = { ...datosFormulario };
    if (datosEnvio.fechaLimite) {
      if (datosEnvio.fechaLimite.length === 16) datosEnvio.fechaLimite += ":00";
    } else {
      datosEnvio.fechaLimite = null;
    }

    datosEnvio.categoriaId = Number(datosEnvio.categoriaId);
    datosEnvio.etiquetasIds = datosEnvio.etiquetasIds.map((id) => Number(id));

    try {
      if (tareaEnEdicion)
        await alActualizarTarea(tareaEnEdicion.id, datosEnvio);
      else await alCrearTarea(datosEnvio);
    } catch (err) {
      console.error("Error al guardar tarea:", err);
      setError("Error al guardar la tarea.");
    }
  };

  const manejarCambioEtiquetas = (e) => {
    const idEtiqueta = Number(e.target.value);
    if (e.target.checked) {
      setDatosFormulario({
        ...datosFormulario,
        etiquetasIds: [...datosFormulario.etiquetasIds, idEtiqueta],
      });
    } else {
      setDatosFormulario({
        ...datosFormulario,
        etiquetasIds: datosFormulario.etiquetasIds.filter(
          (id) => id !== idEtiqueta,
        ),
      });
    }
  };

  return (
    <div className={styles.overlay}>
      <div className={styles.form}>
        <div className={styles.header}>
          <h3>{tareaEnEdicion ? "Detalles" : "Nueva Tarea"}</h3>
          <button className={styles.closeBtn} onClick={alCancelarEdicion}>
            OK
          </button>
        </div>

        {error && <div className={styles.error}>{error}</div>}

        <form onSubmit={manejarEnvio}>
          <div className={styles.inputGroup}>
            <input
              type="text"
              placeholder="Título"
              value={datosFormulario.titulo}
              onChange={(e) =>
                setDatosFormulario({
                  ...datosFormulario,
                  titulo: e.target.value,
                })
              }
              className={styles.input}
              autoFocus
            />
          </div>
          <div className={styles.inputGroup}>
            <textarea
              placeholder="Notas"
              value={datosFormulario.descripcion}
              onChange={(e) =>
                setDatosFormulario({
                  ...datosFormulario,
                  descripcion: e.target.value,
                })
              }
              className={styles.textarea}
            />
          </div>

          <div className={styles.row}>
            <div className={styles.half}>
              <label className={styles.label}>Prioridad</label>
              <select
                value={datosFormulario.prioridad}
                onChange={(e) =>
                  setDatosFormulario({
                    ...datosFormulario,
                    prioridad: e.target.value,
                  })
                }
                className={styles.select}
              >
                <option value="ALTA">Alta</option>
                <option value="MEDIA">Media</option>
                <option value="BAJA">Baja</option>
              </select>
            </div>
            <div className={styles.half}>
              <label className={styles.label}>Estado</label>
              <select
                value={datosFormulario.estado}
                onChange={(e) =>
                  setDatosFormulario({
                    ...datosFormulario,
                    estado: e.target.value,
                  })
                }
                className={styles.select}
              >
                <option value="PENDIENTE">Pendiente</option>
                <option value="EN_PROGRESO">En Progreso</option>
                <option value="COMPLETADA">Completada</option>
              </select>
            </div>
          </div>

          <div className={styles.row}>
            <div className={styles.half}>
              <label className={styles.label}>Fecha Límite</label>
              <input
                type="datetime-local"
                value={datosFormulario.fechaLimite}
                onChange={(e) =>
                  setDatosFormulario({
                    ...datosFormulario,
                    fechaLimite: e.target.value,
                  })
                }
                className={styles.datetime}
              />
            </div>
            <div className={styles.half}>
              <label className={styles.label}>Lista</label>
              <select
                value={datosFormulario.categoriaId}
                onChange={(e) =>
                  setDatosFormulario({
                    ...datosFormulario,
                    categoriaId: e.target.value,
                  })
                }
                className={styles.select}
              >
                <option value="">Selecciona una opcion</option>
                {categorias.map((cat) => (
                  <option key={cat.id} value={cat.id}>
                    {cat.titulo}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className={styles.inputGroup}>
            <label className={styles.label}>Etiquetas</label>
            <div className={styles.row}>
              {etiquetas.map((etiqueta) => (
                <label key={etiqueta.id} className={styles.half}>
                  <input
                    type="checkbox"
                    value={etiqueta.id}
                    checked={datosFormulario.etiquetasIds.includes(etiqueta.id)}
                    onChange={manejarCambioEtiquetas}
                  />
                  <span> #{etiqueta.text}</span>
                </label>
              ))}
              {etiquetas.length === 0 && (
                <span className={styles.label}>
                  No hay etiquetas disponibles
                </span>
              )}
            </div>
          </div>

          <button type="submit" className={styles.submitBtn}>
            {tareaEnEdicion ? "Guardar" : "Añadir"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default TaskForm;
