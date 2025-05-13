# E-Commerce API Documentation

This document provides comprehensive documentation for all endpoints available in the E-Commerce API. Use this as a reference when implementing the API in web applications, mobile apps, or any other platform.

## Base URL

```
http://localhost:8080/api
```

## Authentication

The API supports multiple authentication methods:

### Simple Authentication

#### Register a new user
**Endpoint:** `POST /api/auth-simple/registro`  
**Description:** Registers a new user in the system.  
**Request Body:**
```json
{
  "email": "user@example.com",
  "nombre": "John",
  "apellido": "Doe",
  "password": "password123",
  "roles": ["CLIENTE"]
}
```
**Response:** Returns the created user with a 201 Created status.

#### Login
**Endpoint:** `POST /api/auth-simple/login`  
**Description:** Authenticates a user and returns user information.  
**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
**Response:** Returns user information and a success message.

#### Alternative Login
**Endpoint:** `POST /api/auth-simple/login-alt`  
**Description:** Alternative login method using query parameters.  
**Query Parameters:**
- `email`: User's email
- `password`: User's password  

**Response:** Returns user information and a success message.

### JWT Authentication

#### Register with JWT
**Endpoint:** `POST /api/auth/jwt/registro`  
**Description:** Registers a new user and returns a JWT token.  
**Request Body:**
```json
{
  "email": "user@example.com",
  "nombre": "John",
  "apellido": "Doe",
  "password": "password123",
  "roles": ["CLIENTE"]
}
```
**Response:** Returns the created user and a JWT token.

#### Login with JWT
**Endpoint:** `POST /api/auth/jwt/login`  
**Description:** Authenticates a user and returns a JWT token.  
**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
**Response:** Returns user information and a JWT token.

### Standard Authentication

#### Register
**Endpoint:** `POST /api/auth/registro`  
**Description:** Registers a new user.  
**Request Body:**
```json
{
  "email": "user@example.com",
  "nombre": "John",
  "apellido": "Doe",
  "password": "password123",
  "roles": ["CLIENTE"]
}
```
**Response:** Returns the created user with a 201 Created status.

#### Login
**Endpoint:** `POST /api/auth/login`  
**Description:** Authenticates a user.  
**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
**Response:** Returns user information and a success message.

#### Get User Profile
**Endpoint:** `GET /api/auth/perfil`  
**Description:** Gets the profile of a user.  
**Query Parameters:**
- `usuarioId`: ID of the user  

**Response:** Returns user information.

#### Update User Profile
**Endpoint:** `PUT /api/auth/perfil/{id}`  
**Description:** Updates the profile of a user.  
**Path Parameters:**
- `id`: ID of the user  

**Request Body:**
```json
{
  "nombre": "John",
  "apellido": "Doe",
  "email": "user@example.com"
}
```
**Response:** Returns the updated user information.

## Catalog

### Get All Products
**Endpoint:** `GET /api/catalogo/productos`  
**Description:** Gets a paginated list of all products.  
**Query Parameters:**
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)
- `sort`: Sort field (default: "id")  

**Response:** Returns a paginated list of products.

### Get Product by ID
**Endpoint:** `GET /api/catalogo/productos/{id}`  
**Description:** Gets a product by its ID.  
**Path Parameters:**
- `id`: ID of the product  

**Response:** Returns the product information.

### Search Products
**Endpoint:** `GET /api/catalogo/productos/buscar`  
**Description:** Searches for products by keyword.  
**Query Parameters:**
- `keyword`: Search term
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)  

**Response:** Returns a paginated list of products matching the search term.

### Get Products by Store
**Endpoint:** `GET /api/catalogo/tiendas/{tiendaId}/productos`  
**Description:** Gets products from a specific store.  
**Path Parameters:**
- `tiendaId`: ID of the store  

**Query Parameters:**
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)  

**Response:** Returns a paginated list of products from the specified store.

### Get Products by Category
**Endpoint:** `GET /api/catalogo/categorias/{categoriaId}/productos`  
**Description:** Gets products from a specific category.  
**Path Parameters:**
- `categoriaId`: ID of the category  

**Query Parameters:**
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)  

**Response:** Returns a paginated list of products from the specified category.

## Shopping Cart

### Get User's Cart
**Endpoint:** `GET /api/carrito/{usuarioId}`  
**Description:** Gets the shopping cart of a user.  
**Path Parameters:**
- `usuarioId`: ID of the user  

**Response:** Returns the user's shopping cart.

### Add Product to Cart
**Endpoint:** `POST /api/carrito/agregar`  
**Description:** Adds a product to the user's shopping cart.  
**Query Parameters:**
- `usuarioId`: ID of the user
- `productoId`: ID of the product
- `cantidad`: Quantity to add  

**Response:** Returns the updated shopping cart.

### Update Product Quantity
**Endpoint:** `PUT /api/carrito/actualizar`  
**Description:** Updates the quantity of a product in the user's shopping cart.  
**Query Parameters:**
- `usuarioId`: ID of the user
- `productoId`: ID of the product
- `cantidad`: New quantity  

**Response:** Returns the updated shopping cart.

