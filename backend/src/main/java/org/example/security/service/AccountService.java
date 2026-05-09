package org.example.security.service;

import org.example.security.entities.AppRole;
import org.example.security.entities.AppUser;

public interface AccountService {
    AppUser addNewUser(String username, String password, String email, String confirmPassword);
    AppRole addNewRole(String role);
    void addRoleToUser(String username, String role);
    void removeRoleFromUser(String username, String role);
    AppUser loadUserByUsername(String username);
    void changePassword(String username, String oldPassword, String newPassword, String confirmPassword);
}
