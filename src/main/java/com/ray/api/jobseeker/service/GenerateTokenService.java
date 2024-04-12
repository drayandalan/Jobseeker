package com.ray.api.jobseeker.service;

import com.ray.api.jobseeker.model.dto.GenerateTokenDTO;

public interface GenerateTokenService {

    public GenerateTokenDTO.Response generateToken(GenerateTokenDTO.Request request);
}
