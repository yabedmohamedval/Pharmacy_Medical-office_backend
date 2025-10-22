package esp.irt.Pharmacy_Medical_office_backend.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import esp.irt.Pharmacy_Medical_office_backend.security.model.UserEntity;

@CrossOrigin(origins = "*")
@RepositoryRestResource
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
    
}
