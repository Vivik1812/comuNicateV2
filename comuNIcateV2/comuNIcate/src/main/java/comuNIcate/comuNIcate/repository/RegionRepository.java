package comuNIcate.comuNIcate.repository;

import comuNIcate.comuNIcate.model.Region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {
    Optional<Region> findByNombreRegion(String nombreRegion);
    boolean existsByNombreRegion(String nombreRegion);
}
