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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Log log = LogFactory.getLog(AuthService.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserMapper userMapper, UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(UserRequestDto dto) {
        boolean alreadyExist = userRepository.existsByEmail(dto.getEmail())
                .isPresent();
        if (alreadyExist) {
            throw new RuntimeException("Email is already taken.");
        }

        User newUser = userMapper.toUserEntity(dto);
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        log.info("Creating user: " + newUser.getUsername());

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
        return new AuthResponseDto(token);
    }
}
