import api from './api';

export const obtenerTareas = async (authorId) => {
  const respuesta = await api.get(`/tareas?authorId=${authorId}`);
  return respuesta.data;
};

export const crearTarea = async (datosTarea, authorId) => {
  const respuesta = await api.post(`/tareas?authorId=${authorId}`, datosTarea);
  return respuesta.data;
};

export const borrarTarea = async (idTarea) => {
  await api.delete(`/tareas/${idTarea}`);
};

export const actualizarTarea = async (idTarea, datosTarea) => {
  const respuesta = await api.put(`/tareas/${idTarea}`, datosTarea);
  return respuesta.data;
};
