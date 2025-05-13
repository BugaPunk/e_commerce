# Documentación de la API de E-Commerce

Este documento proporciona una documentación completa de todos los endpoints disponibles en la API de E-Commerce. Utiliza esta referencia cuando implementes la API en aplicaciones web, aplicaciones móviles o cualquier otra plataforma.

## URL Base

```
http://localhost:8080/api
```

## Autenticación

La API utiliza un único método de autenticación basado en JWT:

### Registro de usuario

**Endpoint:** `POST /api/auth/registro`
**Descripción:** Registra un nuevo usuario en el sistema y devuelve un token JWT.
**Cuerpo de la Solicitud:**
```json
{
  "email": "usuario@ejemplo.com",
  "nombre": "Juan",
  "apellido": "Pérez",
  "password": "contraseña123",
  "roles": ["CLIENTE"]
}
```
**Respuesta:**
```json
{
  "success": true,
  "message": "Registro exitoso",
  "data": {
    "usuario": {
      "id": 1,
      "email": "usuario@ejemplo.com",
      "nombre": "Juan",
      "apellido": "Pérez",
      "password": null,
      "roles": ["CLIENTE"]
    },
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  },
  "timestamp": "2025-05-11T11:28:40.376061498"
}
```

### Iniciar sesión

**Endpoint:** `POST /api/auth/login`
**Descripción:** Autentica a un usuario y devuelve un token JWT.
**Cuerpo de la Solicitud:**
```json
{
  "email": "usuario@ejemplo.com",
  "password": "contraseña123"
}
```
**Respuesta:**
```json
{
  "success": true,
  "message": "Inicio de sesión exitoso",
  "data": {
    "usuario": {
      "id": 1,
      "email": "usuario@ejemplo.com",
      "nombre": "Juan",
      "apellido": "Pérez",
      "password": null,
      "roles": ["CLIENTE"]
    },
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  },
  "timestamp": "2025-05-11T11:28:53.178605254"
}
```

#### Obtener perfil de usuario
**Endpoint:** `GET /api/auth/perfil`
**Descripción:** Obtiene el perfil de un usuario.
**Parámetros de Consulta:**
- `usuarioId`: ID del usuario

**Respuesta:** Devuelve información del usuario.

#### Actualizar perfil de usuario
**Endpoint:** `PUT /api/auth/perfil/{id}`
**Descripción:** Actualiza el perfil de un usuario.
**Parámetros de Ruta:**
- `id`: ID del usuario

**Cuerpo de la Solicitud:**
```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "usuario@ejemplo.com"
}
```
**Respuesta:** Devuelve la información actualizada del usuario.

## Catálogo

### Obtener todos los productos
**Endpoint:** `GET /api/catalogo/productos`
**Descripción:** Obtiene una lista paginada de todos los productos.
**Parámetros de Consulta:**
- `page`: Número de página (predeterminado: 0)
- `size`: Tamaño de página (predeterminado: 10)
- `sort`: Campo de ordenación (predeterminado: "id")

**Respuesta:** Devuelve una lista paginada de productos.

### Obtener producto por ID
**Endpoint:** `GET /api/catalogo/productos/{id}`
**Descripción:** Obtiene un producto por su ID.
**Parámetros de Ruta:**
- `id`: ID del producto

**Respuesta:** Devuelve la información del producto.

### Buscar productos
**Endpoint:** `GET /api/catalogo/productos/buscar`
**Descripción:** Busca productos por palabra clave.
**Parámetros de Consulta:**
- `keyword`: Término de búsqueda
- `page`: Número de página (predeterminado: 0)
- `size`: Tamaño de página (predeterminado: 10)

**Respuesta:** Devuelve una lista paginada de productos que coinciden con el término de búsqueda.

### Obtener productos por tienda
**Endpoint:** `GET /api/catalogo/tiendas/{tiendaId}/productos`
**Descripción:** Obtiene productos de una tienda específica.
**Parámetros de Ruta:**
- `tiendaId`: ID de la tienda

**Parámetros de Consulta:**
- `page`: Número de página (predeterminado: 0)
- `size`: Tamaño de página (predeterminado: 10)

**Respuesta:** Devuelve una lista paginada de productos de la tienda especificada.

### Obtener productos por categoría
**Endpoint:** `GET /api/catalogo/categorias/{categoriaId}/productos`
**Descripción:** Obtiene productos de una categoría específica.
**Parámetros de Ruta:**
- `categoriaId`: ID de la categoría

**Parámetros de Consulta:**
- `page`: Número de página (predeterminado: 0)
- `size`: Tamaño de página (predeterminado: 10)

**Respuesta:** Devuelve una lista paginada de productos de la categoría especificada.

## Carrito de Compras

