package esp.irt.Pharmacy_Medical_office_backend.security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity @Data @NoArgsConstructor
@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Role {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    public Role(String string) {
		name = string;
	}
}