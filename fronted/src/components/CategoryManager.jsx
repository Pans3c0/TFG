import React, { useState, useEffect } from "react";
import { obtenerCategorias, crearCategoria, borrarCategoria } from "../services/CategoryService";
import styles from "../styles/CategoryManager.module.css";

const CategoryManager = ({ alCambiarCategorias }) => {
  const [categorias, setCategorias] = useState([]);
  const [nuevoTitulo, setNuevoTitulo] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    cargarCategorias();
  }, []);

  const cargarCategorias = async () => {
    try {
      const datos = await obtenerCategorias();
      setCategorias(datos);
      if(alCambiarCategorias) alCambiarCategorias(datos);
    } catch (err) {
      setError("Error al cargar categorías.");
    }
  };

  const manejarAñadir = async (e) => {
    e.preventDefault();
    if (!nuevoTitulo.trim()) return;

    try {
      const categoria = await crearCategoria({ titulo: nuevoTitulo });
      const actualizadas = [...categorias, categoria];
      setCategorias(actualizadas);
      setNuevoTitulo("");
      setError("");
      if(alCambiarCategorias) alCambiarCategorias(actualizadas);
    } catch (err) {
      setError("Error al crear categoría. Asegúrate de tener permisos.");
    }
  };

  const manejarBorrar = async (id) => {
    const mensajeAdvertencia = "⚠️ ¡ADVERTENCIA CRÍTICA!\n\n¿Estás completamente seguro de que quieres borrar esta lista/categoría?\n\nAl borrarla, TODAS LAS TAREAS que estén vinculadas a esta categoría (tanto tuyas como de otros usuarios) serán eliminadas de la base de datos de forma permanente.\n\n¿Deseas continuar?";
    if (!window.confirm(mensajeAdvertencia)) return;
    
    try {
      await borrarCategoria(id);
      const actualizadas = categorias.filter((c) => c.id !== id);
      setCategorias(actualizadas);
      setError("");
      if(alCambiarCategorias) alCambiarCategorias(actualizadas);
    } catch (err) {
      setError("Error al borrar categoría. Revisa si el servidor lo permite.");
    }
  };

  return (
    <div className={styles.container}>
      <h3 className={styles.title}>🛠️ Panel de Administración</h3>
      <p className={styles.subtitle}>Gestión de Listas (Solo GESTOR/ADMIN)</p>
      
      {error && <div className={styles.error}>⚠️ {error}</div>}
      
      <form onSubmit={manejarAñadir} className={styles.form}>
        <input 
          type="text" 
          placeholder="Nueva lista..." 
          value={nuevoTitulo} 
          onChange={(e) => setNuevoTitulo(e.target.value)} 
          className={styles.input}
        />
        <button type="submit" className={styles.button}>
          Crear
        </button>
      </form>

      <ul className={styles.list}>
        {categorias.length === 0 && <li className={styles.empty}>No hay listas. Crea una.</li>}
        {categorias.map((cat) => (
          <li key={cat.id} className={styles.listItem}>
            <span className={styles.catTitle}>{cat.titulo}</span>
            <button onClick={() => manejarBorrar(cat.id)} className={styles.deleteBtn}>
              Borrar
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CategoryManager;
