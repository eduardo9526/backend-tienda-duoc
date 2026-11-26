package cl.duoc.backend_tienda.repository;
import cl.duoc.backend_tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductoRepository extends JpaRepository<Producto, Long> {}
