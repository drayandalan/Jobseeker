package com.ray.api.jobseeker.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ray.api.jobseeker.model.dto.RegisterDTO;
import com.ray.api.jobseeker.model.entity.User;
import com.ray.api.jobseeker.repository.UserRepository;
import com.ray.api.jobseeker.service.RegisterService;
import com.ray.api.jobseeker.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {

    private final ObjectMapper objectMapper = ObjectMapperUtil.getObjectMapper();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public RegisterDTO.Response register(RegisterDTO.Request request) {
        RegisterDTO.Response response = RegisterDTO.Response.builder().build();

        if (request.getUsername() == null
                || request.getPassword() == null
                || request.getUsername().isBlank()
                || request.getPassword().isBlank())
            return RegisterDTO.Response.builder()
                    .error(Boolean.TRUE)
                    .message("Username & password tidak boleh kosong.").build();

        try {
            userRepository.save(User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail()).build());
        } catch (Exception e) {
            e.printStackTrace();
            return RegisterDTO.Response.builder()
                    .error(Boolean.TRUE)
                    .message("Registrasi gagal.").build();
        }

        response.setError(Boolean.FALSE);
        response.setMessage("Registarasi berhasil.");

        return response;
    }
}