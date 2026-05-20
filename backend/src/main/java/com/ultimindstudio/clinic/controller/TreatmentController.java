package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.request.TreatmentRequest;
import com.ultimindstudio.clinic.dto.response.TreatmentResponse;
import com.ultimindstudio.clinic.service.TreatmentService;
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
@RequestMapping("/api/v1/treatments")
@RequiredArgsConstructor
@Tag(name = "Treatments")
public class TreatmentController {

    private final TreatmentService treatmentService;

    @GetMapping
    public ResponseEntity<Page<TreatmentResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(treatmentService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(treatmentService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TreatmentResponse> create(@Valid @RequestBody TreatmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(treatmentService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TreatmentResponse> update(@PathVariable Long id, @Valid @RequestBody TreatmentRequest request) {
        return ResponseEntity.ok(treatmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        treatmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
