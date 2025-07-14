package comuNIcate.comuNIcate;

import comuNIcate.comuNIcate.model.Comuna;
import comuNIcate.comuNIcate.model.Publicacion;
import comuNIcate.comuNIcate.model.Region;
import comuNIcate.comuNIcate.model.Tarjeta;
import comuNIcate.comuNIcate.model.Usuario;
import comuNIcate.comuNIcate.repository.ComunaRepository;
import comuNIcate.comuNIcate.repository.PublicacionRepository;
import comuNIcate.comuNIcate.repository.RegionRepository;
import comuNIcate.comuNIcate.repository.TarjetaRepository;
import comuNIcate.comuNIcate.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Random;


@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner{

    @Autowired
    private RegionRepository regRepo;

    @Autowired
    private ComunaRepository comRepo;

    @Autowired
    private UsuarioRepository usuRepo;

    @Autowired
    private PublicacionRepository pubRepo;

    @Autowired
    private TarjetaRepository tarRepo;

    @Override
    public void run(String... args) throws Exception{
        Faker faker = new Faker();
        Random random = new Random();

        //Creacion de datos en Region
        for (int i = 0; i < 3; i++) {
            Region reg = new Region();
            reg.setNombreRegion(faker.country().capital());
            regRepo.save(reg);
        }
        //Retorna todas las regiones creadas
        List<Region> regiones = regRepo.findAll();

        //Creacion de datos en Comuna
        for (int i = 0; i < 3; i++) {
            Comuna com = new Comuna();
            com.setNombreComuna(faker.address().cityName());
            com.setFkRegion(regiones.get(random.nextInt(3)));
            comRepo.save(com);
        }
        //Retorna todas las comunas creadas
        List<Comuna> comunas = comRepo.findAll();

        //Creacion de datos en Usuario
        for (int i = 0; i < 10; i++) {
            Usuario usu = new Usuario();
            usu.setNombreUsuario("Juanito"+i+1);
            usu.setClave(faker.internet().password());
            Date fechaNacimiento = java.sql.Date.valueOf(faker.timeAndDate().birthday(18,100));
            usu.setFechaNacimientoUsuario(fechaNacimiento);
            usu.setCorreoUsuario(faker.internet().emailAddress());
            usu.setFkComuna(comunas.get(random.nextInt(3)));
            usuRepo.save(usu);
        }

        //Retorna todos los usuario creados
        List<Usuario> usuarios = usuRepo.findAll();

        //Creacion de datos en Publicacion
        for (int i = 0; i < 3; i++) {
            Publicacion pub = new Publicacion();
            pub.setDescripcionPublicacion(faker.lorem().sentence());
            pub.setFechaHoraPublicacion(new Date());
            Double longitud = Double.parseDouble(faker.address().longitude());
            pub.setUbicacionLongitudPublicacion(longitud);
            Double latitud = Double.parseDouble(faker.address().latitude());
            pub.setUbicacionLatitudPublicacion(latitud);
            pub.setFkUsuario(usuarios.get(random.nextInt(10)));
            pubRepo.save(pub);
        }

        //Creacion de datos en Tarjeta
        for (int i = 0; i < 3; i++) {
            Tarjeta tar = new Tarjeta();
            tar.setNumeroPan(faker.finance().creditCard().replaceAll("-",""));
            tar.setNombreTarjeta(faker.funnyName().name());
            tar.setNombreTitular(faker.name().name());
            tar.setFechaVencimientoTarjeta(new Date());
            tar.setCodigoVerificadorTarjeta(faker.number().numberBetween(100,1000));
            tar.setFkUsuario(usuarios.get(random.nextInt(10)));
            tarRepo.save(tar);
        }



    }
}
