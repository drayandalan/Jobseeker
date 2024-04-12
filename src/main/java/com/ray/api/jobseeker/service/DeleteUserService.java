package com.ray.api.jobseeker.service;

import com.ray.api.jobseeker.model.dto.DeleteUserDTO;

public interface DeleteUserService {

    public DeleteUserDTO.Response deleteUser(DeleteUserDTO.Request request);
}
