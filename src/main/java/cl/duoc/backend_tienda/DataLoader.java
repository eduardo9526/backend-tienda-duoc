package cl.duoc.backend_tienda;

import cl.duoc.backend_tienda.model.*;
import cl.duoc.backend_tienda.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final CategoriaRepository categoriaRepo;
    private final ProductoRepository productoRepo;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UsuarioRepository usuarioRepo, CategoriaRepository categoriaRepo,
                      ProductoRepository productoRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.categoriaRepo = categoriaRepo;
        this.productoRepo = productoRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepo.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador Duoc");
            admin.setEmail("admin@duoc.cl");
            admin.setPassword(passwordEncoder.encode("123"));
            admin.setRol("ADMIN");
            usuarioRepo.save(admin);

            Usuario vendedor = new Usuario();
            vendedor.setNombre("Vendedor Tienda");
            vendedor.setEmail("vendedor@duoc.cl");
            vendedor.setPassword(passwordEncoder.encode("123"));
            vendedor.setRol("VENDEDOR");
            usuarioRepo.save(vendedor);

            Usuario cliente = new Usuario();
            cliente.setNombre("Cliente Prueba");
            cliente.setEmail("cliente@duoc.cl");
            cliente.setPassword(passwordEncoder.encode("123"));
            cliente.setRol("CLIENTE");
            usuarioRepo.save(cliente);
        }

        if (categoriaRepo.count() == 0) {
            Categoria cat1 = new Categoria();
            cat1.setNombre("Hardware");
            cat1.setDescripcion("Componentes y periféricos de computador");
            categoriaRepo.save(cat1);

            Categoria cat2 = new Categoria();
            cat2.setNombre("Accesorios");
            cat2.setDescripcion("Accesorios tecnológicos");
            categoriaRepo.save(cat2);

            Producto p1 = new Producto();
            p1.setNombre("Teclado Mecánico RGB");
            p1.setDescripcion("Switches Blue, retroiluminado");
            p1.setPrecio(45990.0);
            p1.setStock(15);
            p1.setCategoria(cat1);
            productoRepo.save(p1);

            Producto p2 = new Producto();
            p2.setNombre("Mouse Gamer Inalámbrico");
            p2.setDescripcion("Sensor óptico 16000 DPI");
            p2.setPrecio(29990.0);
            p2.setStock(20);
            p2.setCategoria(cat2);
            productoRepo.save(p2);
        }
    }
}