### Obtener carrito del usuario
**Endpoint:** `GET /api/carrito/{usuarioId}`
**Descripción:** Obtiene el carrito de compras de un usuario.
**Parámetros de Ruta:**
- `usuarioId`: ID del usuario

**Respuesta:** Devuelve el carrito de compras del usuario.

### Agregar producto al carrito
**Endpoint:** `POST /api/carrito/agregar`
**Descripción:** Agrega un producto al carrito de compras del usuario.
**Parámetros de Consulta:**
- `usuarioId`: ID del usuario
- `productoId`: ID del producto
- `cantidad`: Cantidad a agregar

**Respuesta:** Devuelve el carrito de compras actualizado.

### Actualizar cantidad de producto
**Endpoint:** `PUT /api/carrito/actualizar`
**Descripción:** Actualiza la cantidad de un producto en el carrito de compras del usuario.
**Parámetros de Consulta:**
- `usuarioId`: ID del usuario
- `productoId`: ID del producto
- `cantidad`: Nueva cantidad

**Respuesta:** Devuelve el carrito de compras actualizado.

### Eliminar producto del carrito
**Endpoint:** `DELETE /api/carrito/eliminar`
**Descripción:** Elimina un producto del carrito de compras del usuario.
**Parámetros de Consulta:**
- `usuarioId`: ID del usuario
- `productoId`: ID del producto

**Respuesta:** Devuelve el carrito de compras actualizado.

### Vaciar carrito
**Endpoint:** `DELETE /api/carrito/vaciar/{usuarioId}`
**Descripción:** Vacía el carrito de compras del usuario.
**Parámetros de Ruta:**
- `usuarioId`: ID del usuario

**Respuesta:** Devuelve el carrito de compras vacío.

## Gestión de Productos

### Crear producto
**Endpoint:** `POST /api/productos`
**Descripción:** Crea un nuevo producto.
**Autorización:** Requiere rol ADMIN o VENDEDOR.
**Cuerpo de la Solicitud:**
```json
{
  "nombre": "Nombre del Producto",
  "descripcion": "Descripción del Producto",
  "precio": 99.99,
  "stock": 100,
  "categoriaId": 1,
  "tiendaId": 1,
  "imagenUrl": "http://ejemplo.com/imagen.jpg"
}
```
**Respuesta:** Devuelve el producto creado con un estado 201 Created.

### Actualizar producto
**Endpoint:** `PUT /api/productos/{id}`
**Descripción:** Actualiza un producto existente.
**Autorización:** Requiere rol ADMIN o VENDEDOR.
**Parámetros de Ruta:**
- `id`: ID del producto

**Cuerpo de la Solicitud:**
```json
{
  "nombre": "Nombre Actualizado del Producto",
  "descripcion": "Descripción Actualizada del Producto",
  "precio": 129.99,
  "stock": 50,
  "categoriaId": 1,
  "imagenUrl": "http://ejemplo.com/imagen-actualizada.jpg"
}
```
**Respuesta:** Devuelve el producto actualizado.

### Eliminar producto
**Endpoint:** `DELETE /api/productos/{id}`
**Descripción:** Marca un producto como inactivo (eliminación lógica).
**Autorización:** Requiere rol ADMIN o VENDEDOR.
**Parámetros de Ruta:**
- `id`: ID del producto

**Respuesta:** Devuelve un mensaje de éxito.

## Pagos

### Procesar pago
**Endpoint:** `POST /api/pagos/procesar`
**Descripción:** Procesa un pago para un pedido.
**Parámetros de Consulta:**
- `usuarioId`: ID del usuario
- `pedidoId`: ID del pedido
- `metodoPago`: Método de pago (TARJETA, PAYPAL, TRANSFERENCIA, etc.)

**Cuerpo de la Solicitud:**
```json
{
  "numeroTarjeta": "4111111111111111",
  "nombreTitular": "Juan Pérez",
  "fechaExpiracion": "12/25",
  "cvv": "123"
}
```
**Respuesta:** Devuelve el pedido pagado y un mensaje de éxito.

### Obtener información de pago
**Endpoint:** `GET /api/pagos/{pagoId}`
**Descripción:** Obtiene información sobre un pago.
**Parámetros de Ruta:**
- `pagoId`: ID del pago

**Respuesta:** Devuelve información del pago.

### Obtener historial de pagos del usuario
**Endpoint:** `GET /api/pagos/usuario/{usuarioId}`
**Descripción:** Obtiene el historial de pagos de un usuario.
**Parámetros de Ruta:**
- `usuarioId`: ID del usuario

**Respuesta:** Devuelve el historial de pagos del usuario.

