package esp.irt.Pharmacy_Medical_office_backend.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import esp.irt.Pharmacy_Medical_office_backend.security.model.Role;


@RepositoryRestResource
@CrossOrigin(origins = "*")
public interface RoleRepository extends JpaRepository<Role, Long>{
	Optional<Role> findByName(String name);
}
