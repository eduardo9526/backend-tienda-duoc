package cl.duoc.backend_tienda.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "boleta")
public class Boleta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBoleta;
    private LocalDateTime fecha = LocalDateTime.now();
    @Column(nullable = false) private Double total;
    @ManyToOne @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL)
    private List<DetalleBoleta> detalles;

    public Long getIdBoleta() { return idBoleta; }
    public void setIdBoleta(Long idBoleta) { this.idBoleta = idBoleta; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<DetalleBoleta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleBoleta> detalles) { this.detalles = detalles; }
}