### Procesar reembolso
**Endpoint:** `POST /api/pagos/reembolso/{pagoId}`
**Descripción:** Procesa un reembolso para un pago.
**Parámetros de Ruta:**
- `pagoId`: ID del pago

**Cuerpo de la Solicitud:**
```json
{
  "motivo": "Producto dañado",
  "montoReembolso": 99.99
}
```
**Respuesta:** Devuelve el resultado del reembolso.

## Reseñas

### Obtener reseñas por producto
**Endpoint:** `GET /api/reseñas/producto/{productoId}`
**Descripción:** Obtiene reseñas para un producto específico.
**Parámetros de Ruta:**
- `productoId`: ID del producto

**Parámetros de Consulta:**
- `page`: Número de página (predeterminado: 0)
- `size`: Tamaño de página (predeterminado: 10)
- `sort`: Campo de ordenación (predeterminado: "fechaCreacion")
- `direction`: Dirección de ordenación (predeterminado: "desc")

**Respuesta:** Devuelve una lista paginada de reseñas para el producto.

### Obtener reseñas por usuario
**Endpoint:** `GET /api/reseñas/usuario/{usuarioId}`
**Descripción:** Obtiene reseñas escritas por un usuario específico.
**Parámetros de Ruta:**
- `usuarioId`: ID del usuario

**Parámetros de Consulta:**
- `page`: Número de página (predeterminado: 0)
- `size`: Tamaño de página (predeterminado: 10)

**Respuesta:** Devuelve una lista paginada de reseñas escritas por el usuario.

### Crear reseña
**Endpoint:** `POST /api/reseñas`
**Descripción:** Crea una nueva reseña.
**Cuerpo de la Solicitud:**
```json
{
  "productoId": 1,
  "usuarioId": 1,
  "calificacion": 5,
  "comentario": "¡Gran producto!",
  "titulo": "Excelente"
}
```
**Respuesta:** Devuelve la reseña creada con un estado 201 Created.

### Actualizar reseña
**Endpoint:** `PUT /api/reseñas/{id}`
**Descripción:** Actualiza una reseña existente.
**Parámetros de Ruta:**
- `id`: ID de la reseña

**Cuerpo de la Solicitud:**
```json
{
  "calificacion": 4,
  "comentario": "Buen producto, pero podría ser mejor",
  "titulo": "Bueno"
}
```
**Respuesta:** Devuelve la reseña actualizada.

### Eliminar reseña
**Endpoint:** `DELETE /api/reseñas/{id}`
**Descripción:** Elimina una reseña.
**Parámetros de Ruta:**
- `id`: ID de la reseña

**Respuesta:** Devuelve un estado 204 No Content.

## Administración de Tiendas

### Obtener todas las tiendas
**Endpoint:** `GET /api/admin/tiendas`
**Descripción:** Obtiene una lista paginada de todas las tiendas.
**Autorización:** Requiere rol ADMIN.
**Parámetros de Consulta:**
- `page`: Número de página (predeterminado: 0)
- `size`: Tamaño de página (predeterminado: 10)
- `sort`: Campo de ordenación (predeterminado: "nombre")

**Respuesta:** Devuelve una lista paginada de tiendas.

### Obtener tienda por ID
**Endpoint:** `GET /api/admin/tiendas/{id}`
**Descripción:** Obtiene una tienda por su ID.
**Autorización:** Requiere rol ADMIN.
**Parámetros de Ruta:**
- `id`: ID de la tienda

**Respuesta:** Devuelve la información de la tienda.

### Obtener tiendas por propietario
**Endpoint:** `GET /api/admin/tiendas/propietario/{propietarioId}`
**Descripción:** Obtiene tiendas propiedad de un usuario específico.
**Autorización:** Requiere rol ADMIN.
**Parámetros de Ruta:**
- `propietarioId`: ID del propietario

**Respuesta:** Devuelve una lista de tiendas propiedad del usuario.

### Crear tienda
**Endpoint:** `POST /api/admin/tiendas`
**Descripción:** Crea una nueva tienda.
**Autorización:** Requiere rol ADMIN.
**Cuerpo de la Solicitud:**
```json
{
  "nombre": "Nombre de la Tienda",
  "descripcion": "Descripción de la Tienda",
  "propietarioId": 1,
  "logoUrl": "http://ejemplo.com/logo.jpg",
  "direccion": "Calle Principal 123",
  "telefono": "123-456-7890",
  "email": "tienda@ejemplo.com"
}
```
**Respuesta:** Devuelve la tienda creada con un estado 201 Created.

### Actualizar tienda
**Endpoint:** `PUT /api/admin/tiendas/{id}`
**Descripción:** Actualiza una tienda existente.
**Autorización:** Requiere rol ADMIN.
**Parámetros de Ruta:**
- `id`: ID de la tienda

