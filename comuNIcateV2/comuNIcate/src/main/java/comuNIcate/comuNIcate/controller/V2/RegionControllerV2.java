package comuNIcate.comuNIcate.controller.V2;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import comuNIcate.comuNIcate.assemblers.RegionModelAssembler;
import comuNIcate.comuNIcate.model.Region;
import comuNIcate.comuNIcate.services.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/regiones")
@Tag(name = "Regiones V2", description = "Operaciones relacionadas con region")
public class RegionControllerV2 {
    
    @Autowired
    private RegionService regServ;
    @Autowired 
    private RegionModelAssembler assembler;

    //Obtiene todas las regiones
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Obtener regiones",
        description = "Obtiene una lista de todas las regiones"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Regiones obtenidas correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay regiones a mostrar")
    })
    public ResponseEntity<CollectionModel<EntityModel<Region>>> getAllRegiones(){
        List<EntityModel<Region>> regiones = regServ.findAllRegiones().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        if (regiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(regiones,
            linkTo(methodOn(RegionControllerV2.class).getAllRegiones()).withSelfRel()
        ));
    }
    //Obtiene una region por ID
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Obtener region por ID",
        description = "Obtiene una region por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Region obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Region no encontrada")
    })
    public ResponseEntity<EntityModel<Region>> getRegionById(
        @Parameter(description = "ID de region", required = true)
        @PathVariable Integer id
    ){
        Region region = regServ.findRegionById(id);
        if (region==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(region));
    }
    //Agrega una region
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Agregar region",
        description = "Agrega una region en la base de datos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Region guardada correctamente",
            content = @Content(
                schema = @Schema(implementation = Region.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Error al agregar region")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Region a agregar",
        required = true
    )
    public ResponseEntity<EntityModel<Region>> createComuna(@RequestBody Region newRegion) {
        Region region = regServ.saveRegion(newRegion);
        return ResponseEntity
            .created(linkTo(methodOn(RegionControllerV2.class).getRegionById(region.getIdRegion())).toUri())
            .body(assembler.toModel(region));
    }
    //Modifica una region
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Modificar region",
        description = "Modifica el nombre de una region"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Region modificada correctamente",
            content = @Content(
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
    public ResponseEntity<EntityModel<Region>> patchRegion(
        @Parameter(description = "ID de Region", required = true)
        @PathVariable Integer id, @RequestBody Region newRegion
    ){
        Region updateRegion = regServ.updateNombreRegion(id, newRegion);
        if (updateRegion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updateRegion));
    }
    //Elimina una region
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Eliminar region",
        description = "Elimina una region por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Region eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Region no encontrada")
    })
    public ResponseEntity<Void> deleteRegion(
        @Parameter(description = "ID de Region", required = true)
        @PathVariable Integer id
    ){
        Region region = regServ.findRegionById(id);
        if (region == null) {
            return ResponseEntity.notFound().build();
        }
        regServ.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
}
