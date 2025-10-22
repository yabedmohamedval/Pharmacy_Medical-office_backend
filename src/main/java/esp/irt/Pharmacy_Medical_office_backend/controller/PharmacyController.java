package esp.irt.Pharmacy_Medical_office_backend.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import esp.irt.Pharmacy_Medical_office_backend.model.Pharmacy;
import esp.irt.Pharmacy_Medical_office_backend.service.PharmacyService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/pharmacies")
public class PharmacyController {

	@Autowired
	private PharmacyService service;

	@GetMapping
	public List<Pharmacy> getAllPharmacies() {
		return service.getAllPharmacies();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pharmacy> getPharmacyById(@PathVariable Long id) {
		Optional<Pharmacy> pharmacy = service.getPharmacyById(id);
		return pharmacy.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

    @PostMapping
    public ResponseEntity<Pharmacy> createPharmacy(@RequestBody Pharmacy pharmacy) {
        // pharmacy.addressRaw doit être rempli côté front
        Pharmacy saved = service.savePharmacy(pharmacy);
        return ResponseEntity.created(URI.create("/pharmacies/" + saved.getId())).body(saved);
    }
	@PutMapping("/{id}")
    public ResponseEntity<Pharmacy> updatePharmacy(@PathVariable Long id,
                                                   @RequestBody Pharmacy pharmacyDetails) {
        return service.getPharmacyById(id)
                .map(existing -> {
                    // Champs hérités de PlaceBase
                    existing.setName(pharmacyDetails.getName());
                    existing.setAddressRaw(pharmacyDetails.getAddressRaw()); // <— important
                    existing.setWilaya(pharmacyDetails.getWilaya());
                    existing.setMoughataa(pharmacyDetails.getMoughataa());
                    existing.setCommune(pharmacyDetails.getCommune());
                    existing.setOpen_time(pharmacyDetails.getOpen_time());     // si renommé en camelCase

                    // savePharmacy refera le géocodage si addressRaw est renseignée
                    Pharmacy updated = service.savePharmacy(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePharmacy(@PathVariable Long id) {
		if (service.getPharmacyById(id).isPresent()) {
			service.deletePharmacy(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
