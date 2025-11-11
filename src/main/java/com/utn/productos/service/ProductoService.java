package com.utn.productos.service;

import com.utn.productos.model.Categoria;
import com.utn.productos.model.Producto;
import com.utn.productos.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Inyección por constructor (recomendada)
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // ----- CRUD -----
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public List<Producto> obtenerPorCategoria(Categoria categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        return productoRepository.findById(id).map(p -> {
            p.setNombre(productoActualizado.getNombre());
            p.setDescripcion(productoActualizado.getDescripcion());
            p.setPrecio(productoActualizado.getPrecio());
            p.setStock(productoActualizado.getStock());
            p.setCategoria(productoActualizado.getCategoria());
            return productoRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Producto con ID " + id + " no encontrado"));
    }

    public Producto actualizarStock(Long id, Integer nuevoStock) {
        return productoRepository.findById(id).map(p -> {
            p.setStock(nuevoStock);
            return productoRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Producto con ID " + id + " no encontrado"));
    }

    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto con ID " + id + " no encontrado");
        }
        productoRepository.deleteById(id);
    }
}
