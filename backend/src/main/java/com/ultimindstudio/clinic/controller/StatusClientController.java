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
@RequestMapping("/api/v1/status-clients")
@Tag(name = "Client Statuses")
public class StatusClientController {

    private final LookupService statusClientService;

    public StatusClientController(@Qualifier("statusClientService") LookupService statusClientService) {
        this.statusClientService = statusClientService;
    }

    @GetMapping
    public ResponseEntity<Page<LookupResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(statusClientService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LookupResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(statusClientService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LookupResponse> create(@Valid @RequestBody LookupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(statusClientService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LookupResponse> update(@PathVariable Long id, @Valid @RequestBody LookupRequest request) {
        return ResponseEntity.ok(statusClientService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        statusClientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
