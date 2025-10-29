package esp.irt.Pharmacy_Medical_office_backend.security.controller;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import esp.irt.Pharmacy_Medical_office_backend.security.config.JWTAuthenticationFilter;
import esp.irt.Pharmacy_Medical_office_backend.security.config.JWTGenerator;
import esp.irt.Pharmacy_Medical_office_backend.security.dto.LoginDto;
import esp.irt.Pharmacy_Medical_office_backend.security.dto.RegisterDto;
import esp.irt.Pharmacy_Medical_office_backend.security.model.Role;
import esp.irt.Pharmacy_Medical_office_backend.security.model.UserEntity;
import esp.irt.Pharmacy_Medical_office_backend.security.repository.RoleRepository;
import esp.irt.Pharmacy_Medical_office_backend.security.repository.UserRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;
    private JWTAuthenticationFilter filter;
 

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator, JWTAuthenticationFilter filter) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.filter = filter;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, String> token = jwtGenerator.generateToken(authentication);
        String access = token.get("access-token");
        return new ResponseEntity<>(access, HttpStatus.OK);
    }
    
    
    @GetMapping("/authenticate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void authenticate() {
        System.out.println("**************************");
    	
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode((registerDto.getPassword())));

        Role roles = roleRepository.findByName("VISITOR")
                .orElseThrow(() -> new RuntimeException("Role VISITOR not found"));

        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                registerDto.getUsername(),
                registerDto.getPassword()));
        
        Map<String, String> token = jwtGenerator.generateToken(authentication);
        String access = token.get("access-token");

        return new ResponseEntity<>(access, HttpStatus.OK);
        
    }
    
    @PostMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
    	String token = filter.getJWTFromRequest(request);
    	if(StringUtils.hasText(token) && jwtGenerator.validateToken(token)) {
    		
    	}
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody LoginDto loginDto){
        Optional<UserEntity> userOptional = userRepository.findByUsername(loginDto.getUsername());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
       
        UserEntity user = userOptional.get();
        user.setPassword(passwordEncoder.encode((loginDto.getPassword())));
        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
