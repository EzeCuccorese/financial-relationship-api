package com.reba.rebatest.controller;

import com.reba.rebatest.model.PeopleStats;
import com.reba.rebatest.model.Person;
import com.reba.rebatest.services.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/people")
@Tag(name = "People", description = "API for managing people")
public class PersonController {

    private final PersonService personService;

    public PersonController(final PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @Operation(summary = "Get all people", responses = {
            @ApiResponse(description = "List of people", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Person.class)))
    })
    public List<Person> getAllPeople() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a person by their ID", responses = {
            @ApiResponse(description = "Person found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Person.class))),
            @ApiResponse(description = "Person not found", responseCode = "404")
    })
    public ResponseEntity<Person> getPersonById(@PathVariable("id") final Long id) {
        final Optional<Person> optionalPerson = personService.findById(id);
        return optionalPerson.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new person. Read description.",
            description = "Please note that in this version of the API it is not possible to create the person with " +
                    "relationships or parents. They should come with an empty list and null. In the case of contact " +
                    "data, it should not have an assigned id, as it is populated by the database at the time of " +
                    "creation",
            responses = {
                    @ApiResponse(description = "Person created", content = @Content(mediaType = "application/json",
                            schema =
                            @Schema(implementation = Person.class)))
            })
    public ResponseEntity<?> createPerson(@RequestBody final Person person) {
        final Person newPerson = personService.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPerson);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing person by their ID", responses = {
            @ApiResponse(description = "Person updated", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Person.class))),
            @ApiResponse(description = "Person not found", responseCode = "404")
    })
    public ResponseEntity<?> updatePerson(@PathVariable("id") final Long id, @RequestBody final Person person) {
        final Person updatedPerson = personService.update(id, person);
        if (updatedPerson != null) {
            return ResponseEntity.ok(updatedPerson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a person by their ID", responses = {
            @ApiResponse(description = "Person deleted", responseCode = "204"),
            @ApiResponse(description = "Person not found", responseCode = "404")
    })
    public ResponseEntity<?> deletePerson(@PathVariable("id") final Long id) {
        final Optional<Person> optionalPerson = personService.findById(id);

        if (optionalPerson.isPresent()) {
            personService.delete(optionalPerson.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{idFather}/father/{idChild}")
    @Operation(summary = "Establish father relationship between two people", responses = {
            @ApiResponse(description = "Relationship established", content = @Content(mediaType = "text/plain")),
            @ApiResponse(description = "Invalid person IDs", responseCode = "400")
    })
    public ResponseEntity<String> establishFather(@PathVariable("idFather") final Long idFather,
                                                  @PathVariable("idChild") final Long idChild) {
        final Optional<Person> father = personService.findById(idFather);
        final Optional<Person> child = personService.findById(idChild);

        if (father.isEmpty() || child.isEmpty()) {
            return ResponseEntity.badRequest().body("The provided IDs do not correspond to existing people");
        }

        child.get().setPather(father.get());
        personService.save(child.get());

        return ResponseEntity.ok("Set " + father.get().getName()
                + " as father of " + child.get().getName());
    }

    @GetMapping("/stats")
    @Operation(summary = "Get people statistics by country", responses = {
            @ApiResponse(description = "People statistics by country", content = @Content(mediaType =
                    "application/json", schema = @Schema(implementation = PeopleStats.class)))
    })
    public ResponseEntity<List<PeopleStats>> getStats() {
        final List<PeopleStats> stats = personService.getPercentageByCountry();
        return ResponseEntity.ok(stats);
    }
}
