package comuNIcate.comuNIcate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="tarjeta")
public class Tarjeta {

    @Id
    @Column(length = 20, unique = true,nullable = false)
    private String numeroPan;
    @Column(length = 20, nullable = false)
    private String nombreTarjeta;
    @Column(length = 30, nullable = false)
    private String nombreTitular;
    @Column(nullable = false)
    private Date fechaVencimientoTarjeta;
    @Column(nullable = false, length = 3)
    private Integer codigoVerificadorTarjeta;
    @ManyToOne
    @JoinColumn(name = "idUsuario",nullable = false)
    private Usuario fkUsuario;
}
