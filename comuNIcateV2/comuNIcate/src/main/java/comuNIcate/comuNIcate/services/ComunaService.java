package comuNIcate.comuNIcate.services;

import comuNIcate.comuNIcate.model.Comuna;
import comuNIcate.comuNIcate.model.Usuario;
import comuNIcate.comuNIcate.repository.ComunaRepository;
import comuNIcate.comuNIcate.repository.PublicacionRepository;
import comuNIcate.comuNIcate.repository.TarjetaRepository;
import comuNIcate.comuNIcate.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ComunaService {
    @Autowired
    private ComunaRepository comRepo;
    @Autowired
    private UsuarioRepository usuRepo;
    @Autowired
    private PublicacionRepository pubRepo;
    @Autowired
    private TarjetaRepository tarRepo;

    //Retorna todas las comunas
    public List<Comuna> findAllComunas(){
        return comRepo.findAll();
    }
    //Retorna una comuna por ID
    public Comuna findComunaById(Integer id){
        return comRepo.findById(id)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna no encontrada"));
    }
    //Retorna una comuna por nombre
    public Comuna findComunaByNombre(String nombreComuna){
        return comRepo.findByNombreComuna(nombreComuna)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Comuna no encontrada"));
    }
    //Guarda una comuna
    public Comuna saveComuna(Comuna newComuna){
        if(newComuna.getNombreComuna()==null|| newComuna.getNombreComuna().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La comuna debe tener un nombre");
        }
        return comRepo.save(newComuna);
    }
    //Actualizar completamente una comuna
    public Comuna updateComuna(Integer id, Comuna newComuna){
        Comuna comuna = comRepo.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Comuna no encontrada"));
        if(newComuna.getNombreComuna()==null|| newComuna.getNombreComuna().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La comuna debe tener un nombre");
        }
        if(newComuna.getFkRegion()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La comuna debe tener una region");
        }
        comuna.setNombreComuna(newComuna.getNombreComuna());
        comuna.setFkRegion(newComuna.getFkRegion());
        return comRepo.save(comuna);
    }
    //Actualizar parcialmente una comuna
    public Comuna partialUpdateComuna(Integer id, Comuna newComuna){
        Comuna comuna = comRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Comuna no encontrada"));
        if (newComuna.getNombreComuna()!=null && !newComuna.getNombreComuna().isBlank()){
            comuna.setNombreComuna(newComuna.getNombreComuna());
        }
        if(newComuna.getFkRegion()!=null){
            comuna.setFkRegion(newComuna.getFkRegion());
        }
        return comRepo.save(comuna);
    }
    //Borra una comuna
    public void deleteComunaById(Integer id){
        Comuna comuna = comRepo.findById(id)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Comuna no encontradada"));
        List<Usuario> usuarios = usuRepo.findByFkComuna(comuna);
        for(Usuario usu : usuarios){
            tarRepo.deleteByFkUsuario(usu);
            pubRepo.deleteByFkUsuario(usu);
            usuRepo.delete(usu);
        }
        comRepo.deleteById(id);
    }
}