package com.ultimindstudio.clinic.config;

import com.ultimindstudio.clinic.entity.Login;
import com.ultimindstudio.clinic.entity.TypeApp;
import com.ultimindstudio.clinic.repository.LoginRepository;
import com.ultimindstudio.clinic.repository.TypeAppRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final LoginRepository loginRepository;
    private final TypeAppRepository typeAppRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (loginRepository.count() > 0) return;

        TypeApp adminType  = typeAppRepository.findById(1L).orElse(null);
        TypeApp doctorType = typeAppRepository.findById(2L).orElse(null);

        if (adminType != null) {
            loginRepository.save(Login.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .typeApp(adminType)
                    .build());
            log.info("Seeded user: admin / admin123 (ADMIN)");
        }

        if (doctorType != null) {
            loginRepository.save(Login.builder()
                    .username("doctor1")
                    .password(passwordEncoder.encode("doctor123"))
                    .typeApp(doctorType)
                    .build());
            log.info("Seeded user: doctor1 / doctor123 (DOCTOR)");
        }
    }
}
