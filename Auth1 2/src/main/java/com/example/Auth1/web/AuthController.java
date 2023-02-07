package com.example.Auth1.web;

import com.example.Auth1.document.User;
import com.example.Auth1.dto.LoginDTO;
import com.example.Auth1.dto.SignupDTO;
import com.example.Auth1.dto.TokenDTO;
import com.example.Auth1.security.TokenGenerator;
import com.example.Auth1.service.UserManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserDetailsManager userDetailsManager;

    @Autowired
    TokenGenerator tokenGenerator;

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    JwtAuthenticationProvider refreshTokenAuthProvider;

    @Autowired
    UserManager userManager;


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody SignupDTO signupDTO){
        User user = new User();
        if(userDetailsManager.userExists(signupDTO.getUsername())){
            User temp = userManager.loadUserByUsername(signupDTO.getUsername());
            if(temp.getPlatforms().contains(signupDTO.getPlatformId())){
                return ResponseEntity.ok("User Exists");
            }
            temp.getPlatforms().add(signupDTO.getPlatformId());
            BeanUtils.copyProperties(temp,user);
        }else{
            BeanUtils.copyProperties(signupDTO,user);
            List list = user.getPlatforms();
            list.add(signupDTO.getPlatformId());
            user.setPlatforms(list);
        }
        userDetailsManager.createUser(user);
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user,signupDTO.getPassword(), Collections.emptyList());
        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO){
        Authentication authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUsername(),loginDTO.getPassword()));
        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }


    @PostMapping("/token")
    public ResponseEntity token(@RequestBody TokenDTO tokenDTO){
        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));
        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }



}