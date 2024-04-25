package ghostNets;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Person {

	@Id
	@GeneratedValue
	private int id;

	private String name;

	private String vorname;

	private String telefonnummer;

	private String passwort;

	private boolean melder;

	@OneToMany(mappedBy = "bergendePerson")
	private List<GhostNet> netze;

	/** Standardkonstruktor */
	public Person() {
	}

	/**
	 * Diese (bergende) Person übernimmt die Bergung des Netzes
	 * 
	 * @param netz Dieses Netz wird von mir geborgen
	 */
	public void addGhostNet(final GhostNet netz) {
		if (this.netze == null)
			this.netze = new ArrayList<>();
		if (this.netze.contains(netz) == false)
			this.netze.add(netz);
	}

	/**
	 * Überschriebene Equals-Methode, damit wir beim Login vergleichen können, ob es
	 * einen Nutzer mit gleichem Namen und Passwort gibt
	 * 
	 * @return true, wenn es einen sich hier um den gleichen Nutzer handelt; false
	 *         sonst
	 */
	@Override
	public boolean equals(Object object) {
		if (object instanceof Person) {
			Person person = (Person) object;
			if (person != null && person.getName().equals(this.name) && person.getPasswort().equals(this.passwort))
				return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return vorname + " " + name;
	}

	///////////////////////
	// Getter und Setter //
	///////////////////////
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getTelefonnummer() {
		return telefonnummer;
	}

	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}

	public String getPasswort() {
		return this.passwort;
	}

	public void setPasswort(final String passwort) {
		this.passwort = passwort;
	}

	public boolean isMelder() {
		return this.melder == true;
	}

	public boolean isBerger() {
		return this.melder == false;
	}

	public List<GhostNet> getNetze() {
		return netze;
	}

	public void setNetze(List<GhostNet> netze) {
		this.netze = netze;
	}
}
