package comuNIcate.comuNIcate.controller.V2;

import comuNIcate.comuNIcate.assemblers.UsuarioModelAssembler;
import comuNIcate.comuNIcate.model.Usuario;
import comuNIcate.comuNIcate.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v2/usuarios")
@Tag(name = "Usuario V2", description = "Operaciones relacionadas con usuario")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuServ;

    @Autowired
    private UsuarioModelAssembler assembler;
    //Obtiene todos los usuarios
    @GetMapping
    @Operation(
        summary = "Obtener usuarios",
        description = "Obtiene una lista de todas los usuarios"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios obtenidas correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay usuarios a mostrar")
    })
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> getAllUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuServ.findAllUsuarios().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        if (usuarios == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(usuarios,
            linkTo(methodOn(UsuarioControllerV2.class).getAllUsuarios()).withSelfRel()));
    }
    //Obtiene un usuario por su ID
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener usuario por ID",
        description = "Obtiene un usuario mediante su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<EntityModel<Usuario>> getUsuarioById(
        @Parameter(description = "ID de usuario", required = true)
        @PathVariable Long id
    ){
        Usuario usuario = usuServ.findUsuarioById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();  
        }
        return ResponseEntity.ok(assembler.toModel(usuario));   
    }
    //Obtiene un usuario por correo
    @GetMapping("/correo/{correo}")
    @Operation(
        summary = "Obtener usuario por correo", 
        description = "Obtiene a un usuario por su correo"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> getUsuarioByCorreo(
        @Parameter(description = "Correo del usuario", required = true)
        @PathVariable String correo
    ){
        try{
            Usuario usuario = usuServ.findUsuarioByCorreo(correo);
            return ResponseEntity.ok(assembler.toModel(usuario));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //Obtiene un usuario por nombre de Usuario
    @GetMapping("/nombreusuario/{nombre}")
    @Operation(
        summary = "Obtener usuario por nombre de usuario",
        description = "Obtiene a un usuario por su nombre de usuario"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> getUsuarioByNombreUsuario(
        @Parameter(description = "Nombre de Usuario", required = true)
        @PathVariable String nombre
    ){
        try{
            Usuario usuario = usuServ.findUsuarioByNombreUsuario(nombre);
            return ResponseEntity.ok(assembler.toModel(usuario));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //Obtiene la cantidad de usuarios
    @GetMapping("/nombreregion/{nombreRegion}")
    @Operation(
        summary = "Obtener cantidad de usuarios en una region",
        description = "Obtiene la cantidad de usuarios por el nombre de una region"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cantidad obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Region no encontrada")
    })
    public ResponseEntity<?> getCantidadUsuariosByRegion(
        @Parameter(description = "Nombre de region", required = true)
        @PathVariable String nombreRegion
    ){
        Long cantidadUsuarios = usuServ.cantidadUsuarioByRegion(nombreRegion);
        return ResponseEntity.ok(cantidadUsuarios);
    }
    //Obtiene los usuarios que hayan nacido entre 2 fechas
    @GetMapping(value = "/fechanac/{fechaInicial}/{fechaFinal}")
    @Operation(
        summary = "Obtiene usuarios por fecha de nacimiento",
        description = "Obtiene una lista de usuario que hayan nacido entre 2 fechas dadas"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios obtenidos correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay usuario para mostrar")
    })
    public ResponseEntity<?> getUsuariosByFechaNacimiento(
        @Parameter(description = "Fecha inicial y fecha final", required = true)
        @PathVariable Date fechaInicial,
        @PathVariable Date fechaFinal){
        List<Usuario> usuarios = usuServ.findUsuariosByFechaNacimiento(fechaInicial, fechaFinal);
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }
    //Agrega un usuario
    @PostMapping
    @Operation(
        summary = "Agregar un usuario", 
        description = "Agrega a un usuario a la base de datos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario agregado correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Error al agregar Usuario")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Usuario a crear", 
        required = true
    )
    public ResponseEntity<EntityModel<Usuario>> postUsuario(@RequestBody Usuario newUsuario) {
        Usuario usuario = usuServ.saveUsuario(newUsuario);
        return ResponseEntity
            .created(linkTo(methodOn(UsuarioControllerV2.class).getUsuarioById(usuario.getIdUsuario())).toUri())
            .body(assembler.toModel(usuario));
    }
    //Modifica un usuario
    @PutMapping("/{id}")
    @Operation(
        summary = "Modificar usuario", 
        description = "Modifica completamente a un usuario"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "El usuario fue modificado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Error al modificar el usuario"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Usuario a modificar", 
        required = true
    )
    public ResponseEntity<EntityModel<Usuario>> putUsuario(@PathVariable Long id, @RequestBody Usuario newUsuario) {
        try {
            Usuario usuario = usuServ.updateUsuario(id, newUsuario);
            return ResponseEntity.ok(assembler.toModel(usuario));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //Modifica parcialmente un usuario
    @PatchMapping("/{id}")
    @Operation(
        summary = "Modificar parcialmente usuario", 
        description = "Modifica parcialmente a un usuario"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario modificado correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Error al modificar usuario"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Usuario a modificar", 
        required = true
    )
    public ResponseEntity<EntityModel<Usuario>> patchUsuario(
        @PathVariable Long id, 
        @RequestBody Usuario newUsuario
    ){
        try {
            Usuario usuario = usuServ.partialUpdateUsuario(id, newUsuario);
            return ResponseEntity.ok(assembler.toModel(usuario));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //Elimina un usuario
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar usuario", 
        description = "Se elimina a un usuario por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        Usuario usuario = usuServ.findUsuarioById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuServ.deleteUsuarioById(id);
        return ResponseEntity.noContent().build();
    }
}
