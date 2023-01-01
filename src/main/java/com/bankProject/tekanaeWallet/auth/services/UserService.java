package com.bankProject.tekanaeWallet.auth.services;

import com.bankProject.tekanaeWallet.auth.dto.LoginDto;
import com.bankProject.tekanaeWallet.auth.dto.AuthResponseDto;
import com.bankProject.tekanaeWallet.auth.dto.RegisterDto;
import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import com.bankProject.tekanaeWallet.exceptions.UserAuthException;
import com.bankProject.tekanaeWallet.exceptions.UserExistsException;
import com.bankProject.tekanaeWallet.role.models.RoleModel;
import com.bankProject.tekanaeWallet.auth.models.UserModel;
import com.bankProject.tekanaeWallet.role.repositories.RoleRepository;
import com.bankProject.tekanaeWallet.auth.repositories.UserRepository;
import com.bankProject.tekanaeWallet.utils.JwtUserDetailService;
import com.bankProject.tekanaeWallet.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponseDto userRegister(RegisterDto registerDto) throws UserExistsException, NotFoundException {
        UserModel findUser = userRepository.findByEmail(registerDto.getEmail());
        List<RoleModel> roles = new ArrayList<>();
        if (findUser != null) {
            throw new UserExistsException("User already exists");
        }
        RoleModel userRole = roleRepository.findByName("USER");
        if(userRole == null) {
            throw new NotFoundException("Role not found");
        }
        roles.add(userRole);

        UserModel user = UserModel.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .contactNumber(registerDto.getContactNumber())
                .roles(roles)
                .build();

        UserModel savedUser = userRepository.save(user);
        return createJwt("User Registered Successfully", savedUser);
    }

    public AuthResponseDto createJwt(String message, UserModel user) {
        String userEmail = user.getEmail();
        UserDetails userDetails = jwtUserDetailService.loadUserByUsername(userEmail);
        String token = jwtUtil.generateToken(userDetails);
        return new AuthResponseDto(message, token);

    }
    public AuthResponseDto userLogin(LoginDto loginDto) throws NotFoundException, UserAuthException {
        String userEmail = loginDto.getEmail();
        String password = loginDto.getPassword();
        UserModel findUser = userRepository.findByEmail(userEmail);
        if(findUser != null) {
            boolean passwordVerification = passwordEncoder.matches(password, findUser.getPassword());
            if(passwordVerification) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, password));
                return createJwt("User Logged in Successfully", findUser);
            } else {
                throw new UserAuthException("Invalid Credential, Try again");
            }
        }else {
            throw new UserAuthException("Invalid Credential, Try again");
        }
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }
}