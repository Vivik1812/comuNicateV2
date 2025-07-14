package comuNIcate.comuNIcate.controller.V2;

import comuNIcate.comuNIcate.assemblers.ComunaModelAssembler;
import comuNIcate.comuNIcate.model.Comuna;
import comuNIcate.comuNIcate.services.ComunaService;
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
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/comunas")
@Tag(name = "Comunas V2", description = "Operaciones relacionadas con comuna")
public class ComunaControllerV2 {

    @Autowired
    private ComunaService comServ;

    @Autowired
    private ComunaModelAssembler assembler;
    //Obtiene todas las comuans
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Obtener comunas",
        description = "Obtiene una lista de todas las comunas"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comuans obtenidas correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay comunas a mostrar")
    })
    public ResponseEntity<CollectionModel<EntityModel<Comuna>>> getAllComuna(){
        List<EntityModel<Comuna>> comunas = comServ.findAllComunas().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        if (comunas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(comunas,
            linkTo(methodOn(ComunaControllerV2.class).getAllComuna()).withSelfRel())
        );
    }
    //Obtiene una comuna por su ID
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Obtener comuna por ID",
        description = "Obtiene una comiuna por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comuna obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    public ResponseEntity<EntityModel<Comuna>> getComunaById(
        @Parameter(description = "ID de comuna", required = true)
        @PathVariable Integer id
    ){
        Comuna comuna = comServ.findComunaById(id);
        if (comuna == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(comuna));
    }
    //Agrega una comuna
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Agregar comuna",
        description = "Agrega una comuna en la base de datos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comuna guardada correctamente",
            content = @Content(
                schema = @Schema(implementation = Comuna.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Error al agregar comuna")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Comuna a agregar",
        required = true
    )
    public ResponseEntity<EntityModel<Comuna>> createComuna(@RequestBody Comuna comuna){
        Comuna newComuna = comServ.saveComuna(comuna);
        return ResponseEntity
            .created(linkTo(methodOn(ComunaControllerV2.class).getComunaById(newComuna.getIdComuna())).toUri())
            .body(assembler.toModel(newComuna));
    }

    //Modifica una comuna
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Modificar comuna",
        description = "Modifica completamente una comuna"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comuna modificada correctamente",
            content = @Content(
                schema = @Schema(implementation = Comuna.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada"),
        @ApiResponse(responseCode = "400", description = "Error al modificar comuna")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Comuna a modificar",
        required = true
    )
    public ResponseEntity<EntityModel<Comuna>> updateComuna(
        @Parameter(description = "ID de comuna")
        @PathVariable Integer id, @RequestBody Comuna newComuna
    ){  
        Comuna comuna = comServ.updateComuna(id, newComuna);
        if (comuna == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(comuna));
    }
    //Modifica parcialmente una comuna
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Modificar comuna",
        description = "Modifica parcialmente una comuna"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comuna modificada correctamente",
            content = @Content(
                schema = @Schema(implementation = Comuna.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada"),
        @ApiResponse(responseCode = "400", description = "Error al modificar comuna")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Comuna a modificar",
        required = true
    )
    public ResponseEntity<EntityModel<Comuna>> patchComuna(
        @Parameter(description = "ID de comuna", required = true)
        @PathVariable Integer id, @RequestBody Comuna comuna
    ){
        Comuna updated = comServ.partialUpdateComuna(id, comuna);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(updated));
    }
    //Elimina una comuna
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Eliminar comuna",
        description = "Elimina una comuna por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Comuna eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada")
    })
    public ResponseEntity<Void> deleteComuna(
        @Parameter(description = "ID de comuna", required = true)
        @PathVariable Integer id
    ){
        Comuna comuna = comServ.findComunaById(id);
        if (comuna == null) {
            return ResponseEntity.notFound().build();
        }
        comServ.deleteComunaById(id);
        return ResponseEntity.noContent().build();
    }
}