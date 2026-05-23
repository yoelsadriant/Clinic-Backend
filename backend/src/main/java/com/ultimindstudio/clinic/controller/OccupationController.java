package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.LookupRequest;
import com.ultimindstudio.clinic.dto.LookupResponse;
import com.ultimindstudio.clinic.service.LookupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/occupations")
@Tag(name = "Occupations")
public class OccupationController {

    private final LookupService occupationService;

    public OccupationController(@Qualifier("occupationService") LookupService occupationService) {
        this.occupationService = occupationService;
    }

    @GetMapping
    public ResponseEntity<Page<LookupResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(occupationService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LookupResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(occupationService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LookupResponse> create(@Valid @RequestBody LookupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(occupationService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LookupResponse> update(@PathVariable Long id, @Valid @RequestBody LookupRequest request) {
        return ResponseEntity.ok(occupationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        occupationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
