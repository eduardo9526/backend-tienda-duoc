package cl.duoc.backend_tienda.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "detalle_boleta")
public class DetalleBoleta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;
    private Integer cantidad;
    private Double precioUnitario;
    @JsonIgnore @ManyToOne @JoinColumn(name = "id_boleta", nullable = false)
    private Boleta boleta;
    @ManyToOne @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    public Long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
    public Boleta getBoleta() { return boleta; }
    public void setBoleta(Boleta boleta) { this.boleta = boleta; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
