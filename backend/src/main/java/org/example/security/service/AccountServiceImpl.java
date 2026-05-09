package org.example.security.service;

import lombok.AllArgsConstructor;
import org.example.security.entities.AppRole;
import org.example.security.entities.AppUser;
import org.example.security.repositories.AppRoleRepository;
import org.example.security.repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser addNewUser(String username, String password, String email, String confirmPassword) {
        if(!password.equals(confirmPassword)) throw new RuntimeException("Passwords do not match");
        AppUser appUser = AppUser.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();
        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(String role) {
        AppRole appRole = AppRole.builder().role(role).build();
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String role) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).get();
        appUser.getRoles().add(appRole);
    }

    @Override
    public void removeRoleFromUser(String username, String role) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).get();
        appUser.getRoles().remove(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword, String confirmPassword) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if(!passwordEncoder.matches(oldPassword, appUser.getPassword())) throw new RuntimeException("Old password incorrect");
        if(!newPassword.equals(confirmPassword)) throw new RuntimeException("Passwords do not match");
        appUser.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(appUser);
    }
}
