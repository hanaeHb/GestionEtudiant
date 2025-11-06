package com.example.gestionfaculteetudiant.web;

import com.example.gestionfaculteetudiant.dto.RequestEtudiantDto;
import com.example.gestionfaculteetudiant.dto.ResponceEtudiantDto;
import com.example.gestionfaculteetudiant.service.EtudiantServiceImpl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "Gestion des etudiants",
                description = "Ce service permet de gérer les etudiants.",
                version = "1.0.0"
        ),
        servers = @Server(
                url = "http://localhost:8083"
        )
)
@RestController
@RequestMapping("/v1/etudiants")
public class ApiRestfullEtudiant {

    private EtudiantServiceImpl etudiantService;
    public ApiRestfullEtudiant (EtudiantServiceImpl etudiantService) {
        this.etudiantService = etudiantService;
    }

    @Operation(
            summary = " Ajouter un etudiant",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RequestEtudiantDto.class )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "bien enregiter",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponceEtudiantDto.class )
                            )
                    ),

                    @ApiResponse(responseCode = "4xx",description = "erreur client"),
                    @ApiResponse(responseCode = "5xx",description = "erreur serveur"),
            }
    )

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")

    @PostMapping
    public ResponseEntity<ResponceEtudiantDto> addEtudiant(@RequestBody RequestEtudiantDto dto) {
        return ResponseEntity.ok(etudiantService.AddEtudiant(dto));
    }

    @Operation(
            summary = "Récupérer la liste complète des étudiants",
            description = "Cette API retourne tous les étudiants enregistrés dans le système, " +
                    "y compris les informations sur la filière associée à chaque étudiant.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Liste des étudiants récupérée avec succès",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponceEtudiantDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Non autorisé : JWT manquant ou invalide"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Accès interdit : l'utilisateur n'a pas les droits suffisants"
                    ),
                    @ApiResponse(
                            responseCode = "5xx",
                            description = "Erreur serveur"
                    )
            }
    )
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_USER')")

    @GetMapping
    public ResponseEntity<List<ResponceEtudiantDto>> getAllEtudiants() {
        return ResponseEntity.ok(etudiantService.GETALLEtudiants());
    }


    @Operation(
            summary = " récupérer etudiant par Id",
            parameters = @Parameter(name = "id", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "bien récuperer",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponceEtudiantDto.class )
                            )
                    ),
                    @ApiResponse(responseCode = "4xx",description = "erreur client"),
                    @ApiResponse(responseCode = "5xx",description = "erreur serveur"),
            }
    )
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_USER')")

    @GetMapping("/{id}")
    public ResponseEntity<ResponceEtudiantDto> getEtudiantById(@PathVariable Integer id) {
        return ResponseEntity.ok(etudiantService.GETEtudiantById(id));
    }

    @Operation(
            summary = " Modifier un etudiant",
            parameters = @Parameter(name = "id", required = true),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RequestEtudiantDto.class )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "bien modifier",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponceEtudiantDto.class )
                            )
                    ),

                    @ApiResponse(responseCode = "4xx",description = "erreur client"),
                    @ApiResponse(responseCode = "5xx",description = "erreur serveur"),
            }
    )

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")

    @PutMapping("/{id}")
    public ResponseEntity<ResponceEtudiantDto> updateEtudiant(@PathVariable Integer id, @RequestBody RequestEtudiantDto dto) {
        return ResponseEntity.ok(etudiantService.UPDATEEtudiant(id, dto));
    }

    @Operation(
            summary = " supprimer etudiant par Id",
            parameters = @Parameter(name = "id", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "bien supprimer"),
                    @ApiResponse(responseCode = "4xx",description = "erreur client"),
                    @ApiResponse(responseCode = "5xx",description = "erreur serveur"),
            }
    )

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Integer id) {
        etudiantService.DELETEEtudiantBYID(id);
        return ResponseEntity.noContent().build();
    }
}
