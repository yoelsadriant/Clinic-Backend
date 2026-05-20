package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.request.BeauticianRequest;
import com.ultimindstudio.clinic.dto.response.BeauticianResponse;
import com.ultimindstudio.clinic.service.BeauticianService;
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
@RequestMapping("/api/v1/beauticians")
@RequiredArgsConstructor
@Tag(name = "Beauticians")
public class BeauticianController {

    private final BeauticianService beauticianService;

    @GetMapping
    public ResponseEntity<Page<BeauticianResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(beauticianService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeauticianResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(beauticianService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BeauticianResponse> create(@Valid @RequestBody BeauticianRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beauticianService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BeauticianResponse> update(@PathVariable Long id, @Valid @RequestBody BeauticianRequest request) {
        return ResponseEntity.ok(beauticianService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        beauticianService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
