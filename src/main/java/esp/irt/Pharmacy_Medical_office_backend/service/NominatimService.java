package esp.irt.Pharmacy_Medical_office_backend.service;

import esp.irt.Pharmacy_Medical_office_backend.dto.GeocodeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class NominatimService {

    @Value("${nominatim.base.url}")
    private String baseUrl;

    @Value("${app.contact.email}")
    private String contactEmail;

    private final RestTemplate http = new RestTemplate();

    // Adresse -> lat/lng
    public GeocodeResult geocode(String address) {
        var uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/search")
                .queryParam("q", address)
                .queryParam("format", "jsonv2")
                .queryParam("addressdetails", 1)
                .queryParam("limit", 1)
                .queryParam("countrycodes", "fr,mr")
                .encode()                  // <= important
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "PharmaMedOfficeApp/1.0 (" + contactEmail + ")");
        headers.set("Accept", "application/json");
        // headers.set("Referer", "http://localhost:8080"); // optionnel

        ResponseEntity<List> resp = http.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), List.class);
        List body = resp.getBody();
        if (body == null || body.isEmpty())
            throw new RuntimeException("No geocoding results for: " + address);

        Map r = (Map) body.get(0);
        String displayName = (String) r.get("display_name");
        double lat = Double.parseDouble(String.valueOf(r.get("lat")));
        double lon = Double.parseDouble(String.valueOf(r.get("lon")));
        return new GeocodeResult(displayName, lat, lon, null, null);
    }


    // lat/lng -> Adresse (la méthode que tu mentionnes)
    public GeocodeResult reverse(double lat, double lon) {
        var uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/reverse")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("format", "jsonv2")
                .build(true).toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "YourApp/1.0 (" + contactEmail + ")");
        ResponseEntity<Map> resp = http.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        Map b = resp.getBody();
        if (b == null) throw new RuntimeException("No reverse result");

        String displayName = (String) b.get("display_name");
        double outLat = Double.parseDouble((String) b.get("lat"));
        double outLon = Double.parseDouble((String) b.get("lon"));
        String osmId = String.valueOf(b.get("osm_id"));
        String osmType = (String) b.get("osm_type");

        return new GeocodeResult(displayName, outLat, outLon, osmId, osmType);
    }
}
