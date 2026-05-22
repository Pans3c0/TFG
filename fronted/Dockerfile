# ETAPA 1: Construcción
FROM node:18-alpine AS build
WORKDIR /app

# Copiamos dependencias e instalamos
COPY package*.json ./
RUN npm install

# Copiamos el resto del código
COPY . .

# Inyectamos la variable de entorno de tu API
ARG REACT_APP_API_URL
ENV REACT_APP_API_URL=$REACT_APP_API_URL

# Compilamos la app para producción
RUN npm run build

# ETAPA 2: Servidor Nginx
FROM nginx:alpine
# Copiamos los archivos estáticos compilados de la etapa anterior
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]