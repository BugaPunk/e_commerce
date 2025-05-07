# Documentación de la API de E-commerce

> **Nota**: Esta es una versión resumida de la documentación de la API. La documentación completa y detallada se encuentra en [docs/api_documentation.md](../../docs/api_documentation.md) en el repositorio de GitHub.

## Descripción General
Esta API proporciona servicios para una plataforma de e-commerce multi-tienda, permitiendo la gestión de productos, tiendas, carritos de compra, pedidos, pagos y reseñas.

## Base URL
```
http://localhost:8080/api
```

## Endpoints de Configuración Inicial

Estos endpoints son utilizados para la configuración inicial del sistema y no forman parte de la API pública.

### Inicializar Roles
**Endpoint:** `POST /api/init/roles`

**Descripción:** Inicializa los roles básicos del sistema (CLIENTE, ADMIN, VENDEDOR) si no existen.

### Registro Simplificado de Usuario
**Endpoint:** `POST /api/auth-simple/registro`

**Descripción:** Versión simplificada del endpoint de registro de usuarios para pruebas iniciales.

**Request Body:**
```json
{
  "email": "usuario@ejemplo.com",
  "nombre": "Nombre",
  "apellido": "Apellido",
  "password": "contraseña",
  "roles": ["CLIENTE"]
}
```

### Login Simplificado
**Endpoint:** `POST /api/auth-simple/login`

**Descripción:** Versión simplificada del endpoint de inicio de sesión para pruebas iniciales.

**Request Body:**
```json
{
  "email": "usuario@ejemplo.com",
  "password": "contraseña"
}
```

### Login Alternativo (Parámetros en URL)
**Endpoint:** `POST /api/auth-simple/login-alt`

**Descripción:** Versión alternativa del endpoint de inicio de sesión que acepta parámetros en la URL.

**Parámetros de Consulta:**
- `email`: Correo electrónico del usuario
- `password`: Contraseña del usuario

**Ejemplo de URL:**
```
http://localhost:8080/api/auth-simple/login-alt?email=usuario@ejemplo.com&password=contraseña
```

**Nota:** Antes de usar estos endpoints, asegúrate de haber inicializado los roles con el endpoint `/api/init/roles`.

## Resumen de Endpoints

### Autenticación
- `POST /api/auth/registro`: Registra un nuevo usuario
- `POST /api/auth/login`: Inicia sesión
- `GET /api/auth/perfil`: Obtiene el perfil del usuario
- `PUT /api/auth/perfil/{id}`: Actualiza el perfil del usuario

### Catálogo
- `GET /api/catalogo/productos`: Obtiene todos los productos
- `GET /api/catalogo/productos/{id}`: Obtiene un producto por ID
- `GET /api/catalogo/productos/buscar`: Busca productos por palabra clave
- `GET /api/catalogo/tiendas/{tiendaId}/productos`: Obtiene productos por tienda
- `GET /api/catalogo/categorias/{categoriaId}/productos`: Obtiene productos por categoría

### Carrito
- `GET /api/carrito/{usuarioId}`: Obtiene el carrito de un usuario
- `POST /api/carrito/agregar`: Agrega un producto al carrito
- `PUT /api/carrito/actualizar`: Actualiza la cantidad de un producto en el carrito
- `DELETE /api/carrito/eliminar`: Elimina un producto del carrito
- `DELETE /api/carrito/vaciar/{usuarioId}`: Vacía el carrito

### Pagos
- `POST /api/pagos/procesar`: Procesa un pago
- `GET /api/pagos/{pagoId}`: Obtiene información de un pago
- `GET /api/pagos/usuario/{usuarioId}`: Obtiene el historial de pagos de un usuario
- `POST /api/pagos/reembolso/{pagoId}`: Procesa un reembolso

### Reseñas
- `GET /api/reseñas/producto/{productoId}`: Obtiene reseñas por producto
- `GET /api/reseñas/usuario/{usuarioId}`: Obtiene reseñas por usuario
- `POST /api/reseñas`: Crea una nueva reseña
- `PUT /api/reseñas/{id}`: Actualiza una reseña
- `DELETE /api/reseñas/{id}`: Elimina una reseña

### Administración de Tiendas
- `GET /api/admin/tiendas`: Obtiene todas las tiendas
- `GET /api/admin/tiendas/{id}`: Obtiene una tienda por ID
- `POST /api/admin/tiendas`: Crea una nueva tienda
- `PUT /api/admin/tiendas/{id}`: Actualiza una tienda
- `DELETE /api/admin/tiendas/{id}`: Desactiva una tienda
- `POST /api/admin/tiendas/{tiendaId}/productos`: Agrega un producto a una tienda

## Documentación Completa

Esta es una versión resumida de la documentación de la API. Para ver la documentación completa y detallada, incluyendo ejemplos de request y response para cada endpoint, consulte el archivo [docs/api_documentation.md](../../docs/api_documentation.md) en el repositorio de GitHub.

## Consideraciones para el Frontend

Al desarrollar el frontend para esta API, tenga en cuenta lo siguiente:

1. **Autenticación**: Almacene el ID de usuario después del inicio de sesión para usarlo en las solicitudes posteriores.

2. **Manejo de errores**: Implemente un manejo de errores consistente para interpretar y mostrar los mensajes de error de la API.

3. **Paginación**: Implemente controles de paginación para navegar por grandes conjuntos de datos.

4. **Formularios**: Valide los datos del formulario en el cliente antes de enviarlos a la API para reducir las solicitudes con errores.

5. **Estado del carrito**: Mantenga el estado del carrito sincronizado con el servidor para evitar inconsistencias.

6. **Reactividad**: Actualice la interfaz de usuario inmediatamente después de acciones como agregar al carrito, enviar una reseña, etc., para proporcionar retroalimentación instantánea al usuario.
