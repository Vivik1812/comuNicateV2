package comuNIcate.comuNIcate.services;

import comuNIcate.comuNIcate.model.Usuario;
import comuNIcate.comuNIcate.repository.PublicacionRepository;
import comuNIcate.comuNIcate.repository.RegionRepository;
import comuNIcate.comuNIcate.repository.TarjetaRepository;
import comuNIcate.comuNIcate.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuRepo;
    @Autowired
    private RegionRepository regRepo;
    @Autowired
    private TarjetaRepository tarRepo;
    @Autowired
    private PublicacionRepository pubRepo;

    //Retorna todos los usuarios
    public List<Usuario> findAllUsuarios(){
        return usuRepo.findAll();
    }
    //Retorna un usuario por ID
    public Usuario findUsuarioById(Long id){
        return usuRepo.findById(id)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado"));
    }
    //Retorna resumen de Usuario
    public List<Map<String, Object>> findDetallesUsuarios(){
        try {
            List<Object[]> resultados = usuRepo.findDetallesUsuario();
            if (resultados.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay usuarios para mostrar");
            }
            List<Map<String, Object>> lista = new ArrayList<>();
            for (Object[] fila : resultados) {
                Map<String, Object> datos = new HashMap<>();
                datos.put("usuario", fila[0]);
                datos.put("edad", fila[1]);
                datos.put("comuna", fila[2]);
                datos.put("region", fila[3]);
                lista.add(datos);
            }
            return lista;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener los detalles de el usuario", e);
        }
    }
    //Retorna un usuario por Correo
    public Usuario findUsuarioByCorreo(String correoUsuario){
        return usuRepo.findByCorreoUsuario(correoUsuario)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado"));
    }
    //Retornar por nombre de usuario
    public Usuario findUsuarioByNombreUsuario(String nombreUsuario){
        return usuRepo.findByNombreUsuario(nombreUsuario)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado"));
    }
    //Retorna cantidad de usuario en una region
    public Long cantidadUsuarioByRegion(String nombreRegion){
        if (!regRepo.existsByNombreRegion(nombreRegion)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Region no encontrada");
        }
        Long cantidadUsuarios = usuRepo.cantidadUsuarioByRegion(nombreRegion);
        return cantidadUsuarios;
    }
    //Retorna los usuarios nacidos entre 2 fechas
    public List<Usuario> findUsuariosByFechaNacimiento(Date fechaInicial, Date fechaFinal){
        return usuRepo.findByFechaNacimientoUsuarioBetween(fechaInicial, fechaFinal);
    }
    //Guarda un usuario
    public Usuario saveUsuario(Usuario newUsuario){
        if (newUsuario.getNombreUsuario()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Debe tener un nombre de usuario");
        }if (newUsuario.getClave()==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Debe tener una clave");
        }if (newUsuario.getFechaNacimientoUsuario()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Debe tener una fecha de nacimiento");
        }if (newUsuario.getCorreoUsuario()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Debe tener un correo");
        }if (newUsuario.getFkComuna()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Debe tener una comuna");
        }
        return usuRepo.save(newUsuario);
    }
    //Actualiza un usuario
    public Usuario updateUsuario(long id,Usuario newUsuario){
        usuRepo.findById(id)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado"));
        if (newUsuario.getNombreUsuario()==null||newUsuario.getNombreUsuario().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Debe tener un nombre de usuario");
        }if (newUsuario.getClave()==null||newUsuario.getClave().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Debe tener una clave");
        }if (newUsuario.getFechaNacimientoUsuario()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Debe tener una fecha de nacimiento");
        }if (newUsuario.getCorreoUsuario()==null||newUsuario.getCorreoUsuario().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Debe tener un correo");
        }if (newUsuario.getFkComuna()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Debe tener una comuna");
        }
        return usuRepo.save(newUsuario);
    }
    //Actualiza parcialmente un usuario
    public Usuario partialUpdateUsuario(long id, Usuario newUsuario){
        Usuario usuario = usuRepo.findById(id)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado"));
        if (newUsuario.getNombreUsuario()!=null&&!newUsuario.getNombreUsuario().isBlank()){
            usuario.setNombreUsuario(newUsuario.getNombreUsuario());
        }if (newUsuario.getClave()==null) {
            usuario.setClave(newUsuario.getClave());
        }if (newUsuario.getFechaNacimientoUsuario()!=null){
            usuario.setFechaNacimientoUsuario(newUsuario.getFechaNacimientoUsuario());
        }if (newUsuario.getCorreoUsuario()!=null&&!newUsuario.getCorreoUsuario().isBlank()){
            usuario.setCorreoUsuario(newUsuario.getCorreoUsuario());
        }if (newUsuario.getFkComuna()!=null){
            usuario.setFkComuna(newUsuario.getFkComuna());
        }
        return usuRepo.save(usuario);
    }
    //Borra un usuario
    public void deleteUsuarioById(long id){
        Usuario usuario = usuRepo.findById(id)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado"));
        tarRepo.deleteByFkUsuario(usuario);
        pubRepo.deleteByFkUsuario(usuario);
        usuRepo.deleteById(id);
    }
}
