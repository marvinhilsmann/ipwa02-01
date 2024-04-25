package ghostNets;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class GhostNet {
	
	// ID-Attribut zur Identifizierung
	@Id
	@GeneratedValue
	private Long id;
	// Koordinaten der Netze
	private BigDecimal latitudinal;
	private BigDecimal longitudinal;
	// ungefaehre Groesse
	private BigDecimal groesse;
	// Bergende Person
	@ManyToOne
	@JoinColumn(name = "person_id")
	private Person bergendePerson;
	
	private NetzStatus status;
	
	/** Standardkonstruktor */
	public GhostNet() {}

	/** Konstruktor zum Erstellen eines neuen Geisternetzes */
	public GhostNet(final BigDecimal latitudinal, final BigDecimal longitudinal, final BigDecimal groesse) {
		this.latitudinal=latitudinal;
		this.longitudinal=longitudinal;
		this.groesse=groesse;
	}

	
	///////////////////////
	// Getter und Setter //
	///////////////////////
	public BigDecimal getLatitudinal() {
		return latitudinal;
	}
	
	public void setLatitudinal(final BigDecimal latitudinal) {
		this.latitudinal = latitudinal;
	}
	
	public BigDecimal getLongitudinal() {
		return longitudinal;
	}
	
	public void setLongitudinal(final BigDecimal longitudinal) {
		this.longitudinal = longitudinal;
	}
	
	public BigDecimal getGroesse() {
		return groesse;
	}
	
	public void setGroesse(final BigDecimal groesse) {
		this.groesse = groesse;
	}
	
	public Person getBergendePerson() {
		return bergendePerson;
	}
	
	public void setBergendePerson(final Person bergendePerson) {
		this.bergendePerson = bergendePerson;
	}
	
	public NetzStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(final NetzStatus status) {
		this.status = status;
	}
	
}
