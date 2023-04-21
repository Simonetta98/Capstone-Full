package com.Capstone.service;

import com.Capstone.payload.LoginDto;
import com.Capstone.payload.RegisterDto;

public interface AuthService {
    
	String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
    
}
