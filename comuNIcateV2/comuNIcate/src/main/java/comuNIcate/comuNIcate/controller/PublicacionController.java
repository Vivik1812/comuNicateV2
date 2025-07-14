package comuNIcate.comuNIcate.controller;

import comuNIcate.comuNIcate.model.Publicacion;
import comuNIcate.comuNIcate.services.PublicacionService;
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
@RequestMapping("/api/v1/publicaciones")
@Tag(name = "Publicaciones", description = "Operaciones relacionadas con publicacion")
public class PublicacionController {
    @Autowired
    PublicacionService pubServ;
    
    //Obtener publicaciones
    @GetMapping
    @Operation(
        summary = "Obtener publicaciones",
        description = "Obtiene una lista de todas las publicaciones"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Obtiene todas las regiones correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay publicaciones a mostrar")
    })
    public ResponseEntity<?> getPublicaciones(){
        List<Publicacion> publicaciones = pubServ.findAllPublicaciones();
        if (publicaciones.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(publicaciones);
    }
    //Obtener resumen de publicaciones
    @GetMapping("/resumen")
    @Operation(
        summary = "Obtener resumen de publicaciones",
        description = "Obtiene un resumen detallado de las "+
        "publicaciones(Descripcion, Usuario, Ubicacion, Comuna y Region)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumen obtenido correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay publicaciones a mostrar")
    })
    public ResponseEntity<?> getDetallesPublicaciones(){

        try{
            List<Map<String,Object>> publicacion = pubServ.findDetallesPublicaciones();
            return ResponseEntity.ok(publicacion);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
    //Obtener publicacion por ID
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener publicacion por ID",
        description = "Obtiene una publicacion mediante su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Publicacion obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Publicacion no encontrada")
    })
    public ResponseEntity<?> getPublicacionById(
        @Parameter(description = "ID de publicacion", required = true)
        @PathVariable Long id
    ){
        try{
            Publicacion publicacion = pubServ.findPublicacionById(id);
            return ResponseEntity.ok(publicacion);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //Obtener publicacion por IDUsuario
    @GetMapping("/IDUsuario/{id}")
    @Operation(
        summary = "Obtener publicacion por usuario",
        description = "Obtiene una lista con las publicaciones de un usuario mediante su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Publicaciones obtenidas con exito"),
        @ApiResponse(responseCode = "204",description = "No hay publicaciones a mostrar")
    })
    public ResponseEntity<?> getPublicacionByUsuario(
        @Parameter(description = "ID Usuario", required = true) 
        @PathVariable Long id
    ){
        List<Publicacion> publicacion = pubServ.findPublicacionesByIdUsuario(id);
        if (publicacion.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
            return ResponseEntity.ok(publicacion);
    }
    //Guardar una publicacion en la base de datos
    @PostMapping
    @Operation(
        summary = "Agregar publicacion",
        description = "Agrega una publicacion a la base de datos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Publicacion agregada correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Publicacion.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Error al agregar publicacion")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Publicacion a agregar", 
        required = true
    )
    public ResponseEntity<?> postPublicacion(@RequestBody Publicacion newPublicacion){
        pubServ.savePublicacion(newPublicacion);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //Moificar publicacion
    @PutMapping("/{id}")
    @Operation(
        summary = "Modificar una publicacion",
        description = "Modifica completamente una publicacion"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Publicacion modificada correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Publicacion.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Error al modificar publicacion")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Publicacion a modificar",
        required = true
    )
    public ResponseEntity<?> putPublicacion(
        @Parameter(description = "ID publicacion", required = true) 
        @RequestBody Publicacion newPublicacion, 
        @PathVariable Long id
    ){
        Publicacion publicacion = pubServ.updatePublicacion(id, newPublicacion);
        return ResponseEntity.ok(publicacion);
    }
    //Modificar parcialmente publicacion
    @PatchMapping("/{id}")
    @Operation(
        summary = "Modificar una publicacion",
        description = "Modifica completamente una publicacion"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Publicacion modificada correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Publicacion.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Publicacion no encontrada"),
        @ApiResponse(responseCode = "400", description = "Error al modificar publicacion")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Publicacion a modificar",
        required = true
    )
    public ResponseEntity<?> patchPublicacion(
        @Parameter(description = "ID de publicacion", required = true) 
        @RequestBody Publicacion newPublicacion, @PathVariable Long id
    ){
        Publicacion publicacion = pubServ.partialUpdatePublicacion(id, newPublicacion);
        return ResponseEntity.ok(publicacion);
    }
    //Eliminar publicacion
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar publicacion",
        description = "Elimina una publicacion mediante su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Publicacion eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Publicacion no encontrada")
    })
    public ResponseEntity<?> deletePublicacion(
        @Parameter(description = "ID de publicacion", required = true)
        @PathVariable Long id){
        try{
           pubServ.deletePublicacionById(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}