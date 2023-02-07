package com.example.Auth1.document;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Document
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class User  implements UserDetails {
    @Id
    private String id;

    @NotNull
    private String username;

    @NotNull
    private String password;
//
    private String firstName;

    private String lastName;

    private Boolean isPrivate = false;

    private String phoneNumber;

    private String email;

    private String dob;

    private List<String> platforms = new ArrayList<>();

    private List<String> Categories;
//

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
