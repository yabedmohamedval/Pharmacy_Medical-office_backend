// UserController.java
package esp.irt.Pharmacy_Medical_office_backend.security.controller;

import esp.irt.Pharmacy_Medical_office_backend.security.dto.UserDto;
import esp.irt.Pharmacy_Medical_office_backend.security.model.Role;
import esp.irt.Pharmacy_Medical_office_backend.security.model.UserEntity;
import esp.irt.Pharmacy_Medical_office_backend.security.repository.RoleRepository;
import esp.irt.Pharmacy_Medical_office_backend.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        List<UserEntity> usersWithoutPassword = users.stream()
                .map(user -> {
                    user.setPassword(null);
                    return user;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(usersWithoutPassword);
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserDto user) {
        UserEntity newUser = new UserEntity();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role=roleRepository.findByName(user.getRole()).get();
        newUser.setRoles(Collections.singletonList(role));
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(newUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        UserEntity updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/resetpassword")
    public ResponseEntity<Void> resetPassword(@RequestBody Long id) {
        UserEntity user=userRepository.findById(id).get();
        user.setPassword(passwordEncoder.encode("1234"));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }


}
