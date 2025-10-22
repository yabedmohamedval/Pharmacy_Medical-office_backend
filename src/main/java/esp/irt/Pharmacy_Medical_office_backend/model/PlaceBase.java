package esp.irt.Pharmacy_Medical_office_backend.model;
import jakarta.persistence.*;
import lombok.*;

@MappedSuperclass
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class PlaceBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String name;

    /** Adresse telle que saisie par l’utilisateur */
    @Column(length = 512)
    protected String addressRaw;

    /** Adresse normalisée retournée par Google (ex: "10 Rue X, 35000 Rennes, France") */
    @Column(length = 512)
    protected String addressFormatted;

    /** Identifiant unique Google Places/Geocoding pour cette adresse */
    @Column(length = 128)
    protected String googlePlaceId;

    /** Coordonnées pour la carte/requêtes de proximité */
    protected Double latitude;
    protected Double longitude;

    protected String wilaya;
    protected String moughataa;
    protected String commune;

    protected String open_time;
}
