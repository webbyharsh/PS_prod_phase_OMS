package com.ps.oms.auth.services;

import com.ps.oms.auth.entities.User;

public interface IAuthService {
    User findOne(String username);
}