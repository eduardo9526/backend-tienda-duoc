package cl.duoc.backend_tienda.controller;

import cl.duoc.backend_tienda.model.*;
import cl.duoc.backend_tienda.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TiendaController {

    private final ProductoRepository productoRepo;
    private final CategoriaRepository categoriaRepo;
    private final BoletaRepository boletaRepo;
    private final UsuarioRepository usuarioRepo;

    public TiendaController(ProductoRepository productoRepo, CategoriaRepository categoriaRepo,
                            BoletaRepository boletaRepo, UsuarioRepository usuarioRepo) {
        this.productoRepo = productoRepo;
        this.categoriaRepo = categoriaRepo;
        this.boletaRepo = boletaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping("/categorias")
    public List<Categoria> listarCategorias() {
        return categoriaRepo.findAll();
    }

    @GetMapping("/productos")
    public List<Producto> listarProductos() {
        return productoRepo.findAll();
    }

    @PostMapping("/productos")
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoRepo.save(producto);
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto datos) {
        return productoRepo.findById(id).map(prod -> {
            prod.setNombre(datos.getNombre());
            prod.setDescripcion(datos.getDescripcion());
            prod.setPrecio(datos.getPrecio());
            prod.setStock(datos.getStock());
            if (datos.getCategoria() != null) {
                prod.setCategoria(datos.getCategoria());
            }
            return ResponseEntity.ok(productoRepo.save(prod));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (!productoRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/boletas")
    public List<Boleta> listarBoletas() {
        return boletaRepo.findAll();
    }

    @PostMapping("/boletas")
    public ResponseEntity<Boleta> crearBoleta(@RequestBody BoletaRequest request) {
        Usuario usuario = usuarioRepo.findById(request.idUsuario()).orElseThrow();

        Boleta boleta = new Boleta();
        boleta.setUsuario(usuario);
        boleta.setTotal(request.total());

        List<DetalleBoleta> detalles = new ArrayList<>();
        for (DetalleRequest d : request.detalles()) {
            Producto prod = productoRepo.findById(d.idProducto()).orElseThrow();
            prod.setStock(Math.max(0, prod.getStock() - d.cantidad()));
            productoRepo.save(prod);

            DetalleBoleta det = new DetalleBoleta();
            det.setBoleta(boleta);
            det.setProducto(prod);
            det.setCantidad(d.cantidad());
            det.setPrecioUnitario(d.precioUnitario());
            detalles.add(det);
        }

        boleta.setDetalles(detalles);
        return ResponseEntity.status(201).body(boletaRepo.save(boleta));
    }

    public record BoletaRequest(Long idUsuario, Double total, List<DetalleRequest> detalles) {}
    public record DetalleRequest(Long idProducto, Integer cantidad, Double precioUnitario) {}
}
