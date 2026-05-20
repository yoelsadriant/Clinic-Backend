package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.request.ClientPackageDetailRequest;
import com.ultimindstudio.clinic.dto.response.ClientPackageDetailResponse;
import com.ultimindstudio.clinic.service.ClientPackageDetailService;
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
@RequestMapping("/api/v1/client-package-details")
@RequiredArgsConstructor
@Tag(name = "Client Package Details")
public class ClientPackageDetailController {

    private final ClientPackageDetailService clientPackageDetailService;

    @GetMapping
    public ResponseEntity<Page<ClientPackageDetailResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(clientPackageDetailService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientPackageDetailResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(clientPackageDetailService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ClientPackageDetailResponse> create(@Valid @RequestBody ClientPackageDetailRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientPackageDetailService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ClientPackageDetailResponse> update(@PathVariable Long id, @Valid @RequestBody ClientPackageDetailRequest request) {
        return ResponseEntity.ok(clientPackageDetailService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientPackageDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
