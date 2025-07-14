package comuNIcate.comuNIcate.repository;

import comuNIcate.comuNIcate.model.Tarjeta;
import comuNIcate.comuNIcate.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, String> {
    List<Tarjeta> findByFkUsuario_idUsuario(Long IdUsuario);

    //QUERYV2 3
    @Query(
        value = 
        """
        SELECT COUNT(t.numero_pan) as cantidad_tarjetas
        FROM tarjeta t
        JOIN usuario u USING(id_usuario)
        JOIN comuna c on u.id_comuna = c.id_comuna
        JOIN region r on c.id_region = r.id_region
        WHERE r.nombre_region = :nombreRegion
        """,
        nativeQuery = true
    )
    Long cantidadTarjetasByRegion(@Param("nombreRegion") String nombreRegion);

    //JPA 2
    List<Tarjeta> findByFechaVencimientoTarjetaBetween(Date fechaInicial, Date fechaFinal);

    //Metodo CASCADE 
    void deleteByFkUsuario(Usuario usuario);
}
