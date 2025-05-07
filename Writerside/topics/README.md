# Plataforma de E-commerce Multi-Tienda

## Descripción General

Esta plataforma de e-commerce es una solución completa para la gestión de múltiples tiendas en un solo portal. Permite a los comerciantes crear sus propias tiendas y vender productos, mientras que los clientes pueden navegar por el catálogo, agregar productos al carrito, realizar pagos y dejar reseñas.

### Características Principales

- **Multi-tienda**: Soporte para múltiples vendedores con sus propias tiendas.
- **Catálogo de Productos**: Gestión completa de productos con categorías, imágenes y descripciones.
- **Carrito de Compras**: Funcionalidad completa de carrito con actualización de cantidades y cálculo de totales.
- **Procesamiento de Pagos**: Integración con múltiples métodos de pago.
- **Reseñas y Calificaciones**: Sistema de reseñas para productos con calificaciones de 1 a 5 estrellas.
- **Gestión de Usuarios**: Registro, inicio de sesión y perfiles de usuario.
- **Panel de Administración**: Herramientas para que los vendedores gestionen sus tiendas y productos.

## Arquitectura

El proyecto sigue una arquitectura de capas estándar para aplicaciones Spring Boot:

- **Controladores**: Manejan las solicitudes HTTP y devuelven respuestas.
- **Servicios**: Contienen la lógica de negocio.
- **Repositorios**: Proporcionan acceso a la base de datos.
- **Modelos**: Definen las entidades y DTOs (Data Transfer Objects).

## Tecnologías Utilizadas

- **Backend**: Spring Boot, Spring Data JPA, Spring Security
- **Base de Datos**: PostgreSQL
- **Documentación**: Markdown

## Módulos

### 1. Autenticación y Usuarios
Gestión de usuarios, registro, inicio de sesión y perfiles.

### 2. Catálogo
Gestión de productos, categorías y búsqueda.

### 3. Carrito
Funcionalidad de carrito de compras.

### 4. Pagos
Procesamiento de pagos y reembolsos.

### 5. Reseñas
Sistema de reseñas y calificaciones para productos.

### 6. Administración de Tiendas
Herramientas para que los vendedores gestionen sus tiendas y productos.

## API REST

La plataforma expone una API REST completa para interactuar con todas las funcionalidades. A continuación se presenta un resumen de los principales endpoints:

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

Para una documentación detallada de la API, consulte el archivo [docs/api_documentation.md](api_documentation.md).

## Configuración y Ejecución

### Requisitos Previos
- Java 17 o superior
- Maven
- PostgreSQL

### Pasos para Ejecutar

1. Clone el repositorio:
   ```
   git clone https://github.com/tu-usuario/e-commerce.git
   cd e-commerce
   ```

2. Configure la base de datos en `src/main/resources/application.properties`.

3. Compile y ejecute la aplicación:
   ```
   mvn clean install
   mvn spring-boot:run
   ```

4. La API estará disponible en `http://localhost:8080/api`.

## Desarrollo Frontend

Para el desarrollo del frontend, se recomienda utilizar React con Next.js por su rendimiento optimizado para e-commerce, SEO mejorado y escalabilidad. Otras opciones viables incluyen Vue.js con Nuxt.js o Angular.

## Contribución

Las contribuciones son bienvenidas. Por favor, siga estos pasos:

1. Fork el repositorio
2. Cree una rama para su funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. Realice sus cambios y haga commit (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Cree un Pull Request

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - vea el archivo LICENSE para más detalles.
