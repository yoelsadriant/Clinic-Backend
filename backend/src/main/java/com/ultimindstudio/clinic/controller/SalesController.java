package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.request.SalesRequest;
import com.ultimindstudio.clinic.dto.response.SalesResponse;
import com.ultimindstudio.clinic.service.SalesService;
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
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
@Tag(name = "Sales")
@PreAuthorize("hasRole('ADMIN')")
public class SalesController {

    private final SalesService salesService;

    @GetMapping
    public ResponseEntity<Page<SalesResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(salesService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(salesService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SalesResponse> create(@Valid @RequestBody SalesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(salesService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesResponse> update(@PathVariable Long id, @Valid @RequestBody SalesRequest request) {
        return ResponseEntity.ok(salesService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
