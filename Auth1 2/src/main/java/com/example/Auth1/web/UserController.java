package com.example.Auth1.web;


import com.example.Auth1.document.User;
import com.example.Auth1.dto.UserDTO;
import com.example.Auth1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/{id}")
    @PreAuthorize("#user.id == #id")
    public ResponseEntity user(@AuthenticationPrincipal User user, @PathVariable String id){
                return ResponseEntity.ok(UserDTO.from(userRepository.findById(id).orElseThrow(NoSuchElementException::new)));
    }

    @GetMapping("/check/{id}")
    @PreAuthorize("#user.id == #id")
    public ResponseEntity checkUser(@AuthenticationPrincipal User user, @PathVariable String id){
        return ResponseEntity.ok("Success");
    }


}
