import React from "react";
import styles from "../styles/TaskList.module.css";

const TaskList = ({ tareas, alBorrar, alEditar, alCambiarEstado }) => {
  return (
    <div className={styles.list}>
      {tareas.length === 0 ? (
        <p
          style={{
            textAlign: "center",
            color: "var(--text-secondary)",
            padding: "30px",
            margin: 0,
          }}
        >
          No hay tareas en esta vista.
        </p>
      ) : (
        tareas.map((tarea) => (
          <div key={tarea.id} className={styles.taskItem}>
            <div className={styles.content}>
              <h3
                className={`${styles.title} ${tarea.estado === "COMPLETADA" ? styles.completed : ""}`}
              >
                {tarea.titulo}
              </h3>
              {tarea.descripcion && (
                <p className={styles.description}>{tarea.descripcion}</p>
              )}
              <div className={styles.meta}>
                {tarea.fechaLimite && (
                  <span className={styles.deadline}>
                    {new Date(tarea.fechaLimite).toLocaleString([], {
                      hour: "2-digit",
                      minute: "2-digit",
                      day: "2-digit",
                      month: "short",
                    })}
                  </span>
                )}
                {tarea.prioridad === "ALTA" && (
                  <span className={styles.priority}>¡Urgente!</span>
                )}
              </div>
              <div className={styles.actions}>
                <button
                  className={`${styles.actionBtn} ${styles.editBtn}`}
                  onClick={() => alEditar(tarea)}
                >
                  Editar
                </button>
                <button
                  className={`${styles.actionBtn} ${styles.deleteBtn}`}
                  onClick={() => {
                    if (
                      window.confirm("¿Seguro que quieres borrar esta tarea?")
                    )
                      alBorrar(tarea.id);
                  }}
                >
                  Borrar
                </button>
              </div>
            </div>
          </div>
        ))
      )}
    </div>
  );
};

export default TaskList;
