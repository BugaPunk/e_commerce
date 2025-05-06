package com.bugabuga.e_commerce.controller; // Define el paquete al que pertenece esta clase

import com.bugabuga.e_commerce.model.dto.CarritoDTO; // Importa la clase CarritoDTO del paquete model.dto
import com.bugabuga.e_commerce.service.CarritoService; // Importa el servicio CarritoService del paquete service
import jakarta.transaction.Transactional; // Importa la anotación Transactional para manejo de transacciones
import jakarta.validation.Valid; // Importa la anotación Valid para validación de datos
import org.slf4j.Logger; // Importa la interfaz Logger para registro de eventos
import org.slf4j.LoggerFactory; // Importa la clase LoggerFactory para crear instancias de Logger
import org.springframework.beans.factory.annotation.Autowired; // Importa la anotación Autowired para inyección de dependencias
import org.springframework.http.HttpStatus; // Importa la clase HttpStatus para manejar códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importa la clase ResponseEntity para manejar respuestas HTTP
import org.springframework.validation.annotation.Validated; // Importa la anotación Validated para validación a nivel de clase
import org.springframework.web.bind.annotation.*; // Importa todas las anotaciones de Spring para controladores web

@RestController // Anotación que indica que esta clase es un controlador REST de Spring
@RequestMapping("/api/carrito") // Define la ruta base para las solicitudes HTTP a este controlador
@Validated // Anotación que indica que esta clase debe ser validada
public class CarritoController { // Define la clase CarritoController

    private final CarritoService carritoService; // Declara una variable final para el servicio de carritos
    private static final Logger logger = LoggerFactory.getLogger(CarritoController.class); // Crea una instancia de Logger para esta clase

    @Autowired // Anotación que indica que el constructor debe ser usado para inyección de dependencias
    public CarritoController(CarritoService carritoService) { // Constructor que recibe el servicio de carritos
        this.carritoService = carritoService; // Asigna el servicio de carritos a la variable de instancia
    }

    @GetMapping("/{usuarioId}") // Anotación que indica que este método maneja solicitudes GET con un parámetro de ruta
    public ResponseEntity<CarritoDTO> obtenerCarritoPorUsuario(@PathVariable Long usuarioId) { // Método para obtener el carrito de un usuario
        long inicio = System.currentTimeMillis(); // Registra el tiempo de inicio para medir el rendimiento
        logger.info("[CARRITO] Inicio obtenerCarritoPorUsuario: {}", inicio); // Registra el inicio de la operación

        CarritoDTO carrito = carritoService.obtenerCarritoPorUsuarioId(usuarioId); // Llama al servicio para obtener el carrito del usuario

        long fin = System.currentTimeMillis(); // Registra el tiempo de finalización
        logger.info("[CARRITO] Fin obtenerCarritoPorUsuario: {} (Duración: {} ms)", fin, (fin-inicio)); // Registra el fin de la operación y su duración

        return ResponseEntity.ok(carrito); // Retorna una respuesta HTTP 200 OK con el carrito encontrado
    }

    @PostMapping("/agregar") // Anotación que indica que este método maneja solicitudes POST a la ruta /agregar
    @Transactional // Anotación que indica que este método debe ejecutarse dentro de una transacción
    public ResponseEntity<CarritoDTO> agregarProductoAlCarrito( // Método para agregar un producto al carrito
            @RequestParam Long usuarioId, // Parámetro de consulta para el ID del usuario
            @RequestParam Long productoId, // Parámetro de consulta para el ID del producto
            @RequestParam Integer cantidad) { // Parámetro de consulta para la cantidad del producto
        long inicio = System.currentTimeMillis(); // Registra el tiempo de inicio para medir el rendimiento
        logger.info("[CARRITO] Inicio agregarProductoAlCarrito: {}", inicio); // Registra el inicio de la operación

        CarritoDTO carrito = carritoService.agregarProductoAlCarrito(usuarioId, productoId, cantidad); // Llama al servicio para agregar el producto al carrito

        long fin = System.currentTimeMillis(); // Registra el tiempo de finalización
        logger.info("[CARRITO] Fin agregarProductoAlCarrito: {} (Duración: {} ms)", fin, (fin-inicio)); // Registra el fin de la operación y su duración

        return ResponseEntity.status(HttpStatus.OK).body(carrito); // Retorna una respuesta HTTP 200 OK con el carrito actualizado
    }

