package comuNIcate.comuNIcate.service;

import comuNIcate.comuNIcate.model.Comuna;
import comuNIcate.comuNIcate.model.Region;
import comuNIcate.comuNIcate.repository.ComunaRepository;
import comuNIcate.comuNIcate.services.ComunaService;
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
public class ComunaServiceTest {
    @Autowired
    private ComunaService comServ;
    @MockBean
    private ComunaRepository comRepo;

    private Comuna createComuna(){
        return new Comuna(1,"Calera de Tango",new Region());
    }

    //Testeo del metodo findAllComunas
    @Test
    public void testFindAllComunas(){
        when(comRepo.findAll()).thenReturn(List.of(createComuna()));
        List<Comuna> comunas = comServ.findAllComunas();
        assertNotNull(comunas);
        assertEquals(1,comunas.size());
    }

    //Testeo del metodo findComunaById
    @Test
    public void testFindComunaById(){
        Integer id = 1;
        when(comRepo.findById(id)).thenReturn(Optional.of(createComuna()));
        Comuna comuna = comServ.findComunaById(id);
        assertNotNull(comuna);
        assertEquals("Calera de Tango",comuna.getNombreComuna());
    }

    //Testeo del metodo findComunaByNombre
    @Test
    public void testFindComunaByNombre(){
        String nombre = "Calera de Tango";
        when(comRepo.findByNombreComuna(nombre)).thenReturn(Optional.of(createComuna()));
        Comuna comuna = comServ.findComunaByNombre(nombre);
        assertNotNull(comuna);
        assertEquals(1,comuna.getIdComuna());
    }

    //Teste del metodo saveComuna
    @Test
    public void testSaveComuna(){
        Comuna comuna = createComuna();
        when(comRepo.save(comuna)).thenReturn(comuna);
        Comuna comunaSave = comServ.saveComuna(comuna);
        assertNotNull(comunaSave);
        assertEquals(1,comunaSave.getIdComuna());
    }

    //Testeo del metodo updateComuna
    @Test
    public void testUpdateComuna(){
        Integer id = 1;
        Comuna comuna = createComuna();

        Comuna patchComuna = new Comuna();
        patchComuna.setNombreComuna("Cerrillos");
        patchComuna.setFkRegion(new Region());

        when(comRepo.findById(id)).thenReturn(Optional.of(comuna));
        when(comRepo.save(any(Comuna.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Comuna updatedComuna = comServ.updateComuna(id, patchComuna);

        assertNotNull(updatedComuna);
        assertEquals("Cerrillos",updatedComuna.getNombreComuna());
    }

    //Testeo del metodo partialUpdateComuna
    @Test
    public void testPartialUpdateComuna(){
        Integer id = 1;
        Comuna comuna = createComuna();

        Comuna patchComuna = new Comuna();
        patchComuna.setNombreComuna("Cerrillos");

        when(comRepo.findById(id)).thenReturn(Optional.of(comuna));
        when(comRepo.save(any(Comuna.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Comuna updatedComuna = comServ.partialUpdateComuna(id, patchComuna);

        assertNotNull(updatedComuna);
        assertEquals("Cerrillos",updatedComuna.getNombreComuna());
    }

    //Testeo del metodo deleteComuna
    @Test
    public void testDeleteComuna(){
        Integer id = 1;
        doNothing().when(comRepo).deleteById(id);
        when(comRepo.findById(id)).thenReturn(Optional.of(createComuna()));
        comServ.deleteComunaById(id);
        verify(comRepo,times(1)).deleteById(id);
    }
}
