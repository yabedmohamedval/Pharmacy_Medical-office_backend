package esp.irt.Pharmacy_Medical_office_backend.controller;
import esp.irt.Pharmacy_Medical_office_backend.dto.StatsSummary;
import esp.irt.Pharmacy_Medical_office_backend.repository.PharmacyRepository;
import esp.irt.Pharmacy_Medical_office_backend.repository.MedicalOfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    private final PharmacyRepository pharmacyRepo;
    private final MedicalOfficeRepository medicalOfficeRepo;

    @GetMapping("/summary")
    public StatsSummary summary(){
        long p = pharmacyRepo.count();
        long m = medicalOfficeRepo.count();
        return new StatsSummary(p, m);
    }
}
