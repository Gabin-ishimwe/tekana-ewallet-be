package com.bankProject.tekanaeWallet.auth.services;

import com.bankProject.tekanaeWallet.account.entity.Account;
import com.bankProject.tekanaeWallet.account.repositories.AccountRepository;
import com.bankProject.tekanaeWallet.auth.dto.LoginDto;
import com.bankProject.tekanaeWallet.auth.dto.AuthResponseDto;
import com.bankProject.tekanaeWallet.auth.dto.RegisterDto;
import com.bankProject.tekanaeWallet.exceptions.NotFoundException;
import com.bankProject.tekanaeWallet.exceptions.UserAuthException;
import com.bankProject.tekanaeWallet.exceptions.UserExistsException;
import com.bankProject.tekanaeWallet.role.entity.Role;
import com.bankProject.tekanaeWallet.auth.entity.User;
import com.bankProject.tekanaeWallet.auth.repositories.UserRepository;
import com.bankProject.tekanaeWallet.role.repository.RoleRepository;
import com.bankProject.tekanaeWallet.utils.JwtUserDetailService;
import com.bankProject.tekanaeWallet.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private AccountRepository accountRepository;

    public AuthResponseDto userRegister(RegisterDto registerDto) throws UserExistsException, NotFoundException {
        User findUser = userRepository.findByEmail(registerDto.getEmail());
        List<Role> roles = new ArrayList<>();
        if (findUser != null) {
            throw new UserExistsException("User already exists");
        }
        Role userRole = roleRepository.findByName("USER");
        if(userRole == null) {
            throw new NotFoundException("Role not found");
        }
        roles.add(userRole);

        // create user account
        Account accountUser = Account.builder()
                .account_number(UUID.randomUUID())
                .balance(0L)
                .build();

        Account savedAccount = accountRepository.save(accountUser);
        // create user instance
        User user = User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .contactNumber(registerDto.getContactNumber())
                .roles(roles)
                .account(savedAccount)
                .build();
        // save user
        userRepository.save(user);
        return new AuthResponseDto("User Registered Successfully", null);
    }

    public AuthResponseDto createJwt(String message, User user) {
        String userEmail = user.getEmail();
        UserDetails userDetails = jwtUserDetailService.loadUserByUsername(userEmail);
        String token = jwtUtil.generateToken(userDetails);
        return new AuthResponseDto(message, token);

    }
    public AuthResponseDto userLogin(LoginDto loginDto) throws NotFoundException, UserAuthException {
        String userEmail = loginDto.getEmail();
        String password = loginDto.getPassword();
        User findUser = userRepository.findByEmail(userEmail);
        if(findUser != null) {
            boolean passwordVerification = passwordEncoder.matches(password, findUser.getPassword());
            if(passwordVerification) {
                System.out.println("password matched-----------");
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, password));
                return createJwt("User Logged in Successfully", findUser);
            } else {
                throw new UserAuthException("Invalid Credential, Try again");
            }
        }else {
            throw new UserAuthException("Invalid Credential, Try again");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getOneUser() throws NotFoundException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User findUser = userRepository.findByEmail(authUser.getUsername());
        if(findUser == null) {
            throw new NotFoundException("User not found");
        }
        return findUser;
    }
}
