package comuNIcate.comuNIcate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import comuNIcate.comuNIcate.model.Publicacion;
import comuNIcate.comuNIcate.model.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.Date;


@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
    Optional<List<Publicacion>> findByfkUsuario_idUsuario(Long idUsuario);
    @Query(
        value =
        """
        SELECT
            pub.descripcion_publicacion AS descripcion,
            usu.nombre_usuario AS nombreUsuario,
            CONCAT(pub.ubicacion_latitud_publicacion, ', ', pub.ubicacion_longitud_publicacion) AS ubicacion,
            com.nombre_comuna AS nombreComuna,
            reg.nombre_region AS nombreRegion
        FROM publicacion pub
        JOIN usuario usu ON pub.id_usuario = usu.id_usuario
        JOIN comuna com ON usu.id_comuna = com.id_comuna
        JOIN region reg ON reg.id_region = com.id_region
        """,
        nativeQuery = true
    )
    List<Object[]> findDetallesPublicacion();

    //QUERYV2 2
    @Query(
        value = 
        """
        SELECT COUNT(p.id_publicacion) as publicacion_cantidad
        FROM publicacion p
        JOIN usuario u using(id_usuario)
        JOIN comuna c on u.id_comuna = c.id_comuna
        JOIN region r on c.id_region = r.id_region
        WHERE r.nombre_region = :nombreRegion
        """,
        nativeQuery = true
    )
    Long cantidadPublicacionByRegion(@Param("nombreRegion") String nombreRegion);

    //QUERYV2 4
    @Query(
        value = 
        """
        SELECT COUNT(p.id_publicacion) as publicacion_cantidad
        FROM publicacion p
        JOIN usuario u using(id_usuario)
        JOIN comuna c on u.id_comuna = c.id_comuna
        WHERE c.nombre_comuna = :nombreComuna
        """,
        nativeQuery = true
    )
    Long cantidadPublicacionByComuna(@Param("nombreComuna") String nombreComuna);

    //JPA 1
    List<Publicacion> findByFechaHoraPublicacionBetween(Date fechaInicial, Date fechaFinal);

    //JPA 3
    List<Publicacion> findByfkUsuario_nombreUsuarioAndFechaHoraPublicacion(String nombreUsuario, Date fecha);

    //Metodo CASCADE 
    void deleteByFkUsuario(Usuario usuario);
}
