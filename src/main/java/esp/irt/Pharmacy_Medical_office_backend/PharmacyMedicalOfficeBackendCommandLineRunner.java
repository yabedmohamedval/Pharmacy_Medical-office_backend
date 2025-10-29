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
            Role visitorRole = new Role("VISITOR");
            
            roleRepository.save(adminRole);
            roleRepository.save(visitorRole);
            
            UserEntity adminUser = new UserEntity();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("1234"));

            UserEntity visitorUser = new UserEntity();
            visitorUser.setUsername("visitor");
            visitorUser.setPassword(passwordEncoder.encode("1234"));

            Optional<Role> adminRoleOptional = roleRepository.findById(adminRole.getId());
            Optional<Role> visitorRoleOptional = roleRepository.findById(visitorRole.getId());

            if (adminRoleOptional.isPresent() && visitorRoleOptional.isPresent()) {
                adminUser.setRoles(Collections.singletonList(adminRoleOptional.get()));
                visitorUser.setRoles(Collections.singletonList(visitorRoleOptional.get()));
            }

            userRepository.save(adminUser);
            userRepository.save(visitorUser);
        }
    }
}