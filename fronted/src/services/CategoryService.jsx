import api from './api';

export const obtenerCategorias = async () => {
  const respuesta = await api.get('/categorias');
  return respuesta.data;
};

export const crearCategoria = async (datosCategoria) => {
  const respuesta = await api.post('/categorias', datosCategoria);
  return respuesta.data;
};

export const borrarCategoria = async (idCategoria) => {
  await api.delete(`/categorias/${idCategoria}`);
};
