package esp.irt.Pharmacy_Medical_office_backend.service;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import esp.irt.Pharmacy_Medical_office_backend.model.Pharmacy;
import esp.irt.Pharmacy_Medical_office_backend.repository.PharmacyRepository;

@Service
public class PharmacyService {

	@Autowired
	private PharmacyRepository repository;
    @Autowired
    private NominatimService nominatimService;

	public List<Pharmacy> getAllPharmacies() {
		return repository.findAll();
	}

	public Optional<Pharmacy> getPharmacyById(Long id) {
		return repository.findById(id);
	}


    @Transactional
    public Pharmacy savePharmacy(Pharmacy pharmacy) {
        String raw = pharmacy.getAddressRaw();
        if (raw != null && !raw.isBlank()) {
            try {
                var g = nominatimService.geocode(raw);
                pharmacy.setAddressFormatted(g.formattedAddress());
                pharmacy.setLatitude(g.lat());
                pharmacy.setLongitude(g.lng());

                // On n'utilise pas Google ici -> laisse googlePlaceId à null
                pharmacy.setGooglePlaceId(null);
            } catch (Exception e) {
                LoggerFactory.getLogger(getClass()).warn("Geocoding failed", e);
            }
        }
        return repository.save(pharmacy);
    }


    public void deletePharmacy(Long id) {
		repository.deleteById(id);
	}
}
