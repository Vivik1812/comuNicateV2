package comuNIcate.comuNIcate.services;

import comuNIcate.comuNIcate.model.Publicacion;
import comuNIcate.comuNIcate.repository.ComunaRepository;
import comuNIcate.comuNIcate.repository.PublicacionRepository;
import comuNIcate.comuNIcate.repository.RegionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PublicacionService {
    @Autowired
    private PublicacionRepository pubRepo;

    @Autowired
    private ComunaRepository comRepo;

    @Autowired
    private RegionRepository regRepo;

    //Obtener todas las publicaciones
    public List<Publicacion> findAllPublicaciones(){
        return pubRepo.findAll();
    }
    //Obtener una publicacion por ID
    public Publicacion findPublicacionById(Long id){
        return pubRepo.findById(id)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Publicacion no encontrada"));
    }
    //Obtener publicaciones por ID de Usuario
    public List<Publicacion> findPublicacionesByIdUsuario(Long id){
        return pubRepo.findByfkUsuario_idUsuario(id)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"El usuario no tiene publicaciones"));
    }
    //Obtener resumen de publicacion
    public List<Map<String,Object>> findDetallesPublicaciones(){
        try {
            List<Object[]> resultados = pubRepo.findDetallesPublicacion();
            if (resultados.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay publicaciones para mostrar");
            }

            List<Map<String, Object>> lista = new ArrayList<>();
            for (Object[] fila : resultados) {
                Map<String, Object> datos = new HashMap<>();
                datos.put("descripcion", fila[0]);
                datos.put("nombreUsuario", fila[1]);
                datos.put("ubicacion", fila[2]);
                datos.put("nombreComuna", fila[3]);
                datos.put("nombreRegion", fila[4]);
                lista.add(datos);
            }
            return lista;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener los detalles de las publicaciones", e);
        }
    }
    //Obtener publicaciones entre 2 fechas
    public List<Publicacion> getPublicacionesByFechas(Date fechaInicial, Date fechaFinal){
        return pubRepo.findByFechaHoraPublicacionBetween(fechaInicial, fechaFinal);
    }
    //Obtener cantidad de publicaciones en una comuna
    public Long cantidadPublicacionesByComuna(String nombreComuna){
        if (!comRepo.existsByNombreComuna(nombreComuna)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna no encontrada");
        }
        Long cantidadPublicaciones = pubRepo.cantidadPublicacionByComuna(nombreComuna);
        return cantidadPublicaciones;
    }
    //Obtener cantidad de publicaciones en una region
    public Long cantidadPublicacionesByRegion(String nombreRegion){
        if (!regRepo.existsByNombreRegion(nombreRegion)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Region no encontrada");
        }
        Long cantidadPublicaciones = pubRepo.cantidadPublicacionByRegion(nombreRegion);
        return cantidadPublicaciones;
    }
    //Obtener publicaciones de un usuario en una fecha dada
    public List<Publicacion> getPublicacionesByUsuarioAndFecha(String nombreUsuario, Date fecha){
        return pubRepo.findByfkUsuario_nombreUsuarioAndFechaHoraPublicacion(nombreUsuario, fecha);
    }
    //Guardar una publicacion
    public Publicacion savePublicacion(Publicacion newPublicacion){
        if (newPublicacion.getDescripcionPublicacion()==null || newPublicacion.getDescripcionPublicacion().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La publicacion debe tener una descripcion");
        }if (newPublicacion.getFechaHoraPublicacion()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La publicacion debe tener fecha y hora");
        }if (   newPublicacion.getUbicacionLongitudPublicacion()==null||
                newPublicacion.getUbicacionLongitudPublicacion()<-180||
                newPublicacion.getUbicacionLongitudPublicacion()>180){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La longitud debe estar entre los -90 y 90 grados");
        }if (   newPublicacion.getUbicacionLatitudPublicacion()==null||
                newPublicacion.getUbicacionLatitudPublicacion()<-90||
                newPublicacion.getUbicacionLatitudPublicacion()>90){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La latitud debe estar entre los -180 y 180 grados");
        }if (newPublicacion.getFkUsuario()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"La publicacion debe tener un usuario asociado");
        }
        return pubRepo.save(newPublicacion);
    }
    //Actualizar una publicacion
    public Publicacion updatePublicacion(Long id,Publicacion newPublicacion){
        Publicacion publicacion = pubRepo.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Publicacion no encontrada"));
        if (newPublicacion.getDescripcionPublicacion()==null || newPublicacion.getDescripcionPublicacion().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ningun campo debe estar vacio");
        }if (newPublicacion.getFechaHoraPublicacion()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ningun campo debe estar vacio");
        }if (   newPublicacion.getUbicacionLongitudPublicacion()==null||
                newPublicacion.getUbicacionLongitudPublicacion()<-180||
                newPublicacion.getUbicacionLongitudPublicacion()>180){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ningun campo debe estar vacio");
        }if (   newPublicacion.getUbicacionLatitudPublicacion()==null||
                newPublicacion.getUbicacionLatitudPublicacion()<-90||
                newPublicacion.getUbicacionLatitudPublicacion()>90){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ningun campo debe estar vacio");
        }if (newPublicacion.getFkUsuario()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ningun campo debe estar vacio");
        }
        publicacion.setDescripcionPublicacion(newPublicacion.getDescripcionPublicacion());
        publicacion.setFechaHoraPublicacion(newPublicacion.getFechaHoraPublicacion());
        publicacion.setUbicacionLongitudPublicacion(newPublicacion.getUbicacionLongitudPublicacion());
        publicacion.setUbicacionLatitudPublicacion(newPublicacion.getUbicacionLatitudPublicacion());
        publicacion.setFkUsuario(newPublicacion.getFkUsuario());
        return pubRepo.save(publicacion);
    }
    //Actualizar parcialmente una publicacion
    public Publicacion partialUpdatePublicacion(Long id,Publicacion newPublicacion){
        Publicacion publicacion = pubRepo.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Publicacion no encontrada"));
        if (newPublicacion.getDescripcionPublicacion()!=null && !newPublicacion.getDescripcionPublicacion().isBlank()){
            publicacion.setDescripcionPublicacion(newPublicacion.getDescripcionPublicacion());
        }if (newPublicacion.getFechaHoraPublicacion()!=null){
            publicacion.setFechaHoraPublicacion(newPublicacion.getFechaHoraPublicacion());
        }if (   newPublicacion.getUbicacionLongitudPublicacion()!=null&&
                newPublicacion.getUbicacionLongitudPublicacion()>-180&&
                newPublicacion.getUbicacionLongitudPublicacion()<180){
            publicacion.setUbicacionLongitudPublicacion(newPublicacion.getUbicacionLongitudPublicacion());
        }if (   newPublicacion.getUbicacionLatitudPublicacion()!=null&&
                newPublicacion.getUbicacionLatitudPublicacion()>-90&&
                newPublicacion.getUbicacionLatitudPublicacion()<90){
            publicacion.setUbicacionLatitudPublicacion(newPublicacion.getUbicacionLatitudPublicacion());
        }if (newPublicacion.getFkUsuario()!=null){
            publicacion.setFkUsuario(newPublicacion.getFkUsuario());
        }
        return pubRepo.save(publicacion);
    }
    //Eliminar una publicacion
    public void deletePublicacionById(Long id){
        pubRepo.findById(id)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Publicacion no encontrada"));
        pubRepo.deleteById(id);
    }
}
