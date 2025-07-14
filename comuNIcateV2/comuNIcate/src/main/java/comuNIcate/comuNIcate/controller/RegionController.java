package comuNIcate.comuNIcate.controller;

import comuNIcate.comuNIcate.model.Region;
import comuNIcate.comuNIcate.services.RegionService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regiones")
@Tag(name = "Regiones", description = "Metodos relacionados con region")
public class RegionController {
    @Autowired
    private RegionService regServ;
   
    //Obtiene todas las regiones
    @GetMapping
    @Operation(
        summary = "Obtener todas las regiones", 
        description = "Obtiene una lista de todas las regiones"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Regiones obtenidas correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay regiones para mostrar")
    })
    public ResponseEntity<?> getRegiones(){
        List<Region> regiones = regServ.findAllRegiones();
        if (regiones.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(regiones);
    }
    //Obtiene una region por ID
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener region por ID", 
        description = "Obtiene una region mediante su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Region obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Region no encontrada")
    })
    public ResponseEntity<?> getRegionById(
        @Parameter(description = "ID de la Region", required = true)
        @PathVariable Integer id
    ){
        try{
            Region region = regServ.findRegionById(id);
            return ResponseEntity.ok(region);
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //Obtiene una region por nombre
    @GetMapping("/nombre/{nombre}")
    @Operation(
        summary = "Obtener region por nombre",
        description = "Obtiene una region mediante el nombre de la region"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Region obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Region no encontrada")
    })
    public ResponseEntity<?> getRegionByNombre(
        @Parameter(description = "Nombre de la Region", required = true)
        @PathVariable String nombre){
        try{
            Region region = regServ.findRegionByNombre(nombre);
            return ResponseEntity.ok(region);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    //Guarda una region
    @PostMapping
    @Operation(
        summary = "Guardar region",
        description = "Guarda una region en la base de datos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Region guardada correctamente",
            content = @Content(
                mediaType = "appication/json",
                schema = @Schema(implementation = Region.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Error al agregar region")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Region a crear", 
        required = true
    )
    public ResponseEntity<?> postRegion(@RequestBody Region newRegion){
            regServ.saveRegion(newRegion);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //Modifica una region
    @PatchMapping("/{id}")
    @Operation(
        summary = "Modificar region",
        description = "Modifica completamente una region"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario modificado correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Region.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Region no encontrada"),
        @ApiResponse(responseCode = "400", description = "Error al modificar region")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Region a modificar",
        required = true
    )
    public ResponseEntity<?> putRegion(
        @Parameter(description = "ID de la Region", required = true) 
        @PathVariable Integer id, 
        @RequestBody Region newRegion
    ){
        regServ.updateNombreRegion(id, newRegion);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //Elimina una region
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar region",
        description = "Elimina una region por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Region eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Region no encontrada")
    })
    public ResponseEntity<?> deleteRegion(
        @Parameter(description = "ID de Region", required = true)
        @PathVariable Integer id
    ){
        try{
            regServ.deleteRegion(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}