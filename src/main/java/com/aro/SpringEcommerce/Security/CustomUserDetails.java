package com.aro.SpringEcommerce.Security;

import com.aro.SpringEcommerce.Entity.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private final AppUser user;

    public CustomUserDetails(AppUser user) {
        this.user = user;
    }

    public Integer getUserId() {
        return user.getUserId();
    }

    public String getRealUsername() { return user.getName(); }

    public AppUser getUser() {
        return this.user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getUserRoles().stream()
            .map((role) -> new SimpleGrantedAuthority(role.getName().name()))
            .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomUserDetails{");
        sb.append("user=").append(user);
        sb.append('}');
        return sb.toString();
    }
}