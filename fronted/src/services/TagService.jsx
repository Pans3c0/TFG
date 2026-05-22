import api from "./api"; // Asumo que tienes configurado tu axios aquí

export const obtenerEtiquetas = async () => {
  const respuesta = await api.get("/etiquetas");
  return respuesta.data;
};

export const crearEtiqueta = async (datos) => {
  const respuesta = await api.post("/etiquetas", datos);
  return respuesta.data;
};

export const borrarEtiqueta = async (id) => {
  await api.delete(`/etiquetas/${id}`);
};
