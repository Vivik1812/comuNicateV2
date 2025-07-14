package comuNIcate.comuNIcate.services;

import comuNIcate.comuNIcate.model.Tarjeta;
import comuNIcate.comuNIcate.repository.RegionRepository;
import comuNIcate.comuNIcate.repository.TarjetaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TarjetaService {
    @Autowired
    private TarjetaRepository tarRepo;

    @Autowired
    private RegionRepository regRepo;
    //Retornar todas las tarjetas
    public List<Tarjeta> findAllTarjetas(){
        return tarRepo.findAll();
    }
    //Retornar una tarjeta por su PAN
    public Tarjeta findTarjetaById(String pan){
        return tarRepo.findById(pan)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Tarjeta no encontrada"));
    }
    //Retorna tarjeta por ID de usuario
    public List<Tarjeta> findTarjetasByUsuario(long idUsuario){
        List<Tarjeta> tarjetas = tarRepo.findByFkUsuario_idUsuario(idUsuario);
        if (tarjetas.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"El usuario no tiene tarjetas registradas");
        }
        return tarjetas;
    }
    //Retorna cantidad de tarjeta por el nombre de una region
    public Long cantidadTarjetaByRegion(String nombreRegion){
        if (!regRepo.existsByNombreRegion(nombreRegion)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna no encontrada");
        }
        return tarRepo.cantidadTarjetasByRegion(nombreRegion);
    }
    //Retorna tarjeta que venceran entre 2 fechas
    public List<Tarjeta> findTarjetaAVencer(Date fechaInicial, Date fechaFinal){
        return tarRepo.findByFechaVencimientoTarjetaBetween(fechaInicial,fechaFinal);
    }
    //Guarda una tarjeta
    public Tarjeta saveTarjeta(Tarjeta newTarjeta){
        if (newTarjeta.getNombreTarjeta()==null || newTarjeta.getNombreTarjeta().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ningun campo debe estar vacio");
        }if (newTarjeta.getNombreTitular()==null || newTarjeta.getNombreTitular().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ningun campo debe estar vacio");
        }if (newTarjeta.getFechaVencimientoTarjeta()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ningun campo debe estar vacio");
        }if (newTarjeta.getCodigoVerificadorTarjeta()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ningun campo debe estar vacio");
        }if (newTarjeta.getFkUsuario()==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ningun campo debe estar vacio");
        }
        return tarRepo.save(newTarjeta);
    }
    //Actualiza una tarjeta
    public Tarjeta updateTarjeta(String pan,Tarjeta newTarjeta){
        Tarjeta tarjeta = tarRepo.findById(pan)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Tarjeta no encontrada"));
        if (newTarjeta.getNombreTarjeta()==null || newTarjeta.getNombreTarjeta().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ningun campo debe estar vacio");
        }if (newTarjeta.getNombreTitular()==null || newTarjeta.getNombreTitular().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ningun campo debe estar vacio");
        }if (newTarjeta.getFechaVencimientoTarjeta()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ningun campo debe estar vacio");
        }if (newTarjeta.getCodigoVerificadorTarjeta()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ningun campo debe estar vacio");
        }if (newTarjeta.getFkUsuario()==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ningun campo debe estar vacio");
        }
        tarjeta.setNombreTarjeta(newTarjeta.getNombreTarjeta());
        tarjeta.setNombreTitular(newTarjeta.getNombreTitular());
        tarjeta.setFechaVencimientoTarjeta(newTarjeta.getFechaVencimientoTarjeta());
        tarjeta.setCodigoVerificadorTarjeta(newTarjeta.getCodigoVerificadorTarjeta());
        tarjeta.setFkUsuario(newTarjeta.getFkUsuario());
        return tarRepo.save(tarjeta);
    }
    //Actualiza parcialmente una tarjeta
    public Tarjeta partialUpdateTarjeta(String pan,Tarjeta newTarjeta){
        Tarjeta tarjeta = tarRepo.findById(pan)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Tarjeta no encontrada"));
        if (newTarjeta.getNombreTarjeta()!=null && !newTarjeta.getNombreTarjeta().isBlank()){
            tarjeta.setNombreTarjeta(newTarjeta.getNombreTarjeta());
        }if (newTarjeta.getNombreTitular()!=null && !newTarjeta.getNombreTitular().isBlank()){
            tarjeta.setNombreTitular(newTarjeta.getNombreTitular());
        }if (newTarjeta.getFechaVencimientoTarjeta()!=null){
            tarjeta.setFechaVencimientoTarjeta(newTarjeta.getFechaVencimientoTarjeta());
        }if (newTarjeta.getCodigoVerificadorTarjeta()!=null){
            tarjeta.setCodigoVerificadorTarjeta(newTarjeta.getCodigoVerificadorTarjeta());
        }if (newTarjeta.getFkUsuario()!=null) {
            tarjeta.setFkUsuario(newTarjeta.getFkUsuario());
        }
        return tarRepo.save(tarjeta);
    }
    //Borra una tarjeta
    public void deleteTarjetaById(String pan){
        tarRepo.findById(pan)
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Tarjeta no encontrada"));
        tarRepo.deleteById(pan);
    }
}