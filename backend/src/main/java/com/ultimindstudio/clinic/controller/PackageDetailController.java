package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.request.PackageDetailRequest;
import com.ultimindstudio.clinic.dto.response.PackageDetailResponse;
import com.ultimindstudio.clinic.service.PackageDetailService;
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
@RequestMapping("/api/v1/package-details")
@RequiredArgsConstructor
@Tag(name = "Package Details")
public class PackageDetailController {

    private final PackageDetailService packageDetailService;

    @GetMapping
    public ResponseEntity<Page<PackageDetailResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(packageDetailService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageDetailResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(packageDetailService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PackageDetailResponse> create(@Valid @RequestBody PackageDetailRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packageDetailService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PackageDetailResponse> update(@PathVariable Long id, @Valid @RequestBody PackageDetailRequest request) {
        return ResponseEntity.ok(packageDetailService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        packageDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
