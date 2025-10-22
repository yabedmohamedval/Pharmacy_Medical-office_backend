package esp.irt.Pharmacy_Medical_office_backend.service;

import java.util.List;
import java.util.Optional;

import esp.irt.Pharmacy_Medical_office_backend.model.Pharmacy;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import esp.irt.Pharmacy_Medical_office_backend.model.MedicalOffice;
import esp.irt.Pharmacy_Medical_office_backend.repository.MedicalOfficeRepository;

@Service
public class MedicalOfficeService {
    
    @Autowired
    private MedicalOfficeRepository repository;
    @Autowired
    private NominatimService nominatimService;

    public List<MedicalOffice> getAllMedicalOffices() {
        return repository.findAll();
    }

    public Optional<MedicalOffice> getMedicalOfficeById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public MedicalOffice saveMedicalOffice(MedicalOffice medicalOffice) {
        String raw = medicalOffice.getAddressRaw();
        if (raw != null && !raw.isBlank()) {
            try {
                var g = nominatimService.geocode(raw);
                medicalOffice.setAddressFormatted(g.formattedAddress());
                medicalOffice.setLatitude(g.lat());
                medicalOffice.setLongitude(g.lng());

                // On n'utilise pas Google ici -> laisse googlePlaceId à null
                medicalOffice.setGooglePlaceId(null);
            } catch (Exception e) {
                LoggerFactory.getLogger(getClass()).warn("Geocoding failed", e);
            }
        }
        return repository.save(medicalOffice);
    }

    public void deleteMedicalOffice(Long id) {
        repository.deleteById(id);
    }
}
