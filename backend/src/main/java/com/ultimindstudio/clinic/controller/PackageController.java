package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.request.PackageRequest;
import com.ultimindstudio.clinic.dto.response.PackageResponse;
import com.ultimindstudio.clinic.service.PackageService;
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
@RequestMapping("/api/v1/packages")
@RequiredArgsConstructor
@Tag(name = "Packages")
public class PackageController {

    private final PackageService packageService;

    @GetMapping
    public ResponseEntity<Page<PackageResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(packageService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(packageService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PackageResponse> create(@Valid @RequestBody PackageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packageService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PackageResponse> update(@PathVariable Long id, @Valid @RequestBody PackageRequest request) {
        return ResponseEntity.ok(packageService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        packageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
