package Controller;

import java.io.Serializable;
import java.util.List;

import ghostNets.GhostNetFishing;
import ghostNets.Person;
import ghostNets.UserSession;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class LoginController implements Serializable {

	@Inject
	private GhostNetFishing ghostNetFishing;

	@Inject
	private UserSession userSession;

	/** Standardkonstruktor */
	public LoginController() {
	}

	/**
	 * Prüfe auf gültigen Login
	 * 
	 * @return Zeichenkette mit dem Link zur nächsten Seite (entweder der Nutzer
	 *         bleibt auf der login.xhtml, wenn kein gültiger Nutzer angegeben
	 *         wurde, oder er landet auf der Übersichtsseite index.xhtml)
	 * 
	 */
	public String login() {
		// Hole Liste der Nutzer
		List<Person> personList = ghostNetFishing.holePersonen();
		if (personList != null && personList.isEmpty() == false && userSession != null
				&& userSession.getBenutzer() != null) {
			for (final Person person : personList) {
				if (person.equals(userSession.getBenutzer())) {
					this.userSession.setBenutzer(this.ghostNetFishing.holeAlleDatenZurPerson(person));
					return "index.xhtml";
				}
			}
		}
		return "login.xhtml";
	}

	/**
	 * Logt den aktuellen Benutzer aus
	 * 
	 * @return Login-Seite, zu der zurückgeleitet wird
	 */
	public String logout() {
		this.userSession.setBenutzer(new Person());
		return "login.xhtml";
	}

	public Person getBenutzer() {
		return this.userSession.getBenutzer();
	}

	public void setBenutzer(final Person benutzer) {
		this.userSession.setBenutzer(benutzer);
	}
}
