package esp.irt.Pharmacy_Medical_office_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class MedicalOffice extends PlaceBase {
    private String speciality = "medecine generale";

}
