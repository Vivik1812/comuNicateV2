package comuNIcate.comuNIcate.controller;

import comuNIcate.comuNIcate.model.Comuna;
import comuNIcate.comuNIcate.services.ComunaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/v1/comunas")
@Tag(name = "Comunas", description = "Operaciones relacionadas con comunas")
public class ComunaController {
    @Autowired
    ComunaService comServ;
    
    //Obtener todas las comunas
    @GetMapping
    @Operation(
        summary = "Obtener comunas",
        description = "Obtiene todas las comunas"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comunas obtenidas correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay comunas a mostrar")
    })
    public ResponseEntity<?> getComunas(){
        List<Comuna> comuna = comServ.findAllComunas();
        if (comuna.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comServ.findAllComunas());
    }
    //Obtener una comuna por su ID
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener comuna por ID", 
        description = "Obtiene una comuna por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comuna obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    public ResponseEntity<?> getComunasById(
        @Parameter(description = "ID de Comuna", required = true)
        @PathVariable Integer id
    ){
        try{
            Comuna comuna = comServ.findComunaById(id);
            return ResponseEntity.ok(comuna);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    //Obtener una comuna por su nombre
    @GetMapping("/nombre/{nombre}")
    @Operation(
        summary = "Obtener comuna por nombre", 
        description = "Obtiene una comuna mediante su nonbre"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comuna obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    public ResponseEntity<?> getComunaByNombre(
        @Parameter(description = "Nombre de comuna", required = true) 
        @PathVariable String nombre
    ){
        try{
            Comuna comuna = comServ.findComunaByNombre(nombre);
            return ResponseEntity.ok(comuna);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //Guardar una comuna
    @PostMapping
    @Operation(
        summary = "Agregar una comuna",
        description = "Agrega una comuna a la base de datos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Comuna agregada correctamente",
            content = @Content(
                mediaType = "application",
                schema = @Schema(implementation = Comuna.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Error al agregar comuna")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Comuna a agregar", 
        required = true
    )
    public ResponseEntity<?> saveComuna(@RequestBody Comuna newComuna){
        Comuna comuna = comServ.saveComuna(newComuna);
        return ResponseEntity.ok(comuna);
    }
    //Modificar una comuna
    @PutMapping("/{id}")
    @Operation(
        summary = "Modificar comuna",
        description = "Modifica completamente una comuna"
    )    
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comuna obtenida correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Comuna.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Comuna a modificar",
        required = true
    )
    public ResponseEntity<?> putComuna(
        @Parameter(description = "ID de comuna", required = true) 
        @RequestBody Comuna newComuna, 
        @PathVariable Integer id
    ){
        Comuna comuna = comServ.updateComuna(id,newComuna);
        return ResponseEntity.ok(comuna);
    }
    //Modificar parcialmente una comuna
    @PatchMapping("/{id}")
    @Operation(
        summary = "Modificar parcialmente una comuna", 
        description = "Modifica parcialmente a una comuna"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comuna modificada correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Comuna.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Comuna a modificar",
        required = true
    )
    public ResponseEntity<?> patchComuna(
        @Parameter(description = "ID de comuna", required = true)
        @RequestBody Comuna newComuna, 
        @PathVariable Integer id
    ){
        Comuna comuna = comServ.partialUpdateComuna(id, newComuna);
        return ResponseEntity.ok(comuna);

    }
    //Eliminar una comuna
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar comuna",
        description = "Elimina una comuna"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Comuna eliminada"),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    public ResponseEntity<?> deleteComuna(
        @Parameter(description = "ID de comuna", required = true)
        @PathVariable Integer id
    ){
        try{
            comServ.deleteComunaById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
