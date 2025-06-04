package com.domain.todo_app.service;

import com.domain.todo_app.db.token.RefreshToken;
import com.domain.todo_app.db.user.User;
import com.domain.todo_app.db.user.UserRepository;
import com.domain.todo_app.dto.AuthResponseDto;
import com.domain.todo_app.dto.LoginRequestDto;
import com.domain.todo_app.dto.UserRequestDto;
import com.domain.todo_app.util.UserMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private static final Log log = LogFactory.getLog(AuthService.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserMapper userMapper, UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder,
                       RefreshTokenService refreshTokenService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public User registerUser(UserRequestDto dto) throws IllegalAccessException {
        boolean isInDB = userRepository.findByEmail(dto.getEmail())
                .isPresent();

        if (isInDB) {
            throw new IllegalAccessException("Email alredy exists.");
        }
        User newUser = userMapper.toUserEntity(dto);
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(newUser);
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername(user.getUsername());
        userRequestDto.setEmail(user.getEmail());
        userRequestDto.setAge(user.getAge());

        String token = jwtService.generateToken(userRequestDto);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new AuthResponseDto(token, refreshToken.getToken());
    }
}