    @PutMapping("/actualizar") // Anotación que indica que este método maneja solicitudes PUT a la ruta /actualizar
    @Transactional // Anotación que indica que este método debe ejecutarse dentro de una transacción
    public ResponseEntity<CarritoDTO> actualizarCantidadProducto( // Método para actualizar la cantidad de un producto en el carrito
            @RequestParam Long usuarioId, // Parámetro de consulta para el ID del usuario
            @RequestParam Long productoId, // Parámetro de consulta para el ID del producto
            @RequestParam Integer cantidad) { // Parámetro de consulta para la nueva cantidad del producto
        long inicio = System.currentTimeMillis(); // Registra el tiempo de inicio para medir el rendimiento
        logger.info("[CARRITO] Inicio actualizarCantidadProducto: {}", inicio); // Registra el inicio de la operación

        CarritoDTO carrito = carritoService.actualizarCantidadProducto(usuarioId, productoId, cantidad); // Llama al servicio para actualizar la cantidad del producto

        long fin = System.currentTimeMillis(); // Registra el tiempo de finalización
        logger.info("[CARRITO] Fin actualizarCantidadProducto: {} (Duración: {} ms)", fin, (fin-inicio)); // Registra el fin de la operación y su duración

        return ResponseEntity.ok(carrito); // Retorna una respuesta HTTP 200 OK con el carrito actualizado
    }

    @DeleteMapping("/eliminar") // Anotación que indica que este método maneja solicitudes DELETE a la ruta /eliminar
    @Transactional // Anotación que indica que este método debe ejecutarse dentro de una transacción
    public ResponseEntity<CarritoDTO> eliminarProductoDelCarrito( // Método para eliminar un producto del carrito
            @RequestParam Long usuarioId, // Parámetro de consulta para el ID del usuario
            @RequestParam Long productoId) { // Parámetro de consulta para el ID del producto
        long inicio = System.currentTimeMillis(); // Registra el tiempo de inicio para medir el rendimiento
        logger.info("[CARRITO] Inicio eliminarProductoDelCarrito: {}", inicio); // Registra el inicio de la operación

        CarritoDTO carrito = carritoService.eliminarProductoDelCarrito(usuarioId, productoId); // Llama al servicio para eliminar el producto del carrito

        long fin = System.currentTimeMillis(); // Registra el tiempo de finalización
        logger.info("[CARRITO] Fin eliminarProductoDelCarrito: {} (Duración: {} ms)", fin, (fin-inicio)); // Registra el fin de la operación y su duración

        return ResponseEntity.ok(carrito); // Retorna una respuesta HTTP 200 OK con el carrito actualizado
    }

    @DeleteMapping("/vaciar/{usuarioId}") // Anotación que indica que este método maneja solicitudes DELETE a la ruta /vaciar/{usuarioId}
    @Transactional // Anotación que indica que este método debe ejecutarse dentro de una transacción
    public ResponseEntity<CarritoDTO> vaciarCarrito(@PathVariable Long usuarioId) { // Método para vaciar el carrito de un usuario
        long inicio = System.currentTimeMillis(); // Registra el tiempo de inicio para medir el rendimiento
        logger.info("[CARRITO] Inicio vaciarCarrito: {}", inicio); // Registra el inicio de la operación

        CarritoDTO carrito = carritoService.vaciarCarrito(usuarioId); // Llama al servicio para vaciar el carrito

        long fin = System.currentTimeMillis(); // Registra el tiempo de finalización
        logger.info("[CARRITO] Fin vaciarCarrito: {} (Duración: {} ms)", fin, (fin-inicio)); // Registra el fin de la operación y su duración

        return ResponseEntity.ok(carrito); // Retorna una respuesta HTTP 200 OK con el carrito vacío
    }
}
