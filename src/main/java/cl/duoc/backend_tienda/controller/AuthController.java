package cl.duoc.backend_tienda.controller;

import cl.duoc.backend_tienda.model.Usuario;
import cl.duoc.backend_tienda.repository.UsuarioRepository;
import cl.duoc.backend_tienda.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String password = credenciales.get("password");

        return usuarioRepo.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .map(u -> {
                    String token = jwtUtil.generarToken(u.getEmail(), u.getRol());
                    return ResponseEntity.ok(Map.of(
                            "token", token,
                            "idUsuario", u.getIdUsuario(),
                            "nombre", u.getNombre(),
                            "email", u.getEmail(),
                            "rol", u.getRol()
                    ));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas")));
    }
}
