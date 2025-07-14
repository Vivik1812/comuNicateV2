package comuNIcate.comuNIcate.service;

import comuNIcate.comuNIcate.model.Tarjeta;
import comuNIcate.comuNIcate.model.Usuario;
import comuNIcate.comuNIcate.repository.RegionRepository;
import comuNIcate.comuNIcate.repository.TarjetaRepository;
import comuNIcate.comuNIcate.services.TarjetaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TarjetaServiceTest {
    @Autowired
    private TarjetaService tarServ;

    @MockBean
    private TarjetaRepository tarRepo;

    @MockBean
    private RegionRepository regRepo;

    private Tarjeta createTarjeta(){
        return new Tarjeta("1234567890123456",
            "VISA",
            "Juanito Perez",
            new Date(),
            123,
            new Usuario());
    }
    //Testeo del metodo findAllTarjetas
    @Test
    public void testFindAllTarjeta(){
        when(tarRepo.findAll()).thenReturn(List.of(createTarjeta()));
        List<Tarjeta> tarjetas = tarServ.findAllTarjetas();
        assertNotNull(tarjetas);
        assertEquals(1, tarjetas.size());
    }
    //Testeo del metodo findTarjetaByPan
    @Test
    public void testFindTarjetByPan(){
        String pan = "1234567890123456";
        when(tarRepo.findById(pan)).thenReturn(Optional.of(createTarjeta()));
        Tarjeta tarjeta = tarServ.findTarjetaById(pan);
        assertNotNull(tarjeta);
        assertEquals("VISA",tarjeta.getNombreTarjeta());
    }
    //Testeo del metodo findTarjetasByUsuario
    @Test
    public void testFindTarjetasByUsuario(){
        long idUsuario = 1L;
        when(tarRepo.findByFkUsuario_idUsuario(idUsuario)).thenReturn(List.of(createTarjeta()));
        List<Tarjeta> tarjetas = tarServ.findTarjetasByUsuario(idUsuario);
        assertNotNull(tarjetas);
        assertEquals(1,tarjetas.size());
    }
    //Testeo del metodo saveTarjeta
    @Test
    public void testSaveTarjeta(){
        Tarjeta tarjeta = createTarjeta();
        when(tarRepo.save(tarjeta)).thenReturn(tarjeta);
        Tarjeta savedTarjeta = tarServ.saveTarjeta(tarjeta);
        assertNotNull(savedTarjeta);
        assertEquals("1234567890123456",savedTarjeta.getNumeroPan());
    }
    //Testeo del metodo updateTarjeta
    @Test
    public void testUpdateTarjeta(){
        String id = "1234567890123456";
        Tarjeta tarjeta = createTarjeta();
        Tarjeta patchTarjeta = new Tarjeta();
        patchTarjeta.setNombreTarjeta("MasterCard");
        patchTarjeta.setNombreTitular("Juanito Jimenez");
        patchTarjeta.setFechaVencimientoTarjeta(new Date());
        patchTarjeta.setCodigoVerificadorTarjeta(234);
        patchTarjeta.setFkUsuario(new Usuario());

        when(tarRepo.findById(id)).thenReturn(Optional.of(tarjeta));
        when(tarRepo.save(any(Tarjeta.class))).thenReturn(tarjeta);

        Tarjeta updatedTarjeta = tarServ.updateTarjeta(id,patchTarjeta);
        assertNotNull(updatedTarjeta);
        assertEquals(234,updatedTarjeta.getCodigoVerificadorTarjeta());
    }
    //Testeo del metodo partialUpdateTarjeta
    @Test
    public void testPartialUpdateTarjeta(){
        String pan = "1234567890123456";
        Tarjeta tarjeta = createTarjeta();
        Tarjeta patchTarjeta = createTarjeta();
        patchTarjeta.setCodigoVerificadorTarjeta(234);

        when(tarRepo.findById(pan)).thenReturn(Optional.of(tarjeta));
        when(tarRepo.save(any(Tarjeta.class))).thenReturn(tarjeta);

        Tarjeta updatedTarjeta = tarServ.partialUpdateTarjeta(pan, patchTarjeta);
        assertNotNull(updatedTarjeta);
        assertEquals(234,updatedTarjeta.getCodigoVerificadorTarjeta());
    }
    //Testeo del metodo deleteTarjeta
    @Test
    public void testDeleteTarjeta(){
        String pan = "1234567890123456";
        doNothing().when(tarRepo).deleteById(pan);
        when(tarRepo.findById(pan)).thenReturn(Optional.of(createTarjeta()));
        tarServ.deleteTarjetaById(pan);
        verify(tarRepo,times(1)).deleteById(pan);
    }
    //Testeo del metodo cantidadTarjetaByRegion
    @Test
    public void testCantidadTarjetaByRegion(){
        String nombreRegion = "Calera de Tango";
        Long cantidadTar = 5L;

        when(regRepo.existsByNombreRegion(nombreRegion)).thenReturn(true);
        when(tarRepo.cantidadTarjetasByRegion(nombreRegion)).thenReturn(cantidadTar);

        Long cantidad = tarServ.cantidadTarjetaByRegion(nombreRegion);
        assertNotNull(cantidad);
        assertEquals(cantidadTar, cantidad);
        verify(regRepo, times(1)).existsByNombreRegion(nombreRegion);
        verify(tarRepo, times(1)).cantidadTarjetasByRegion(nombreRegion);
    }
    //Testeo del metodo findTarjetasAVencer
    @Test
    public void testFindTarjetasAVencer(){
        Date fechaInicial = new Date(System.currentTimeMillis() - 100000);
        Date fechaFinal = new Date(System.currentTimeMillis() + 100000);

        Tarjeta tarjeta = createTarjeta();
        tarjeta.setFechaVencimientoTarjeta(new Date());
        when(tarRepo.findByFechaVencimientoTarjetaBetween(fechaInicial, fechaFinal)).thenReturn(List.of(tarjeta));

        List<Tarjeta> resultado = tarServ.findTarjetaAVencer(fechaInicial, fechaFinal);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(tarRepo, times(1)).findByFechaVencimientoTarjetaBetween(fechaInicial, fechaFinal);
    }
}

