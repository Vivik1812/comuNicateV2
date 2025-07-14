package comuNIcate.comuNIcate.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import comuNIcate.comuNIcate.model.Comuna;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import comuNIcate.comuNIcate.controller.V2.ComunaControllerV2;


@Component
public class ComunaModelAssembler implements RepresentationModelAssembler<Comuna, EntityModel<Comuna>>{

    @SuppressWarnings("null")
    @Override
    public EntityModel<Comuna> toModel(Comuna comuna){
        return EntityModel.of(comuna,
            linkTo(methodOn(ComunaControllerV2.class).getComunaById(comuna.getIdComuna())).withSelfRel(),
            linkTo(methodOn(ComunaControllerV2.class).getAllComuna()).withRel("comunas"),
            linkTo(methodOn(ComunaControllerV2.class).updateComuna(comuna.getIdComuna(),comuna)).withRel("actualizar"),
            linkTo(methodOn(ComunaControllerV2.class).deleteComuna(comuna.getIdComuna())).withRel("eliminar"),      
            linkTo(methodOn(ComunaControllerV2.class).patchComuna(comuna.getIdComuna(),comuna)).withRel("actualizar-parcial")
        );
    }
}
