import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  obtenerTareas,
  crearTarea,
  borrarTarea,
  actualizarTarea,
} from "../services/TaskService";
import { obtenerCategorias } from "../services/CategoryService";
import { obtenerEtiquetas } from "../services/TagService";
import TaskForm from "../components/TaskForm";
import TaskList from "../components/TaskList";
import CategoryManager from "../components/CategoryManager";
import UserManager from "../components/UserManager";
import TagManager from "../components/TagManager";
import styles from "../styles/HomePage.module.css";

const HomePage = () => {
  const [usuario, setUsuario] = useState(null);
  const [tareas, setTareas] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [etiquetas, setEtiquetas] = useState([]);
  const [tareaEnEdicion, setTareaEnEdicion] = useState(null);
  const [formularioAbierto, setFormularioAbierto] = useState(false);
  const [filtroPrioridad, setFiltroPrioridad] = useState("");
  const [filtroEstado, setFiltroEstado] = useState("");
  const [mensaje, setMensaje] = useState({ tipo: "", texto: "" });
  const navegar = useNavigate();

  useEffect(() => {
    const usuarioGuardado = localStorage.getItem("loggedUser");
    const token = localStorage.getItem("token");
    if (usuarioGuardado && token) {
      const usuarioParseado = JSON.parse(usuarioGuardado);
      setUsuario(usuarioParseado);
      cargarTareasUsuario(usuarioParseado.id);
      cargarCategoriasIniciales();
      cargarEtiquetasIniciales();
    } else {
      navegar("/login");
    }
  }, [navegar]);

  const cargarCategoriasIniciales = async () => {
    try {
      const datos = await obtenerCategorias();
      setCategorias(datos);
    } catch (e) {}
  };

  const cargarEtiquetasIniciales = async () => {
    try {
      const datos = await obtenerEtiquetas();
      setEtiquetas(datos);
    } catch (e) {
      console.error("Error al cargar etiquetas", e);
    }
  };

  const mostrarMensaje = (tipo, texto) => {
    setMensaje({ tipo, texto });
    setTimeout(() => setMensaje({ tipo: "", texto: "" }), 4000);
  };

  const cargarTareasUsuario = async (idUsuario) => {
    try {
      const datos = await obtenerTareas(idUsuario);
      setTareas(datos);
    } catch (err) {
      mostrarMensaje("error", "Error al cargar las tareas.");
    }
  };

  const manejarCrearTarea = async (datosTarea) => {
    try {
      const nuevaTarea = await crearTarea(datosTarea, usuario.id);
      setTareas([...tareas, nuevaTarea]);
      setFormularioAbierto(false);
      mostrarMensaje("success", "Tarea creada correctamente.");
    } catch (err) {
      mostrarMensaje(
        "error",
        "Error al crear la tarea: " +
          (err.response?.data?.message || err.message),
      );
    }
  };

  const manejarActualizarTarea = async (idTarea, datosActualizados) => {
    try {
      const tareaActualizada = await actualizarTarea(
        idTarea,
        datosActualizados,
      );
      setTareas(tareas.map((t) => (t.id === idTarea ? tareaActualizada : t)));
      setTareaEnEdicion(null);
      setFormularioAbierto(false);
      mostrarMensaje("success", "Tarea actualizada.");
    } catch (err) {
      mostrarMensaje(
        "error",
        "Error al actualizar la tarea: " +
          (err.response?.data?.message || err.message),
      );
    }
  };

  const manejarBorrarTarea = async (idTarea) => {
    try {
      await borrarTarea(idTarea);
      setTareas(tareas.filter((t) => t.id !== idTarea));
    } catch (err) {
      mostrarMensaje("error", "Error al borrar la tarea.");
    }
  };

  const manejarCambioEstado = async (tarea) => {
    try {
      const nuevoEstado =
        tarea.estado === "COMPLETADA" ? "PENDIENTE" : "COMPLETADA";

      const datosEnvio = {
        titulo: tarea.titulo,
        descripcion: tarea.descripcion,
        fechaLimite: tarea.fechaLimite,
        prioridad: tarea.prioridad,
        estado: nuevoEstado,
        categoriaId: tarea.category ? tarea.category.id : null,
        etiquetasIds: tarea.etiquetas ? tarea.etiquetas.map((e) => e.id) : [],
      };

      const tareaActualizada = await actualizarTarea(tarea.id, datosEnvio);
      setTareas(tareas.map((t) => (t.id === tarea.id ? tareaActualizada : t)));
    } catch (err) {
      mostrarMensaje("error", "Error al cambiar el estado.");
    }
  };

  const tareasFiltradas = tareas.filter((tarea) => {
    let coincidePrioridad = true;
    let coincideEstado = true;
    if (filtroPrioridad)
      coincidePrioridad = tarea.prioridad === filtroPrioridad;
    if (filtroEstado) coincideEstado = tarea.estado === filtroEstado;
    return coincidePrioridad && coincideEstado;
  });

  const cantidadAltaPrioridad = tareas.filter(
    (t) => t.prioridad === "ALTA",
  ).length;
  const cantidadCompletadas = tareas.filter(
    (t) => t.estado === "COMPLETADA",
  ).length;

  if (!usuario)
    return (
      <div style={{ textAlign: "center", marginTop: "50px" }}>Cargando...</div>
    );
  const esGestorOAdmin = usuario.Rol === "GESTOR" || usuario.Rol === "ADMIN";
  const esAdmin = usuario.Rol === "ADMIN";

  return (
    <div className={styles.container}>
      <header className={styles.header}>
        <h1 className={styles.title}>Mis Listas</h1>
        <button
          onClick={() => {
            localStorage.removeItem("loggedUser");
            localStorage.removeItem("token");
            navegar("/login");
          }}
          className={styles.logoutBtn}
        >
          Cerrar Sesión
        </button>
      </header>

      {mensaje.texto && (
        <div
          className={`${styles.message} ${mensaje.tipo === "error" ? styles.msgError : styles.msgSuccess}`}
        >
          {mensaje.tipo === "error" ? "❌ " : "✅ "} {mensaje.texto}
        </div>
      )}

      <div className={styles.dashboardGrid}>
        <div
          className={`${styles.dashCard} ${styles.cardBlue}`}
          onClick={() => {
            setFiltroEstado("PENDIENTE");
            setFiltroPrioridad("");
          }}
        >
          <span className={styles.dashCardIcon}>📅</span>
          <span className={styles.dashCardTitle}>Hoy / Pendientes</span>
          <span className={styles.dashCardCount}>
            {tareas.length - cantidadCompletadas}
          </span>
        </div>
        <div
          className={`${styles.dashCard} ${styles.cardRed}`}
          onClick={() => setFiltroPrioridad("ALTA")}
        >
          <span className={styles.dashCardIcon}>⏰</span>
          <span className={styles.dashCardTitle}>Urgentes</span>
          <span className={styles.dashCardCount}>{cantidadAltaPrioridad}</span>
        </div>
        <div
          className={`${styles.dashCard} ${styles.cardGray}`}
          onClick={() => {
            setFiltroEstado("");
            setFiltroPrioridad("");
          }}
        >
          <span className={styles.dashCardIcon}>🗂️</span>
          <span className={styles.dashCardTitle}>Todos</span>
          <span className={styles.dashCardCount}>{tareas.length}</span>
        </div>
        <div
          className={`${styles.dashCard} ${styles.cardOrange}`}
          onClick={() => setFiltroEstado("COMPLETADA")}
        >
          <span className={styles.dashCardIcon}>✓</span>
          <span className={styles.dashCardTitle}>Completados</span>
          <span className={styles.dashCardCount}>{cantidadCompletadas}</span>
        </div>
      </div>

      <TagManager etiquetas={etiquetas} alActualizarEtiquetas={setEtiquetas} />

      {esGestorOAdmin && (
        <CategoryManager
          alCambiarCategorias={(nuevasCats) => {
            setCategorias(nuevasCats);
            cargarTareasUsuario(usuario.id);
          }}
        />
      )}

      {esAdmin && <UserManager />}

      <h2 className={styles.sectionTitle}>Tareas</h2>

      <div className={styles.filters}>
        <select
          className={styles.filterSelect}
          value={filtroPrioridad}
          onChange={(e) => setFiltroPrioridad(e.target.value)}
        >
          <option value="">Prioridad (Todas)</option>
          <option value="ALTA">Alta</option>
          <option value="MEDIA">Media</option>
          <option value="BAJA">Baja</option>
        </select>
        <select
          className={styles.filterSelect}
          value={filtroEstado}
          onChange={(e) => setFiltroEstado(e.target.value)}
        >
          <option value="">Estado (Todos)</option>
          <option value="PENDIENTE">Pendientes</option>
          <option value="COMPLETADA">Completadas</option>
        </select>
      </div>

      <div className={styles.listContainer}>
        <TaskList
          tareas={tareasFiltradas}
          alBorrar={manejarBorrarTarea}
          alEditar={(tarea) => {
            setTareaEnEdicion(tarea);
            setFormularioAbierto(true);
          }}
          alCambiarEstado={manejarCambioEstado}
        />
      </div>

      <button
        className={styles.fab}
        onClick={() => {
          setTareaEnEdicion(null);
          setFormularioAbierto(true);
        }}
      >
        +
      </button>

      {formularioAbierto && (
        <TaskForm
          alCrearTarea={manejarCrearTarea}
          alActualizarTarea={manejarActualizarTarea}
          tareaEnEdicion={tareaEnEdicion}
          alCancelarEdicion={() => {
            setTareaEnEdicion(null);
            setFormularioAbierto(false);
          }}
          categorias={categorias}
          etiquetas={etiquetas}
        />
      )}
    </div>
  );
};

export default HomePage;
