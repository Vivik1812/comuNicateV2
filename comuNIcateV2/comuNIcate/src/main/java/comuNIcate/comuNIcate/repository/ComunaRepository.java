package comuNIcate.comuNIcate.repository;

import comuNIcate.comuNIcate.model.Comuna;
import comuNIcate.comuNIcate.model.Region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;


@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Integer> {
    Optional<Comuna> findByNombreComuna(String nombreComuna);
    boolean existsByNombreComuna(String nombreComuna);

    //METODOS CASCADE
    List<Comuna> findByFkRegion(Region fkRegion);
    void deleteByFkRegion(Region region);

}
