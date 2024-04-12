package com.ray.api.jobseeker.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ray.api.jobseeker.model.dto.UpdateUserDTO;
import com.ray.api.jobseeker.model.entity.User;
import com.ray.api.jobseeker.repository.UserRepository;
import com.ray.api.jobseeker.service.UpdateUserService;
import com.ray.api.jobseeker.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class UpdateUserServiceImpl implements UpdateUserService {

    private final ObjectMapper objectMapper = ObjectMapperUtil.getObjectMapper();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public UpdateUserDTO.Response updateUser(UpdateUserDTO.Request request) {
        UpdateUserDTO.Response response = UpdateUserDTO.Response.builder().build();

        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("username").is(request.getUsername()));
            User user = mongoTemplate.findOne(query, User.class);
            if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String message = "";
                Query queryUpdate = new Query();
                queryUpdate.addCriteria(Criteria.where("username").is(request.getUsername()));
                if (request.getNewPassword() != null && request.getNewEmail() != null) {
                    log.info("Update password & email");
                    Update update = new Update();
                    update.set("password", passwordEncoder.encode(request.getNewPassword()));
                    update.set("email", request.getNewEmail());
                    if (!update.getUpdateObject().isEmpty()) mongoTemplate.updateFirst(query, update, User.class);
                    message = "Successfully updated password & email.";
                }
                if (request.getNewEmail() == null) {
                    log.info("Update password");
                    Update update = new Update();
                    update.set("password", passwordEncoder.encode(request.getNewPassword()));
                    if (!update.getUpdateObject().isEmpty()) mongoTemplate.updateFirst(query, update, User.class);
                    message = "Successfully updated password.";
                }
                if (request.getNewPassword() == null) {
                    log.info("update email");
                    Update update = new Update();
                    update.set("email", request.getNewEmail());
                    if (!update.getUpdateObject().isEmpty()) mongoTemplate.updateFirst(query, update, User.class);
                    message = "Successfully updated email.";
                }
                response.setError(Boolean.FALSE);
                response.setMessage(message);
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UpdateUserDTO.Response.builder()
                .error(Boolean.TRUE)
                .message("Failed to update data user.")
                .build();
    }
}