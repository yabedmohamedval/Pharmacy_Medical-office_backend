package esp.irt.Pharmacy_Medical_office_backend.dto;

public record GeocodeResult(
        String formattedAddress,
        double lat,
        double lng,
        String osmId,
        String osmType
) {}