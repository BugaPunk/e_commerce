package com.bugabuga.e_commerce.service;

import com.bugabuga.e_commerce.config.CacheConfig;
import com.bugabuga.e_commerce.exception.ResourceNotFoundException;
import com.bugabuga.e_commerce.model.dto.ProductoActualizacionDTO;
import com.bugabuga.e_commerce.model.dto.ProductoCreacionDTO;
import com.bugabuga.e_commerce.model.dto.ProductoDTO;
import com.bugabuga.e_commerce.model.entity.Categoria;
import com.bugabuga.e_commerce.model.entity.Producto;
import com.bugabuga.e_commerce.model.entity.Tienda;
import com.bugabuga.e_commerce.repository.CategoriaRepository;
import com.bugabuga.e_commerce.repository.ProductoRepository;
import com.bugabuga.e_commerce.repository.ReseñaRepository;
import com.bugabuga.e_commerce.repository.TiendaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;
    private final TiendaRepository tiendaRepository;
    private final CategoriaRepository categoriaRepository;
    private final ReseñaRepository reseñaRepository;
    private final MappingService mappingService;

    @Autowired
    public ProductoService(ProductoRepository productoRepository,
                          TiendaRepository tiendaRepository,
                          CategoriaRepository categoriaRepository,
                          ReseñaRepository reseñaRepository,
                          MappingService mappingService) {
        this.productoRepository = productoRepository;
        this.tiendaRepository = tiendaRepository;
        this.categoriaRepository = categoriaRepository;
        this.reseñaRepository = reseñaRepository;
        this.mappingService = mappingService;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.CACHE_PRODUCTOS, key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    public Page<ProductoDTO> obtenerTodosLosProductos(Pageable pageable) {
        logger.debug("Obteniendo todos los productos, página: {}, tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Producto> productos = productoRepository.findByActivoTrue(pageable);

        logger.debug("Encontrados {} productos", productos.getTotalElements());
        return productos.map(mappingService::mapToProductoDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.CACHE_PRODUCTO, key = "#id")
    public ProductoDTO obtenerProductoPorId(Long id) {
        logger.debug("Buscando producto con id: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        if (!producto.isActivo()) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }

        logger.debug("Producto encontrado: {}", producto.getNombre());
        return mappingService.mapToProductoDTO(producto);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = CacheConfig.CACHE_PRODUCTOS, allEntries = true),
        @CacheEvict(value = CacheConfig.CACHE_PRODUCTOS_RECIENTES, allEntries = true),
        @CacheEvict(value = CacheConfig.CACHE_PRODUCTOS_POR_TIENDA, key = "#productoDTO.tiendaId"),
        @CacheEvict(value = CacheConfig.CACHE_PRODUCTOS_POR_CATEGORIA, key = "#productoDTO.categoriaId")
    })
    public ProductoDTO crearProducto(ProductoCreacionDTO productoDTO) {
        logger.debug("Creando nuevo producto: {}", productoDTO.getNombre());

        Tienda tienda = tiendaRepository.findById(productoDTO.getTiendaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con id: " + productoDTO.getTiendaId()));

        Categoria categoria = null;
        if (productoDTO.getCategoriaId() != null) {
            categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + productoDTO.getCategoriaId()));
        }

        // Usar el servicio de mapeo para convertir DTO a entidad
        Producto producto = mappingService.mapToProducto(productoDTO);
        producto.setTienda(tienda);
        producto.setCategoria(categoria);
        producto.setActivo(true);

        Producto productoGuardado = productoRepository.save(producto);
        logger.debug("Producto creado con id: {}", productoGuardado.getId());

        return mappingService.mapToProductoDTO(productoGuardado);
    }

    @Transactional
    @Caching(put = {
        @CachePut(value = CacheConfig.CACHE_PRODUCTO, key = "#id")
    }, evict = {
        @CacheEvict(value = CacheConfig.CACHE_PRODUCTOS, allEntries = true),
        @CacheEvict(value = CacheConfig.CACHE_PRODUCTOS_RECIENTES, allEntries = true),
        @CacheEvict(value = CacheConfig.CACHE_PRODUCTOS_POR_TIENDA, allEntries = true),
        @CacheEvict(value = CacheConfig.CACHE_PRODUCTOS_POR_CATEGORIA, allEntries = true)
    })
    public ProductoDTO actualizarProducto(Long id, ProductoActualizacionDTO productoDTO) {
        logger.debug("Actualizando producto con id: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        // Actualizar categoría si se proporciona
        if (productoDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + productoDTO.getCategoriaId()));
            producto.setCategoria(categoria);
        }

        // Usar el servicio de mapeo para actualizar la entidad
        mappingService.updateProductoFromDTO(producto, productoDTO);

        Producto productoActualizado = productoRepository.save(producto);
        logger.debug("Producto actualizado: {}", productoActualizado.getNombre());

        return mappingService.mapToProductoDTO(productoActualizado);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = CacheConfig.CACHE_PRODUCTO, key = "#id"),
        @CacheEvict(value = CacheConfig.CACHE_PRODUCTOS, allEntries = true),
        @CacheEvict(value = CacheConfig.CACHE_PRODUCTOS_RECIENTES, allEntries = true),
        @CacheEvict(value = CacheConfig.CACHE_PRODUCTOS_POR_TIENDA, allEntries = true),
        @CacheEvict(value = CacheConfig.CACHE_PRODUCTOS_POR_CATEGORIA, allEntries = true)
    })
    public void eliminarProducto(Long id) {
        logger.debug("Eliminando producto con id: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        producto.setActivo(false);
        productoRepository.save(producto);

        logger.debug("Producto marcado como inactivo: {}", producto.getNombre());
    }

    @Transactional(readOnly = true)
    public Page<ProductoDTO> buscarProductos(String keyword, Pageable pageable) {
        logger.debug("Buscando productos con keyword: {}, página: {}", keyword, pageable.getPageNumber());

        Page<Producto> productos = productoRepository.buscarProductos(keyword, pageable);

        logger.debug("Encontrados {} productos para la búsqueda: {}", productos.getTotalElements(), keyword);
        return productos.map(mappingService::mapToProductoDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.CACHE_PRODUCTOS_POR_TIENDA, key = "#tiendaId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<ProductoDTO> obtenerProductosPorTienda(Long tiendaId, Pageable pageable) {
        logger.debug("Obteniendo productos de la tienda con id: {}, página: {}", tiendaId, pageable.getPageNumber());

        Page<Producto> productos = productoRepository.findByTiendaIdAndActivoTrue(tiendaId, pageable);

        logger.debug("Encontrados {} productos para la tienda: {}", productos.getTotalElements(), tiendaId);
        return productos.map(mappingService::mapToProductoDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.CACHE_PRODUCTOS_POR_CATEGORIA, key = "#categoriaId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<ProductoDTO> obtenerProductosPorCategoria(Long categoriaId, Pageable pageable) {
        logger.debug("Obteniendo productos de la categoría con id: {}, página: {}", categoriaId, pageable.getPageNumber());

        Page<Producto> productos = productoRepository.findByCategoriaIdAndActivoTrue(categoriaId, pageable);

        logger.debug("Encontrados {} productos para la categoría: {}", productos.getTotalElements(), categoriaId);
        return productos.map(mappingService::mapToProductoDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.CACHE_PRODUCTOS_RECIENTES)
    public List<ProductoDTO> obtenerProductosRecientes() {
        logger.debug("Obteniendo productos recientes");

        List<Producto> productos = productoRepository.findTop10ByActivoTrueOrderByIdDesc();

        logger.debug("Encontrados {} productos recientes", productos.size());
        return mappingService.mapToProductoDTOList(productos);
    }


}
