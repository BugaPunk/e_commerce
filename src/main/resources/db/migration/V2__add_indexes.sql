-- Índices para la tabla producto
CREATE INDEX IF NOT EXISTS idx_producto_activo ON producto(activo);
CREATE INDEX IF NOT EXISTS idx_producto_tienda_id ON producto(tienda_id);
CREATE INDEX IF NOT EXISTS idx_producto_categoria_id ON producto(categoria_id);
CREATE INDEX IF NOT EXISTS idx_producto_nombre ON producto(nombre);
CREATE INDEX IF NOT EXISTS idx_producto_precio ON producto(precio);

-- Índices para la tabla usuario
CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuario(email);
CREATE INDEX IF NOT EXISTS idx_usuario_activo ON usuario(activo);

-- Índices para la tabla pedido
CREATE INDEX IF NOT EXISTS idx_pedido_usuario_id ON pedido(usuario_id);
CREATE INDEX IF NOT EXISTS idx_pedido_estado ON pedido(estado);
CREATE INDEX IF NOT EXISTS idx_pedido_fecha ON pedido(fecha);

-- Índices para la tabla detalle_pedido
CREATE INDEX IF NOT EXISTS idx_detalle_pedido_pedido_id ON detalle_pedido(pedido_id);
CREATE INDEX IF NOT EXISTS idx_detalle_pedido_producto_id ON detalle_pedido(producto_id);

-- Índices para la tabla reseña
CREATE INDEX IF NOT EXISTS idx_reseña_producto_id ON reseña(producto_id);
CREATE INDEX IF NOT EXISTS idx_reseña_usuario_id ON reseña(usuario_id);
CREATE INDEX IF NOT EXISTS idx_reseña_calificacion ON reseña(calificacion);

-- Índices para la tabla carrito
CREATE INDEX IF NOT EXISTS idx_carrito_usuario_id ON carrito(usuario_id);

-- Índices para la tabla item_carrito
CREATE INDEX IF NOT EXISTS idx_item_carrito_carrito_id ON item_carrito(carrito_id);
CREATE INDEX IF NOT EXISTS idx_item_carrito_producto_id ON item_carrito(producto_id);
