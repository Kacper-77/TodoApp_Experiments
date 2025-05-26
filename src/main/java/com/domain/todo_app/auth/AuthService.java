package com.domain.todo_app.auth;

import com.domain.todo_app.db.user.User;
import com.domain.todo_app.db.user.UserRepository;
import com.domain.todo_app.dto.AuthResponseDto;
import com.domain.todo_app.dto.LoginRequestDto;
import com.domain.todo_app.dto.UserRequestDto;
import com.domain.todo_app.util.UserMapper;
import jakarta.transaction.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Log log = LogFactory.getLog(AuthService.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserMapper userMapper, UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       JwtService jwtUtil,
                       PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(UserRequestDto dto) {
        User newUser = userMapper.toUserEntity(dto);
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        log.info("Creating user: " + newUser.getUsername());

        return userRepository.save(newUser);
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        String token = jwtUtil.generateToken(dto.getUsername());
        return new AuthResponseDto(token);
    }
}
