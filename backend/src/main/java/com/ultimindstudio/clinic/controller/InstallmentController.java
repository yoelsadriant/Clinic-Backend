package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.request.InstallmentRequest;
import com.ultimindstudio.clinic.dto.response.InstallmentResponse;
import com.ultimindstudio.clinic.service.InstallmentService;
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
@RequestMapping("/api/v1/installments")
@RequiredArgsConstructor
@Tag(name = "Installments")
@PreAuthorize("hasRole('ADMIN')")
public class InstallmentController {

    private final InstallmentService installmentService;

    @GetMapping
    public ResponseEntity<Page<InstallmentResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(installmentService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstallmentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(installmentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<InstallmentResponse> create(@Valid @RequestBody InstallmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(installmentService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstallmentResponse> update(@PathVariable Long id, @Valid @RequestBody InstallmentRequest request) {
        return ResponseEntity.ok(installmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        installmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
