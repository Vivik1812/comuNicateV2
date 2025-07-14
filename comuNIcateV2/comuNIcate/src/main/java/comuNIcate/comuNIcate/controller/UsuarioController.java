package comuNIcate.comuNIcate.controller;

import comuNIcate.comuNIcate.model.Usuario;
import comuNIcate.comuNIcate.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
public class UsuarioController {
    @Autowired
    UsuarioService usuServ;
    
    //Obtiene todos los usuario
    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene una lista de todos los usuarios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios obtenidos correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay usuarios a mostrar")
    })
    public ResponseEntity<?> getUsuarios(){
        List<Usuario> usuarios = usuServ.findAllUsuarios();
        if (usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }
    //Obtiene resumen de todos los usuarios
    @GetMapping("/resumen")
    @Operation(
        summary = "Obtiene detalles de todos los usuarios",
        description = "Obtiene una lista de detalles de todos los usuarios(Nombre de Usuario, edad, Comuna, Region)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumen de usuarios obtenido correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay usuarios a mostrar")
    })
    public ResponseEntity<?> getDetallesUsuario(){
        try{
            List<Map<String,Object>> resumen = usuServ.findDetallesUsuarios();
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
    //Obtiene un usuario por ID
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener usuario por ID", 
        description = "Obtiene a un usuario por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> getUsuarioByID(
        @Parameter(description = "ID del Usuario", required = true)
        @PathVariable Long id
    ){
        try{
            Usuario usuario = usuServ.findUsuarioById(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
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
            return ResponseEntity.ok(usuario);
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
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //Guarda un usuario
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
    public ResponseEntity<?> postUsuario(@RequestBody Usuario newUsuario){
        usuServ.saveUsuario(newUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //Modifica completamente un usuario
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
    public ResponseEntity<?> putUsuario(
        @Parameter(description = "ID del usuario", required = true)
        @RequestBody Usuario newUsuario, 
        @PathVariable Long id
    ){
        try {
            Usuario usuario = usuServ.updateUsuario(id, newUsuario);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //Modifica parcialmente a un usuario
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
    public ResponseEntity<?> patchUsuario(
        @Parameter(description = "ID del Usuario") 
        @RequestBody Usuario newUsuario, 
        @PathVariable Long id
    ){
        Usuario usuario = usuServ.partialUpdateUsuario(id, newUsuario);
        return ResponseEntity.ok(usuario);
    }
    //Eliminar un usuario
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar usuario", 
        description = "Se elimina a un usuario por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> deleteUsuario(
        @Parameter(description = "ID del Usuario", required = true)
        @PathVariable Long id
    ){
        Usuario usuario = usuServ.findUsuarioById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuServ.deleteUsuarioById(id);
        return ResponseEntity.noContent().build();
    }
}