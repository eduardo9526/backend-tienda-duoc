package cl.duoc.backend_tienda.repository;
import cl.duoc.backend_tienda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario, Long> { Optional<Usuario> findByEmail(String email); }