### Remove Product from Cart
**Endpoint:** `DELETE /api/carrito/eliminar`  
**Description:** Removes a product from the user's shopping cart.  
**Query Parameters:**
- `usuarioId`: ID of the user
- `productoId`: ID of the product  

**Response:** Returns the updated shopping cart.

### Empty Cart
**Endpoint:** `DELETE /api/carrito/vaciar/{usuarioId}`  
**Description:** Empties the user's shopping cart.  
**Path Parameters:**
- `usuarioId`: ID of the user  

**Response:** Returns the empty shopping cart.

## Products Management

### Create Product
**Endpoint:** `POST /api/productos`  
**Description:** Creates a new product.  
**Authorization:** Requires ADMIN or VENDEDOR role.  
**Request Body:**
```json
{
  "nombre": "Product Name",
  "descripcion": "Product Description",
  "precio": 99.99,
  "stock": 100,
  "categoriaId": 1,
  "tiendaId": 1,
  "imagenUrl": "http://example.com/image.jpg"
}
```
**Response:** Returns the created product with a 201 Created status.

### Update Product
**Endpoint:** `PUT /api/productos/{id}`  
**Description:** Updates an existing product.  
**Authorization:** Requires ADMIN or VENDEDOR role.  
**Path Parameters:**
- `id`: ID of the product  

**Request Body:**
```json
{
  "nombre": "Updated Product Name",
  "descripcion": "Updated Product Description",
  "precio": 129.99,
  "stock": 50,
  "categoriaId": 1,
  "imagenUrl": "http://example.com/updated-image.jpg"
}
```
**Response:** Returns the updated product.

### Delete Product
**Endpoint:** `DELETE /api/productos/{id}`  
**Description:** Marks a product as inactive (soft delete).  
**Authorization:** Requires ADMIN or VENDEDOR role.  
**Path Parameters:**
- `id`: ID of the product  

**Response:** Returns a success message.

## Payments

### Process Payment
**Endpoint:** `POST /api/pagos/procesar`  
**Description:** Processes a payment for an order.  
**Query Parameters:**
- `usuarioId`: ID of the user
- `pedidoId`: ID of the order
- `metodoPago`: Payment method (TARJETA, PAYPAL, TRANSFERENCIA, etc.)  

**Request Body:**
```json
{
  "numeroTarjeta": "4111111111111111",
  "nombreTitular": "John Doe",
  "fechaExpiracion": "12/25",
  "cvv": "123"
}
```
**Response:** Returns the paid order and a success message.

### Get Payment Information
**Endpoint:** `GET /api/pagos/{pagoId}`  
**Description:** Gets information about a payment.  
**Path Parameters:**
- `pagoId`: ID of the payment  

**Response:** Returns payment information.

### Get User's Payment History
**Endpoint:** `GET /api/pagos/usuario/{usuarioId}`  
**Description:** Gets the payment history of a user.  
**Path Parameters:**
- `usuarioId`: ID of the user  

**Response:** Returns the user's payment history.

### Process Refund
**Endpoint:** `POST /api/pagos/reembolso/{pagoId}`  
**Description:** Processes a refund for a payment.  
**Path Parameters:**
- `pagoId`: ID of the payment  

**Request Body:**
```json
{
  "motivo": "Product damaged",
  "montoReembolso": 99.99
}
```
**Response:** Returns the refund result.

## Reviews

### Get Reviews by Product
**Endpoint:** `GET /api/reseñas/producto/{productoId}`  
**Description:** Gets reviews for a specific product.  
**Path Parameters:**
- `productoId`: ID of the product  

**Query Parameters:**
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)
- `sort`: Sort field (default: "fechaCreacion")
- `direction`: Sort direction (default: "desc")  

**Response:** Returns a paginated list of reviews for the product.

### Get Reviews by User
**Endpoint:** `GET /api/reseñas/usuario/{usuarioId}`  
**Description:** Gets reviews written by a specific user.  
**Path Parameters:**
- `usuarioId`: ID of the user  

**Query Parameters:**
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)  

**Response:** Returns a paginated list of reviews written by the user.

### Create Review
**Endpoint:** `POST /api/reseñas`  
**Description:** Creates a new review.  
**Request Body:**
```json
{
  "productoId": 1,
  "usuarioId": 1,
  "calificacion": 5,
  "comentario": "Great product!",
  "titulo": "Excellent"
}
```
**Response:** Returns the created review with a 201 Created status.

### Update Review
**Endpoint:** `PUT /api/reseñas/{id}`  
**Description:** Updates an existing review.  
**Path Parameters:**
- `id`: ID of the review  

**Request Body:**
```json
{
  "calificacion": 4,
  "comentario": "Good product, but could be better",
  "titulo": "Good"
}
```
**Response:** Returns the updated review.

### Delete Review
**Endpoint:** `DELETE /api/reseñas/{id}`  
**Description:** Deletes a review.  
**Path Parameters:**
- `id`: ID of the review  

**Response:** Returns a 204 No Content status.

## Store Administration

