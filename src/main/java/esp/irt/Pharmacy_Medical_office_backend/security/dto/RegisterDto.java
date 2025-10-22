package esp.irt.Pharmacy_Medical_office_backend.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class RegisterDto {
    private String username;
    private String password;
}