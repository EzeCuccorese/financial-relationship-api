package com.fintech.relationship.controller;

import com.fintech.relationship.model.Person;
import com.fintech.relationship.model.Relationship;
import com.fintech.relationship.services.PersonService;
import com.fintech.relationship.services.RelationshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/relationships")
@Tag(name = "Relationships", description = "API for managing relationships between people")
public class RelationshipController {

    private final RelationshipService relationshipService;
    private final PersonService personService;

    public RelationshipController(final RelationshipService relationshipService, final PersonService personService) {
        this.relationshipService = relationshipService;
        this.personService = personService;
    }

    @GetMapping("/{id1}/{id2}")
    @Operation(summary = "Get relationship between two people by their IDs", responses = {
            @ApiResponse(description = "Relationship found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(description = "Invalid person IDs", responseCode = "400"),
            @ApiResponse(description = "Relationship not found", responseCode = "404")
    })
    public ResponseEntity<String> getRelationship(@PathVariable("id1") final Long id1,
                                                  @PathVariable("id2") final Long id2) {
        final Optional<Person> person1 = personService.findById(id1);
        final Optional<Person> person2 = personService.findById(id2);

        if (person1.isEmpty() || person2.isEmpty()) {
            return ResponseEntity.badRequest().body("The provided IDs do not correspond to existing people");
        }

        final Relationship relationship = relationshipService.getRelation(person1.get(), person2.get());

        if (relationship == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(relationship.getRelationshipType().getDescription());
    }
}
