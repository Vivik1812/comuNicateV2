package comuNIcate.comuNIcate.controller;

import comuNIcate.comuNIcate.model.Tarjeta;
import comuNIcate.comuNIcate.services.TarjetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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

@RestController
@RequestMapping("/api/v1/tarjetas")
@Tag(name = "Tarjetas", description = "Operaciones relacionadas con tarjeta")
public class TarjetaController {
    @Autowired
    private TarjetaService tarServ;

    //Obtener todas las tarjetas
    @GetMapping
    @Operation(
        summary = "Obtener todas las tarjetas",
        description = "Obtiene una lista de todas las tarjetas"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarjetas obtenidas correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay tarjetas para mostrar")
    })
    public ResponseEntity<?> getTarjetas(){
        List<Tarjeta> tarjeta = tarServ.findAllTarjetas();
        if (tarjeta.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tarjeta);
    }
    //Obtener tarjeta por ID
    @GetMapping("/{pan}")
    @Operation(
        summary = "Obtener tarjeta por su PAN",
        description = "Obtiene las tarjetas por su PAN"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarjeta obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Tarjeta no encontrada")
    })
    public ResponseEntity<?> getTarjetaById(
        @Parameter(description = "PAN de tarjeta", required = true)
        @PathVariable String id
    ){
        try{
            Tarjeta tarjeta = tarServ.findTarjetaById(id);
            return ResponseEntity.ok(tarjeta);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();

        }
    }
    //Obtener tarjetas por usuario
    @GetMapping("/IDUsuario/{id}")
    @Operation(
        summary = "Obtiene tarjetas de un usuario",
        description = "Obtiene una lista de las tarjetas de un usuario por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarjetas obtenidas correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay tarjetas a mostrar")
    })
    public ResponseEntity<?> getTarjetasByUsuario(
        @Parameter(description = "ID de usuario", required = true) 
        @PathVariable long id
    ){
        try{
            List<Tarjeta> tarjeta = tarServ.findTarjetasByUsuario(id);
            return ResponseEntity.ok(tarjeta);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
    //Agregar una tarjeta
    @PostMapping
    @Operation(
        summary = "Agrega una tarjeta",
        description = "Agrega una tarjeta a la base de datos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarjeta agregada correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Tarjeta.class)
            )
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Tarjeta a agregar",
        required = true
    )
    public ResponseEntity<?> postTarjeta(@RequestBody Tarjeta newTarjeta){
            tarServ.saveTarjeta(newTarjeta);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //Modificar completamente una tarjeta
    @PutMapping("/{id}")
    @Operation(
        summary = "Modificar tarjeta",
        description = "Modifica completamente una tarjeta"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarjeta modificada correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Tarjeta.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Error al modificar tarjeta"),
        @ApiResponse(responseCode = "404", description = "Tarjeta no encontrada")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Tarjeta a modificar",
        required = true
    )
    public ResponseEntity<?> putTarjeta(
        @Parameter(description = "ID de tarjeta")    
        @RequestBody Tarjeta newTarjeta,@PathVariable String id
    ){
        Tarjeta tarjeta = tarServ.updateTarjeta(id,newTarjeta);
        return ResponseEntity.ok(tarjeta);
    }
    //Modificar parcialmente una tarjeta
    @PatchMapping("/{id}")
    @Operation(
        summary = "Modificar parcialmente tarjeta",
        description = "Modifica parcialmente una tarjeta"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarjeta modificada correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Tarjeta.class)
            )
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Tarjeta a modificar",
        required = true
    )
    public ResponseEntity<?> patchTarjeta(
        @Parameter(description = "ID de tarjeta")    
        @RequestBody Tarjeta newTarjeta,@PathVariable String id
    ){
        Tarjeta tarjeta = tarServ.partialUpdateTarjeta(id, newTarjeta);
        return ResponseEntity.ok(tarjeta);
    }
    //Eliminar tarjeta
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar tarjeta",
        description = "Elimina una tarjeta mediante su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tarjeta eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Tarjeta no encontrada")
    })
    public ResponseEntity<?> deleteTarjeta(
        @Parameter(description = "ID de tarjeta", required = true)
        @PathVariable String id
    ){
        try{
            tarServ.deleteTarjetaById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
