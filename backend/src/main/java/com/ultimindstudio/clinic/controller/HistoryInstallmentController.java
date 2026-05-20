package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.request.HistoryInstallmentRequest;
import com.ultimindstudio.clinic.dto.response.HistoryInstallmentResponse;
import com.ultimindstudio.clinic.service.HistoryInstallmentService;
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
@RequestMapping("/api/v1/history-installments")
@RequiredArgsConstructor
@Tag(name = "History Installments")
@PreAuthorize("hasRole('ADMIN')")
public class HistoryInstallmentController {

    private final HistoryInstallmentService historyInstallmentService;

    @GetMapping
    public ResponseEntity<Page<HistoryInstallmentResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(historyInstallmentService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoryInstallmentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(historyInstallmentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<HistoryInstallmentResponse> create(@Valid @RequestBody HistoryInstallmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historyInstallmentService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoryInstallmentResponse> update(@PathVariable Long id, @Valid @RequestBody HistoryInstallmentRequest request) {
        return ResponseEntity.ok(historyInstallmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        historyInstallmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