**Cuerpo de la Solicitud:**
```json
{
  "nombre": "Nombre Actualizado de la Tienda",
  "descripcion": "Descripción Actualizada de la Tienda",
  "logoUrl": "http://ejemplo.com/logo-actualizado.jpg",
  "direccion": "Calle Principal 456",
  "telefono": "987-654-3210",
  "email": "tienda-actualizada@ejemplo.com"
}
```
**Respuesta:** Devuelve la tienda actualizada.

### Desactivar tienda
**Endpoint:** `DELETE /api/admin/tiendas/{id}`
**Descripción:** Desactiva una tienda (eliminación lógica).
**Autorización:** Requiere rol ADMIN.
**Parámetros de Ruta:**
- `id`: ID de la tienda

**Respuesta:** Devuelve un estado 204 No Content.

### Agregar producto a tienda
**Endpoint:** `POST /api/admin/tiendas/{tiendaId}/productos`
**Descripción:** Agrega un producto a una tienda.
**Autorización:** Requiere rol ADMIN.
**Parámetros de Ruta:**
- `tiendaId`: ID de la tienda

**Cuerpo de la Solicitud:**
```json
{
  "nombre": "Nombre del Producto",
  "descripcion": "Descripción del Producto",
  "precio": 99.99,
  "stock": 100,
  "categoriaId": 1,
  "imagenUrl": "http://ejemplo.com/imagen.jpg"
}
```
**Respuesta:** Devuelve el producto creado con un estado 201 Created.

### Obtener productos de tienda
**Endpoint:** `GET /api/admin/tiendas/{tiendaId}/productos`
**Descripción:** Obtiene productos de una tienda específica.
**Autorización:** Requiere rol ADMIN.
**Parámetros de Ruta:**
- `tiendaId`: ID de la tienda

**Parámetros de Consulta:**
- `page`: Número de página (predeterminado: 0)
- `size`: Tamaño de página (predeterminado: 10)

**Respuesta:** Devuelve una lista paginada de productos de la tienda especificada.

## Monitoreo

### Verificación de salud de la API
**Endpoint:** `GET /api/test/ping`
**Descripción:** Verifica que la API está funcionando.
**Respuesta:** Devuelve un mensaje de éxito y una marca de tiempo.

### Obtener estadísticas de caché
**Endpoint:** `GET /api/admin/monitor/cache`
**Descripción:** Obtiene estadísticas sobre las cachés del sistema.
**Autorización:** Requiere rol ADMIN.
**Respuesta:** Devuelve estadísticas de caché.

### Limpiar cachés
**Endpoint:** `POST /api/admin/monitor/cache/clear`
**Descripción:** Limpia todas las cachés del sistema.
**Autorización:** Requiere rol ADMIN.
**Respuesta:** Devuelve un mensaje de éxito.

### Obtener información del sistema
**Endpoint:** `GET /api/admin/monitor/system`
**Descripción:** Obtiene información sobre el sistema, incluyendo uso de memoria, tiempo de actividad y configuración de JVM.
**Autorización:** Requiere rol ADMIN.
**Respuesta:** Devuelve información del sistema.

## Configuración

### Inicializar roles
**Endpoint:** `POST /api/setup/init-roles`
**Descripción:** Inicializa roles básicos en el sistema (CLIENTE, ADMIN, VENDEDOR).
**Respuesta:** Devuelve un mensaje de éxito y la lista de roles creados.

### Inicializar categorías
**Endpoint:** `POST /api/setup/init-categorias`
**Descripción:** Inicializa categorías básicas de productos.
**Respuesta:** Devuelve un mensaje de éxito.

## Manejo de Errores

Todos los endpoints siguen un patrón consistente de manejo de errores:

- **400 Bad Request**: Datos de entrada inválidos
- **401 Unauthorized**: Se requiere autenticación
- **403 Forbidden**: Permisos insuficientes
- **404 Not Found**: Recurso no encontrado
- **500 Internal Server Error**: Error del lado del servidor

Las respuestas de error incluyen:
```json
{
  "path": "/api/ruta/al/recurso",
  "error": "Tipo de error",
  "message": "Mensaje detallado del error",
  "status": 400
}
```

## Formato de Respuesta

Las respuestas exitosas siguen un formato consistente:
```json
{
  "success": true,
  "message": "Operación exitosa",
  "data": {
    // Datos de respuesta
  }
}
```

## Autenticación

La mayoría de los endpoints requieren autenticación. Incluye el token JWT en el encabezado Authorization:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Paginación

Las respuestas paginadas incluyen:
```json
{
  "content": [
    // Elementos
  ],
  "currentPage": 0,
  "totalItems": 100,
  "totalPages": 10
}
```
