package esp.irt.Pharmacy_Medical_office_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import esp.irt.Pharmacy_Medical_office_backend.model.Pharmacy;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
