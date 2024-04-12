package com.ray.api.jobseeker.service;

import com.ray.api.jobseeker.model.dto.UpdateUserDTO;

public interface UpdateUserService {

    public UpdateUserDTO.Response updateUser(UpdateUserDTO.Request request);
}
