package com.ray.api.jobseeker.service;

import com.ray.api.jobseeker.model.dto.RegisterDTO;

public interface RegisterService {

    public RegisterDTO.Response register(RegisterDTO.Request request);
}
