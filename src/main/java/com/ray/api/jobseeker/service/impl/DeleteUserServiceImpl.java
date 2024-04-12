package com.ray.api.jobseeker.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ray.api.jobseeker.model.dto.DeleteUserDTO;
import com.ray.api.jobseeker.model.entity.User;
import com.ray.api.jobseeker.repository.UserRepository;
import com.ray.api.jobseeker.service.DeleteUserService;
import com.ray.api.jobseeker.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class DeleteUserServiceImpl implements DeleteUserService {

    private final ObjectMapper objectMapper = ObjectMapperUtil.getObjectMapper();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public DeleteUserDTO.Response deleteUser(DeleteUserDTO.Request request) {
        DeleteUserDTO.Response response = DeleteUserDTO.Response.builder().build();

        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("username").is(request.getUsername()));
            User user = mongoTemplate.findOne(query, User.class);
            if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                userRepository.deleteById(user.getId());
                response.setError(Boolean.FALSE);
                response.setMessage("Successfully deleted user.");
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DeleteUserDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Failed to delete user.")
                .build();
    }
}