package comuNIcate.comuNIcate.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import comuNIcate.comuNIcate.controller.V2.TarjetaControllerV2;
import comuNIcate.comuNIcate.model.Tarjeta;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TarjetaModelAssembler implements RepresentationModelAssembler<Tarjeta, EntityModel<Tarjeta>>{
    @SuppressWarnings("null")
    @Override
    public EntityModel<Tarjeta> toModel(Tarjeta tarjeta){
        return EntityModel.of(tarjeta,
            linkTo(methodOn(TarjetaControllerV2.class).getAllTarjetas()).withRel("tarjetas"),
            linkTo(methodOn(TarjetaControllerV2.class).getTarjetaById(tarjeta.getNombreTarjeta())).withSelfRel(),
            linkTo(methodOn(TarjetaControllerV2.class).getTarjetasByUsuario(Long.valueOf(tarjeta.getFkUsuario().getIdUsuario()))).withRel("tarjetas-de-usuario"),
            linkTo(methodOn(TarjetaControllerV2.class).postTarjeta(tarjeta)).withRel("crear"),
            linkTo(methodOn(TarjetaControllerV2.class).deleteTarjeta(tarjeta.getNumeroPan())).withRel("eliminar"),
            linkTo(methodOn(TarjetaControllerV2.class).putTarjeta(tarjeta, tarjeta.getNumeroPan())).withRel("actualizar"),
            linkTo(methodOn(TarjetaControllerV2.class).patchTarjeta(tarjeta, tarjeta.getNumeroPan())).withRel("actualizar-parcial")
        );
        
    }
}
