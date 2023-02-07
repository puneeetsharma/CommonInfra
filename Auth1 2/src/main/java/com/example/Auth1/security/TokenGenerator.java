package com.example.Auth1.security;


import com.example.Auth1.document.User;
import com.example.Auth1.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class TokenGenerator {

    @Autowired
    JwtEncoder accessTokenEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    JwtEncoder refreshTokenEncoder;

    private String createAccessToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("myApp")
                .issuedAt(now)
                .expiresAt(now.plus(30,ChronoUnit.MINUTES))
                .subject(user.getId())
                .build();

        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    private String createRefreshToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("myApp")
                .issuedAt(now)
                .expiresAt(now.plus(30,ChronoUnit.DAYS))
                .subject(user.getId())
                .build();

        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue(); // Refresh Token
    }

    public TokenDTO createToken(Authentication authentication){
        if(!(authentication.getPrincipal() instanceof User)){
            throw new BadCredentialsException(
                    MessageFormat.format("principal {0} is not of User Type" , authentication.getPrincipal().getClass())
            );
        }
        User user = (User) authentication.getPrincipal();

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUserId(user.getId());
        tokenDTO.setAccessToken(createAccessToken(authentication));

        String refreshToken;
        if(authentication.getCredentials() instanceof Jwt){
            Jwt jwt = (Jwt) authentication.getCredentials();
            Instant now = Instant.now();
            Instant expiresAt = jwt.getExpiresAt();
            Duration duration = Duration.between(now,expiresAt);
            long daysUntillExpired = duration.toDays();
            if(daysUntillExpired < 7){
                refreshToken = createRefreshToken(authentication);
            }else{
                refreshToken = jwt.getTokenValue();
            }
        } else {
            refreshToken = createRefreshToken(authentication);
        }
        tokenDTO.setRefreshToken(refreshToken);
        return tokenDTO;
    }


}
