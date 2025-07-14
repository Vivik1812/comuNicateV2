package comuNIcate.comuNIcate.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "publicacion")

public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPublicacion;
    @Column(nullable = false)
    private String descripcionPublicacion;
    @Column(nullable = false)
    private Date fechaHoraPublicacion;
    @Column(nullable = false)
    private Double ubicacionLongitudPublicacion;
    @Column(nullable = false)
    private Double ubicacionLatitudPublicacion;
    @ManyToOne
    @JoinColumn(name = "idUsuario",nullable = false)
    private Usuario fkUsuario;
}
