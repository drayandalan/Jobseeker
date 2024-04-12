package com.ray.api.jobseeker.controller;

import com.ray.api.jobseeker.model.dto.DeleteUserDTO;
import com.ray.api.jobseeker.model.dto.GenerateTokenDTO;
import com.ray.api.jobseeker.model.dto.RegisterDTO;
import com.ray.api.jobseeker.model.dto.UpdateUserDTO;
import com.ray.api.jobseeker.service.DeleteUserService;
import com.ray.api.jobseeker.service.GenerateTokenService;
import com.ray.api.jobseeker.service.RegisterService;
import com.ray.api.jobseeker.service.UpdateUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class AuthController {

    @Autowired private RegisterService registerService;
    @Autowired private GenerateTokenService generateTokenService;
    @Autowired private UpdateUserService updateUserService;
    @Autowired private DeleteUserService deleteUserService;

    @PostMapping("/jobseeker-api/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO.Request request) {
        RegisterDTO.Response response = registerService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/jobseeker-api/generateToken")
    public ResponseEntity<?> generateToken(@RequestBody GenerateTokenDTO.Request request) {
        GenerateTokenDTO.Response response = generateTokenService.generateToken(request);
        if (response.getToken() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/jobseeker-api/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO.Request request) {
        UpdateUserDTO.Response response = updateUserService.updateUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/jobseeker-api/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserDTO.Request request) {
        DeleteUserDTO.Response response = deleteUserService.deleteUser(request);
        return ResponseEntity.ok(response);
    }
}
