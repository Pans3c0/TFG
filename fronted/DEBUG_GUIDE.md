# 🐛 Guía de Debuggeo - TodoList Frontend

## ✅ Lo que hemos arreglado:

1. **Interceptor de API mejorado**: Ya NO redirige a login cuando hay errores en crear tareas
2. **Logging persistente**: Los logs se guardan en `localStorage` incluso si la consola se limpia
3. **Botón "Ver Logs"**: Puedes ver todos los logs persistentes desde la app
4. **Logging de tokens**: Ahora puedes ver si el token se envía correctamente
5. **Headers por defecto**: Todos los requests llevan `Content-Type: application/json` y `Authorization: Bearer <token>`

---

## 🔴 Si recibís error 401 "Falta el token o es inválido"

Sigue estos pasos para diagnosticar:

### **Paso 1: Verifica el token en el login**
1. Abre **DevTools** (`F12`)
2. Ve a **Console**
3. **Intenta hacer login**
4. Busca estos logs:
   ```
   🔑 Token recibido: eyJhbGciOi... (primeros 30 caracteres)
   💾 Token guardado en localStorage
   🔑 Verificando token guardado: eyJhbGciOi...
   ```
   - Si ves **"NO"** → El servidor no está retornando token
   - Si ves **"NO ENCONTRADO"** → El localStorage no está guardando

### **Paso 2: Verifica el token cuando carga HomePage**
1. Después del login, en HomePage deberías ver:
   ```
   🔍 HomePage - Verificando autenticación
   👤 Usuario en localStorage: SÍ
   🔑 Token en localStorage: SÍ (eyJhbGciOi...)
   ✅ Usuario cargado: tunombre
   ```

### **Paso 3: Verifica que el token se envía en cada request**
1. Intenta crear una tarea
2. Busca logs como estos:
   ```
   📤 [POST] /tareas
   🔑 Token disponible: SÍ (eyJhbGciOi...)
   ✅ Header Authorization añadido
   ```
   - Si ves **"NO"** → El token se perdió de localStorage
   - Si ves **"Header Authorization añadido"** → Se está enviando correctamente

### **Paso 4: Si aún falla con 401**
Muestra estos logs:
```
🔴 Error de respuesta [401] /tareas
   Mensaje: Falta el token o es inválido
```

**Posibles causas:**
- El servidor espera formato diferente (ej: `Token` en lugar de `Bearer`)
- El token está expirado
- El token no es válido para este usuario
- El servidor tiene restricción de CORS

---

## 🔍 Cómo debuggear ahora:

### **Opción 1: Usar la consola + Logs persistentes**

1. Abre **DevTools** (`F12`)
2. Ve a **Console**
3. Intenta crear una tarea con error
4. Los logs aparecerán en consola Y se guardarán en localStorage
5. Si la consola se limpia, haz click en **"Ver Logs"** en la app → aparecerán de nuevo en consola

### **Opción 2: Acceder directamente a localStorage**

En la consola, escribe:

```javascript
JSON.parse(localStorage.getItem("appLogs")).forEach((log) => console.log(log));
```

O simplemente:

```javascript
console.table(JSON.parse(localStorage.getItem("appLogs")));
```

### **Opción 3: Ver el token en localStorage**

```javascript
// Ver primeros 50 caracteres del token
console.log(localStorage.getItem("token").substring(0, 50));

// Ver token completo
console.log(localStorage.getItem("token"));

// Ver usuario
console.log(JSON.parse(localStorage.getItem("loggedUser")));
```

### **Opción 4: Borrar logs y empezar de cero**

```javascript
localStorage.removeItem("appLogs");
```

---

## 📊 Qué esperar en los logs:

### **Cuando creas una tarea SIN categoría:**

```
11:45:23 [❌ CREAR_TAREA_ERROR] Error al crear tarea: {"message":"Categoría requerida"}
```

### **Cuando creas una tarea CORRECTAMENTE:**

```
11:45:25 [📝 CREAR_TAREA] Datos antes de crear: {...}
11:45:25 [🆔 CATEGORIA] ID: 123, Tipo: number
11:45:26 [✅ CREAR_TAREA] Tarea creada exitosamente
```

---

## 🚨 Si aún ves redirección a login:

Eso significa que el servidor está retornando un error diferente. Mira los logs para ver qué error específico retorna:

- **400**: Datos inválidos
- **401**: Token no válido (pero no debería pasar ahora)
- **500**: Error en el servidor

---

## 💡 Tips importantes:

- Los logs se guardan máximo 50 registros (se borra el más antiguo si superas ese límite)
- Los logs incluyen timestamp automático
- Todos los errores incluyen el mensaje del servidor (si está disponible)
- El localStorage se limpia cuando cierras sesión, así que copia los logs ANTES de cerrar sesión si los necesitas guardar
- El token suele tener formato JWT: `eyJhbGciOi...` (comienza con ey...)
