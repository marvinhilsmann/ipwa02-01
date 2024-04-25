package ghostNets;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;

/**
 * Klasse, welche die Informationen zu meinem angemeldeten Benutzer bereithält
 * 
 * @author Marvin Hilsmann
 */
@SessionScoped
public class UserSession implements Serializable{
	/** aktuell angemeldeter Benutzer */
	private Person benutzer;
	
	public UserSession() {
		this.benutzer = new Person();
	}
	
	public Person getBenutzer() {
		return this.benutzer;
	}
	
	public void setBenutzer(final Person benutzer) {
		this.benutzer = benutzer;
	}
}
