package org.example.security.service;

import lombok.AllArgsConstructor;
import org.example.security.entities.AppUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = accountService.loadUserByUsername(username);
        if(appUser == null) throw new UsernameNotFoundException("User not found");
        return User.withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .authorities(appUser.getRoles().stream().map(r -> r.getRole()).toArray(String[]::new))
                .build();
    }
}
