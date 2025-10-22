package esp.irt.Pharmacy_Medical_office_backend;

import java.util.Collections;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import esp.irt.Pharmacy_Medical_office_backend.security.model.Role;
import esp.irt.Pharmacy_Medical_office_backend.security.model.UserEntity;
import esp.irt.Pharmacy_Medical_office_backend.security.repository.RoleRepository;
import esp.irt.Pharmacy_Medical_office_backend.security.repository.UserRepository;

@Component
public class PharmacyMedicalOfficeBackendCommandLineRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    public PharmacyMedicalOfficeBackendCommandLineRunner(UserRepository userRepository, RoleRepository roleRepository, Environment environment, PasswordEncoder passwordEncoder) {
        super();
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0 && roleRepository.count() == 0) {
            Role adminRole = new Role("ADMIN");
            Role visiterRole = new Role("VISITER");
            
            roleRepository.save(adminRole);
            roleRepository.save(visiterRole);
            
            UserEntity adminUser = new UserEntity();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("1234"));

            UserEntity regularUser = new UserEntity();
            regularUser.setUsername("abdallahi");
            regularUser.setPassword(passwordEncoder.encode("34887831"));
            
            UserEntity visiterUser = new UserEntity();
            visiterUser.setUsername("visitor");
            visiterUser.setPassword(passwordEncoder.encode("8080uiabd"));

            Optional<Role> adminRoleOptional = roleRepository.findById(adminRole.getId());
            Optional<Role> visiterRoleOptional = roleRepository.findById(visiterRole.getId());

            if (adminRoleOptional.isPresent() && visiterRoleOptional.isPresent()) {
                adminUser.setRoles(Collections.singletonList(adminRoleOptional.get()));
                regularUser.setRoles(Collections.singletonList(adminRoleOptional.get()));
                visiterUser.setRoles(Collections.singletonList(visiterRoleOptional.get()));
            }

            userRepository.save(adminUser);
            userRepository.save(regularUser);
            userRepository.save(visiterUser);
        }
    }
}