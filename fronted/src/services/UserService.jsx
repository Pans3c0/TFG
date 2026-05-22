import api from "./api";

export const obtenerUsuarios = async () => {
  const respuesta = await api.get("/usuarios");
  return respuesta.data;
};

export const ascenderUsuario = async (id) => {
  const respuesta = await api.put(`/usuarios/${id}/promote`);
  return respuesta.data;
};

export const degradarUsuario = async (id) => {
  const respuesta = await api.put(`/usuarios/${id}/demote`);
  return respuesta.data;
};
export const actualizarUsuario = async (id, datos) => {
  const respuesta = await api.put(`/usuarios/${id}`, datos);
  return respuesta.data;
};
