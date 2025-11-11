package com.utn.productos.controller;

import com.utn.productos.dto.ActualizarStockDTO;
import com.utn.productos.dto.ProductoDTO;
import com.utn.productos.dto.ProductoResponseDTO;
import com.utn.productos.exception.ProductoNotFoundException;
import com.utn.productos.model.Categoria;
import com.utn.productos.model.Producto;
import com.utn.productos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // ===============================
    // GET: Listar todos los productos
    // ===============================
    @GetMapping
    public List<ProductoResponseDTO> obtenerTodos() {
        return productoService.obtenerTodos()
                .stream()
                .map(p -> new ProductoResponseDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getCategoria()))
                .toList();
    }

    // ==================================
    // GET: Obtener producto por ID
    // ==================================
    @GetMapping("/{id}")
    public ProductoResponseDTO obtenerPorId(@PathVariable Long id) {
        Producto p = productoService.obtenerPorId(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));
        return new ProductoResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getStock(),
                p.getCategoria()
        );
    }

    // ==================================
    // GET: Filtrar por categoría
    // ==================================
    @GetMapping("/categoria/{categoria}")
    public List<ProductoResponseDTO> obtenerPorCategoria(@PathVariable Categoria categoria) {
        return productoService.obtenerPorCategoria(categoria)
                .stream()
                .map(p -> new ProductoResponseDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getCategoria()))
                .toList();
    }

    // ==================================
    // POST: Crear nuevo producto
    // ==================================
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoDTO dto) {
        Producto nuevo = new Producto(
                null,
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getPrecio(),
                dto.getStock(),
                dto.getCategoria()
        );
        Producto guardado = productoService.crearProducto(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ProductoResponseDTO(
                        guardado.getId(),
                        guardado.getNombre(),
                        guardado.getDescripcion(),
                        guardado.getPrecio(),
                        guardado.getStock(),
                        guardado.getCategoria()
                ));
    }

    // ==================================
    // PUT: Actualizar producto completo
    // ==================================
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody ProductoDTO dto) {
        Producto actualizado = new Producto(
                id,
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getPrecio(),
                dto.getStock(),
                dto.getCategoria()
        );
        Producto guardado = productoService.actualizarProducto(id, actualizado);
        return ResponseEntity.ok(new ProductoResponseDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getDescripcion(),
                guardado.getPrecio(),
                guardado.getStock(),
                guardado.getCategoria()
        ));
    }

    // ==================================
    // PATCH: Actualizar solo el stock
    // ==================================
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductoResponseDTO> actualizarStock(@PathVariable Long id,
                                                               @Valid @RequestBody ActualizarStockDTO dto) {
        Producto actualizado = productoService.actualizarStock(id, dto.getStock());
        return ResponseEntity.ok(new ProductoResponseDTO(
                actualizado.getId(),
                actualizado.getNombre(),
                actualizado.getDescripcion(),
                actualizado.getPrecio(),
                actualizado.getStock(),
                actualizado.getCategoria()
        ));
    }

    // ==================================
    // DELETE: Eliminar producto
    // ==================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
