package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.request.StaffRequest;
import com.ultimindstudio.clinic.dto.response.StaffResponse;
import com.ultimindstudio.clinic.service.StaffService;
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
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
@Tag(name = "Staff")
public class StaffController {

    private final StaffService staffService;

    @GetMapping
    public ResponseEntity<Page<StaffResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(staffService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffResponse> create(@Valid @RequestBody StaffRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(staffService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffResponse> update(@PathVariable Long id, @Valid @RequestBody StaffRequest request) {
        return ResponseEntity.ok(staffService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        staffService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
