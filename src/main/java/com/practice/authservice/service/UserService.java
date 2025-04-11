package com.practice.authservice.service;

public interface UserService {

    void register(String username, String email, String password);

    void authenticate(String username, String password);

}
