package cl.duoc.backend_tienda.repository;
import cl.duoc.backend_tienda.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {}
