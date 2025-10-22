// RoleController.java
package esp.irt.Pharmacy_Medical_office_backend.security.controller;

import esp.irt.Pharmacy_Medical_office_backend.security.model.Role;
import esp.irt.Pharmacy_Medical_office_backend.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role newRole = roleRepository.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        if (!roleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        role.setId(id);
        Role updatedRole = roleRepository.save(role);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        if (!roleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        roleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
