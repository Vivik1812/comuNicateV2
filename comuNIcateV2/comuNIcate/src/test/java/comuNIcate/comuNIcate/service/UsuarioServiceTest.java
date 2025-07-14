package comuNIcate.comuNIcate.service;

import comuNIcate.comuNIcate.model.Comuna;
import comuNIcate.comuNIcate.model.Usuario;
import comuNIcate.comuNIcate.repository.RegionRepository;
import comuNIcate.comuNIcate.repository.UsuarioRepository;
import comuNIcate.comuNIcate.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UsuarioServiceTest {
    @Autowired
    private UsuarioService usuServ;

    @MockBean
    private UsuarioRepository usuRepo;
    
    @MockBean
    private RegionRepository regRepo;

    private Usuario createUsuario(){
        return new Usuario(1L,"Juanito","1234567890",new Date(),"juanito@gmail.com",new Comuna());

    }
    //Testeo del metodo findAllUsuarios
    @Test
    public void testFindAllUsuarios(){
        when(usuRepo.findAll()).thenReturn(List.of(createUsuario()));
        List<Usuario> usuarios = usuServ.findAllUsuarios();
        assertNotNull(usuarios);
        assertEquals(1,usuarios.size());
    }
    //Testeo del metodo findUsuarioById
    @Test
    public void testFindUsuarioById(){
        Long id = 1L;
        when(usuRepo.findById(id)).thenReturn(Optional.of(createUsuario()));
        Usuario usuario = usuServ.findUsuarioById(id);
        assertNotNull(usuario);
        assertEquals("Juanito",usuario.getNombreUsuario());
    }
    //Testeo del metodo findDetallesUsuario
    @Test
    public void testFindDetallerUsuario(){
        Object[] row1 = {"Juanito",25,"Calera de Tango","Santiago"};
        Object[] row2 = {"Juanito",25,"Calera de Tango","Santiago"};
        List<Object[]> results = List.of(row1, row2);
        when(usuRepo.findDetallesUsuario()).thenReturn(results);

        List<Map<String, Object>> result = usuServ.findDetallesUsuarios();

        assertNotNull(result);
        assertEquals(2,result.size());
    }
    //Testeo del metodo findUsuarioByCorreo
    @Test
    public void findUsuarioByCorreo(){
        String correo = "juanito@gmail.com";
        when(usuRepo.findByCorreoUsuario(correo)).thenReturn(Optional.of(createUsuario()));
        Usuario usuario = usuServ.findUsuarioByCorreo(correo);
        assertNotNull(usuario);
        assertEquals(1, usuario.getIdUsuario());
    }
    //Testeo del metodo findUsuarioByNombreUsuario
    @Test
    public void testFindUsuarioByNombreUsuario(){
        String nombreUsuario = "Juanito";
        when(usuRepo.findByNombreUsuario(nombreUsuario)).thenReturn(Optional.of(createUsuario()));
        Usuario usuario = usuServ.findUsuarioByNombreUsuario(nombreUsuario);
        assertNotNull(usuario);
        assertEquals(1, usuario.getIdUsuario());
    }
    //Testeo del metodo saveUsuario
    @Test
    public void saveUsuario(){
        Usuario usuario = createUsuario();
        when(usuRepo.save(usuario)).thenReturn(usuario);
        Usuario savedUsuario = usuServ.saveUsuario(usuario);
        assertNotNull(savedUsuario);
        assertEquals(1,savedUsuario.getIdUsuario());

    }
    //Testeo del metodo updateUsuario
    @Test
    public void testUpdateUsuario(){
        long id = 1L;
        Usuario usuario = createUsuario();
        Usuario patchUsuario = new Usuario();
        patchUsuario.setNombreUsuario("JuanitoMaster");
        patchUsuario.setFechaNacimientoUsuario(new Date());
        patchUsuario.setCorreoUsuario("juanito3000@gmail.com");
        patchUsuario.setClave("12345678");
        patchUsuario.setFkComuna(new Comuna());
        when(usuRepo.findById(id)).thenReturn(Optional.of(usuario));
        when(usuRepo.save(any(Usuario.class))).thenReturn(patchUsuario);

        Usuario updatedUsuario = usuServ.updateUsuario(id,patchUsuario);
        assertNotNull(updatedUsuario);
        assertEquals("JuanitoMaster",updatedUsuario.getNombreUsuario());
    }
    //Testeo del metodo partialUpdateUsuario
    @Test
    public void testPartialUpdateUsuario(){
        long id = 1L;
        Usuario usuario = createUsuario();
        Usuario patchUsuario = new Usuario();
        patchUsuario.setNombreUsuario("JuanitoMaster3000");
        patchUsuario.setFechaNacimientoUsuario(usuario.getFechaNacimientoUsuario());
        patchUsuario.setCorreoUsuario(usuario.getCorreoUsuario());
        patchUsuario.setFkComuna(usuario.getFkComuna());
        when(usuRepo.findById(id)).thenReturn(Optional.of(usuario));
        when(usuRepo.save(usuario)).thenReturn(usuario);

        Usuario updatedUsuario = usuServ.partialUpdateUsuario(id, patchUsuario);
        assertNotNull(updatedUsuario);
        assertEquals("JuanitoMaster3000",updatedUsuario.getNombreUsuario());
    }
    //Testeo del metodo deleteUsuario
    @Test
    public void testDeleteUsuario(){
        long id = 1L;
        doNothing().when(usuRepo).deleteById(id);
        when(usuRepo.findById(id)).thenReturn(Optional.of(createUsuario()));
        usuServ.deleteUsuarioById(id);
        verify(usuRepo,times(1)).deleteById(id);
    }
    //Testeo del metodo cantidadUsuarioByRegion
    @Test
    public void testCantidadUsuarioByRegion(){
        String nombreRegion = "San Bernardo";
        Long cantidadUsu = 5L;
        when(regRepo.existsByNombreRegion(nombreRegion)).thenReturn(true);
        when(usuRepo.cantidadUsuarioByRegion(nombreRegion)).thenReturn(cantidadUsu);

        Long resultado = usuServ.cantidadUsuarioByRegion(nombreRegion);
        assertNotNull(resultado);
        assertEquals(cantidadUsu, resultado);
        verify(regRepo, times(1)).existsByNombreRegion(nombreRegion);
        verify(usuRepo, times(1)).cantidadUsuarioByRegion(nombreRegion);
    }
    //Testeo del metodo findUsuariosByFechaNacimiento
    @Test
    public void testFindUsuariosByFechaNacimiento(){
        Date fechaInicial = new Date(System.currentTimeMillis() - 100000);
        Date fechaFinal = new Date(System.currentTimeMillis() + 100000);

        Usuario usuario = createUsuario();
        usuario.setFechaNacimientoUsuario(new Date());
        when(usuRepo.findByFechaNacimientoUsuarioBetween(fechaInicial, fechaFinal)).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuServ.findUsuariosByFechaNacimiento(fechaInicial, fechaFinal);
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuRepo, times(1)).findByFechaNacimientoUsuarioBetween(fechaInicial, fechaFinal);
    }
}