### Get All Stores
**Endpoint:** `GET /api/admin/tiendas`  
**Description:** Gets a paginated list of all stores.  
**Authorization:** Requires ADMIN role.  
**Query Parameters:**
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)
- `sort`: Sort field (default: "nombre")  

**Response:** Returns a paginated list of stores.

### Get Store by ID
**Endpoint:** `GET /api/admin/tiendas/{id}`  
**Description:** Gets a store by its ID.  
**Authorization:** Requires ADMIN role.  
**Path Parameters:**
- `id`: ID of the store  

**Response:** Returns the store information.

### Get Stores by Owner
**Endpoint:** `GET /api/admin/tiendas/propietario/{propietarioId}`  
**Description:** Gets stores owned by a specific user.  
**Authorization:** Requires ADMIN role.  
**Path Parameters:**
- `propietarioId`: ID of the owner  

**Response:** Returns a list of stores owned by the user.

### Create Store
**Endpoint:** `POST /api/admin/tiendas`  
**Description:** Creates a new store.  
**Authorization:** Requires ADMIN role.  
**Request Body:**
```json
{
  "nombre": "Store Name",
  "descripcion": "Store Description",
  "propietarioId": 1,
  "logoUrl": "http://example.com/logo.jpg",
  "direccion": "123 Main St",
  "telefono": "123-456-7890",
  "email": "store@example.com"
}
```
**Response:** Returns the created store with a 201 Created status.

### Update Store
**Endpoint:** `PUT /api/admin/tiendas/{id}`  
**Description:** Updates an existing store.  
**Authorization:** Requires ADMIN role.  
**Path Parameters:**
- `id`: ID of the store  

**Request Body:**
```json
{
  "nombre": "Updated Store Name",
  "descripcion": "Updated Store Description",
  "logoUrl": "http://example.com/updated-logo.jpg",
  "direccion": "456 Main St",
  "telefono": "987-654-3210",
  "email": "updated-store@example.com"
}
```
**Response:** Returns the updated store.

### Deactivate Store
**Endpoint:** `DELETE /api/admin/tiendas/{id}`  
**Description:** Deactivates a store (soft delete).  
**Authorization:** Requires ADMIN role.  
**Path Parameters:**
- `id`: ID of the store  

**Response:** Returns a 204 No Content status.

### Add Product to Store
**Endpoint:** `POST /api/admin/tiendas/{tiendaId}/productos`  
**Description:** Adds a product to a store.  
**Authorization:** Requires ADMIN role.  
**Path Parameters:**
- `tiendaId`: ID of the store  

**Request Body:**
```json
{
  "nombre": "Product Name",
  "descripcion": "Product Description",
  "precio": 99.99,
  "stock": 100,
  "categoriaId": 1,
  "imagenUrl": "http://example.com/image.jpg"
}
```
**Response:** Returns the created product with a 201 Created status.

### Get Store Products
**Endpoint:** `GET /api/admin/tiendas/{tiendaId}/productos`  
**Description:** Gets products from a specific store.  
**Authorization:** Requires ADMIN role.  
**Path Parameters:**
- `tiendaId`: ID of the store  

**Query Parameters:**
- `page`: Page number (default: 0)
- `size`: Page size (default: 10)  

**Response:** Returns a paginated list of products from the specified store.

## Monitoring

### API Health Check
**Endpoint:** `GET /api/test/ping`  
**Description:** Verifies that the API is running.  
**Response:** Returns a success message and timestamp.

### Get Cache Statistics
**Endpoint:** `GET /api/admin/monitor/cache`  
**Description:** Gets statistics about the system's caches.  
**Authorization:** Requires ADMIN role.  
**Response:** Returns cache statistics.

### Clear Caches
**Endpoint:** `POST /api/admin/monitor/cache/clear`  
**Description:** Clears all caches in the system.  
**Authorization:** Requires ADMIN role.  
**Response:** Returns a success message.

### Get System Information
**Endpoint:** `GET /api/admin/monitor/system`  
**Description:** Gets information about the system, including memory usage, uptime, and JVM configuration.  
**Authorization:** Requires ADMIN role.  
**Response:** Returns system information.

## Setup

### Initialize Roles
**Endpoint:** `POST /api/setup/init-roles`  
**Description:** Initializes basic roles in the system (CLIENTE, ADMIN, VENDEDOR).  
**Response:** Returns a success message and the list of created roles.

### Initialize Categories
**Endpoint:** `POST /api/setup/init-categorias`  
**Description:** Initializes basic product categories.  
**Response:** Returns a success message.

## Error Handling

All endpoints follow a consistent error handling pattern:

- **400 Bad Request**: Invalid input data
- **401 Unauthorized**: Authentication required
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server-side error

Error responses include:
```json
{
  "path": "/api/path/to/resource",
  "error": "Error type",
  "message": "Detailed error message",
  "status": 400
}
```

## Response Format

Successful responses follow a consistent format:
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    // Response data
  }
}
```

## Authentication

Most endpoints require authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Pagination

Paginated responses include:
```json
{
  "content": [
    // Items
  ],
  "currentPage": 0,
  "totalItems": 100,
  "totalPages": 10
}
```
