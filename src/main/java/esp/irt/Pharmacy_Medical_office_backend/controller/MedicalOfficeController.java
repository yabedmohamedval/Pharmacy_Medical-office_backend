package esp.irt.Pharmacy_Medical_office_backend.controller;

import java.util.List;
import java.util.Optional;

import esp.irt.Pharmacy_Medical_office_backend.model.Pharmacy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import esp.irt.Pharmacy_Medical_office_backend.model.MedicalOffice;
import esp.irt.Pharmacy_Medical_office_backend.service.MedicalOfficeService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/medicalOffices")
public class MedicalOfficeController {

    @Autowired
    private MedicalOfficeService service;

    @GetMapping
    public List<MedicalOffice> getAllMedicalOffices() {
        return service.getAllMedicalOffices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalOffice> getMedicalOfficeById(@PathVariable Long id) {
        Optional<MedicalOffice> medicalOffice = service.getMedicalOfficeById(id);
        return medicalOffice.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public MedicalOffice createMedicalOffice(@RequestBody MedicalOffice medicalOffice) {
        return service.saveMedicalOffice(medicalOffice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalOffice> updateMedicalOffice(@PathVariable Long id, @RequestBody MedicalOffice medicalOfficeDetails) {
        return service.getMedicalOfficeById(id)
                .map(existing -> {
                    // Champs hérités de PlaceBase
                    existing.setName(medicalOfficeDetails.getName());
                    existing.setAddressRaw(medicalOfficeDetails.getAddressRaw()); // <— important
                    existing.setWilaya(medicalOfficeDetails.getWilaya());
                    existing.setMoughataa(medicalOfficeDetails.getMoughataa());
                    existing.setCommune(medicalOfficeDetails.getCommune());
                    existing.setOpen_time(medicalOfficeDetails.getOpen_time());     // si renommé en camelCase
                    existing.setSpeciality(medicalOfficeDetails.getSpeciality());

                    // savePharmacy refera le géocodage si addressRaw est renseignée
                    MedicalOffice updated = service.saveMedicalOffice(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalOffice(@PathVariable Long id) {
        if (service.getMedicalOfficeById(id).isPresent()) {
            service.deleteMedicalOffice(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
