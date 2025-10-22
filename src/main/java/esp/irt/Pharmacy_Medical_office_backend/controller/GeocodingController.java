package esp.irt.Pharmacy_Medical_office_backend.controller;

import esp.irt.Pharmacy_Medical_office_backend.dto.GeocodeResult;
import esp.irt.Pharmacy_Medical_office_backend.service.NominatimService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/geocode")
@RequiredArgsConstructor
public class GeocodingController {

    private final NominatimService nominatim;

    @GetMapping("/search")
    public GeocodeResult search(@RequestParam String q) {
        return nominatim.geocode(q);
    }

    @GetMapping("/reverse")
    public GeocodeResult reverse(@RequestParam double lat, @RequestParam double lon) {
        return nominatim.reverse(lat, lon);
    }
}
