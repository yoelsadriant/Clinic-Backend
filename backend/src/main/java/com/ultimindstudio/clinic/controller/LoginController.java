package com.ultimindstudio.clinic.controller;

import com.ultimindstudio.clinic.dto.LookupResponse;
import com.ultimindstudio.clinic.repository.LoginRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users")
@PreAuthorize("hasRole('ADMIN')")
public class LoginController {

    private final LoginRepository loginRepository;

    @GetMapping
    public ResponseEntity<Page<LookupResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(
                loginRepository.findAll(pageable).map(l -> new LookupResponse(l.getId(), l.getUsername()))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        loginRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
