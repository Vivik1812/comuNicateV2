package comuNIcate.comuNIcate.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import comuNIcate.comuNIcate.model.Publicacion;
import comuNIcate.comuNIcate.controller.V2.PublicacionControllerV2;

@Component
public class PublicacionModelAssembler implements RepresentationModelAssembler<Publicacion, EntityModel<Publicacion>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<Publicacion> toModel(Publicacion publicacion) {
        return EntityModel.of(publicacion,
            linkTo(methodOn(PublicacionControllerV2.class).getPublicacionById(Long.valueOf(publicacion.getIdPublicacion()))).withSelfRel(),
            linkTo(methodOn(PublicacionControllerV2.class).getAllPublicaciones()).withRel("publicaciones"),
            linkTo(methodOn(PublicacionControllerV2.class).getPublicacionesByUsuario(Long.valueOf(publicacion.getFkUsuario().getIdUsuario()))).withRel("publicaciones-del-usuario"),
            linkTo(methodOn(PublicacionControllerV2.class).deletePublicacion(Long.valueOf(publicacion.getIdPublicacion()))).withRel("eliminar"),
            linkTo(methodOn(PublicacionControllerV2.class).updatePublicacion(Long.valueOf(publicacion.getIdPublicacion()), publicacion)).withRel("actualizar"),
            linkTo(methodOn(PublicacionControllerV2.class).patchPublicacion(Long.valueOf(publicacion.getIdPublicacion()), publicacion)).withRel("actualizar-parcial")
            
        );
    }
}