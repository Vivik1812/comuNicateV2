package comuNIcate.comuNIcate.assemblers;

import comuNIcate.comuNIcate.controller.V2.UsuarioControllerV2;
import comuNIcate.comuNIcate.model.Usuario;
import org.springframework.hateoas.EntityModel;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
            linkTo(methodOn(UsuarioControllerV2.class).getUsuarioById(Long.valueOf(usuario.getIdUsuario()))).withSelfRel(),
            linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withRel("usuarios"),
            linkTo(methodOn(UsuarioControllerV2.class).postUsuario(usuario)).withRel("crear"),
            linkTo(methodOn(UsuarioControllerV2.class).putUsuario(Long.valueOf(usuario.getIdUsuario()), usuario)).withRel("actualizar"),
            linkTo(methodOn(UsuarioControllerV2.class).patchUsuario(Long.valueOf(usuario.getIdUsuario()), usuario)).withRel("actualizar-parcial"),
            linkTo(methodOn(UsuarioControllerV2.class).deleteUsuario(Long.valueOf(usuario.getIdUsuario()))).withRel("eliminar")
        );
    }
}
