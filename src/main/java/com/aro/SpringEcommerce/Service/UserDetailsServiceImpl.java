package com.aro.SpringEcommerce.Service;

import com.aro.SpringEcommerce.Repos.UserRepo;
import com.aro.SpringEcommerce.Security.CustomUserDetails;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).map(CustomUserDetails::new).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
    }
}
