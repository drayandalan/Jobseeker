package com.ray.api.jobseeker.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ray.api.jobseeker.model.dto.GenerateTokenDTO;
import com.ray.api.jobseeker.repository.UserRepository;
import com.ray.api.jobseeker.service.GenerateTokenService;
import com.ray.api.jobseeker.util.JwtTokenUtil;
import com.ray.api.jobseeker.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class GenerateTokenServiceImpl implements GenerateTokenService, UserDetailsService {

    private final ObjectMapper objectMapper = ObjectMapperUtil.getObjectMapper();
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtTokenUtil jwtTokenUtil;
    @Autowired private MongoTemplate mongoTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        com.ray.api.jobseeker.model.entity.User user = mongoTemplate.findOne(query, com.ray.api.jobseeker.model.entity.User.class);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    @Override
    @Transactional
    public GenerateTokenDTO.Response generateToken(GenerateTokenDTO.Request request) {
        GenerateTokenDTO.Response response = GenerateTokenDTO.Response.builder().build();
        UserDetails userDetails = null;

        try {
            userDetails = loadUserByUsername(request.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userDetails != null && passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            try {
                String token = jwtTokenUtil.generateToken(request.getUsername());
                log.info("JWT Token: "+token);
                response.setToken(token);
                Query query = new Query();
                query.addCriteria(Criteria.where("username").is(request.getUsername()));
                Update update = new Update();
                update.set("active_token", token);
                if (!update.getUpdateObject().isEmpty()) mongoTemplate.updateFirst(query, update, com.ray.api.jobseeker.model.entity.User.class);

                return response;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return GenerateTokenDTO.Response.builder()
                .errorMessage("Failed to generate token.")
                .build();
    }
}