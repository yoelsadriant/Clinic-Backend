package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.request.ClientPackageRequest;
import com.ultimindstudio.clinic.dto.response.ClientPackageResponse;
import com.ultimindstudio.clinic.service.ClientPackageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/client-packages")
@RequiredArgsConstructor
@Tag(name = "Client Packages")
public class ClientPackageController {

    private final ClientPackageService clientPackageService;

    @GetMapping
    public ResponseEntity<Page<ClientPackageResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(clientPackageService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientPackageResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(clientPackageService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ClientPackageResponse> create(@Valid @RequestBody ClientPackageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientPackageService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ClientPackageResponse> update(@PathVariable Long id, @Valid @RequestBody ClientPackageRequest request) {
        return ResponseEntity.ok(clientPackageService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientPackageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
