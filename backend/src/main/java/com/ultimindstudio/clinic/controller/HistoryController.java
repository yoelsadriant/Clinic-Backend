package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.request.HistoryRequest;
import com.ultimindstudio.clinic.dto.response.HistoryResponse;
import com.ultimindstudio.clinic.service.HistoryService;
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
@RequestMapping("/api/v1/histories")
@RequiredArgsConstructor
@Tag(name = "Histories")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<Page<HistoryResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(historyService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoryResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(historyService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<HistoryResponse> create(@Valid @RequestBody HistoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historyService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<HistoryResponse> update(@PathVariable Long id, @Valid @RequestBody HistoryRequest request) {
        return ResponseEntity.ok(historyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        historyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
