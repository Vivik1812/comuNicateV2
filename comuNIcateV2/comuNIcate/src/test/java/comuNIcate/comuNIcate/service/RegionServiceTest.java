package comuNIcate.comuNIcate.service;

import comuNIcate.comuNIcate.model.Region;
import comuNIcate.comuNIcate.repository.RegionRepository;
import comuNIcate.comuNIcate.services.RegionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RegionServiceTest {

        @Autowired
        private RegionService regServ;
        @MockBean
        private RegionRepository regRepo;

        private Region createRegion(){
            return new Region(1,"Valparaiso");
        }
        //Testeo del metodo findAll
        @Test
        public void testFindAll(){
                when(regRepo.findAll()).thenReturn(List.of(createRegion()));
                List<Region> regiones = regServ.findAllRegiones();
                assertNotNull(regiones);
                assertEquals(1,regiones.size());
        }
        //Testeo del metodo findRegionById
        @Test
        public void testFindById(){
                Integer id = 1;
                when(regRepo.findById(id)).thenReturn(Optional.of(createRegion()));
                Region region = regServ.findRegionById(id);
                assertNotNull(region);
                assertEquals("Valparaiso",region.getNombreRegion());
        }
        //Testeo del metodo findRegionByNombre
        @Test
        public void testFindByNombre(){
                String nombre = "Valparaiso";
                when(regRepo.findByNombreRegion(nombre)).thenReturn(Optional.of(createRegion()));
                Region region = regServ.findRegionByNombre(nombre);
                assertNotNull(region);
                assertEquals(1,region.getIdRegion() );
        }
        //Testeo del metodo saveRegion
        @Test
        public void testSaveRegion(){
                Region region = createRegion();
                when(regRepo.save(region)).thenReturn(region);
                Region regionSave = regServ.saveRegion(region);
                assertNotNull(regionSave);
                assertEquals(1,regionSave.getIdRegion());
        }
        //Testeo del metodo updateNombreRegion
        @Test
        public void testUpdateRegion(){
                Region region = createRegion();

                Region patchRegion = new Region();
                patchRegion.setNombreRegion("Antofagasta");

                when(regRepo.findById(1)).thenReturn(Optional.of(region));
                when(regRepo.save(any(Region.class))).thenReturn(region);

                Region updatedRegion = regServ.updateNombreRegion(1,patchRegion);

                assertNotNull(updatedRegion);
                assertEquals("Antofagasta",updatedRegion.getNombreRegion());
        }

        //Testeo del metodo deleteRegion
        @Test
        public void testDeleteRegion(){
                Integer id = 1;
                doNothing().when(regRepo).deleteById(id);
                when(regRepo.findById(id)).thenReturn(Optional.of(createRegion()));
                regServ.deleteRegion(id);
                verify(regRepo,times(1)).deleteById(id);
        }

}
