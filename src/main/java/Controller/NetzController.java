package Controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import Definitions.DotDefinitions;
import Definitions.NetzStatus;
import ghostNets.GhostNet;
import ghostNets.GhostNetFishing;
import ghostNets.Person;
import ghostNets.UserSession;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class NetzController implements Serializable {

	@Inject
	private GhostNetFishing ghostNetFishing;

	@Inject
	private UserSession userSession;

	private BigDecimal newLat = BigDecimal.ZERO;
	private BigDecimal newLong = BigDecimal.ZERO;
	private BigDecimal newGroesse = BigDecimal.ZERO;

	private List<GhostNet> netze = new ArrayList<>();
	// Datenbasis für die Karte mit den Netzen
	private MapModel ghostNetModel;
	// Aktuell gewählter Marker
	private Marker currentMarker;

	/** Standardkonstruktor */
	public NetzController() {
	}

	@PostConstruct
	private void intialize() {
		this.netze = ghostNetFishing.holeGeisternetze();
		createMapWithGhostNets();
	}

	/** Baue das Modell mit den Markern der Karte zusammen */
	private void createMapWithGhostNets() {
		this.ghostNetModel = new DefaultMapModel();
		int i = 1;

		for (final GhostNet netz : this.netze) {
			this.ghostNetModel.addOverlay(
					new Marker(new LatLng(netz.getLatitudinal().doubleValue(), netz.getLongitudinal().doubleValue()),
							"Geisternetz " + i, null, DotDefinitions.getDotForStatus(netz.getStatus())));
			i++;
		}
	}

	/** Listener, der ausgelöst wird, wenn der Nutzer einen Marker anwählt */
	public void onMarkerSelect(OverlaySelectEvent event) {
		this.currentMarker = (Marker) event.getOverlay();
	}

	/** Methode, welche durch die Aktion des Benutzers ausgelöst ein neues Netz erfasst, falls der Benutzer eine meldende Person ist */
	public String createGhostNet() {
		if (userSession != null && userSession.getBenutzer() != null) {
			if (userSession.getBenutzer().isMelder()) {
				// Benutzereingaben als neues Geisternetz erfassen
				GhostNet newGhostNet = new GhostNet(newLat, newLong, newGroesse);
				newGhostNet.setStatus(NetzStatus.GEMELDET);
				// und zum Persistieren an unsere "Session" geben
				this.ghostNetFishing.storeNewGhostNet(newGhostNet);
				// Eingaben zurücksetzen für die nächste Neuerfassung
				newLat = newLong = newGroesse = BigDecimal.ZERO;
			}
		}
		return "index.xhtml"; // Wir geben am Ende die aktuelle Seite zurück
	}

	/**
	 * Das Netz, dass übergeben wird, soll bald geborgen werden
	 * Neuer Status = NetzStatus.BERGUNG_BEVORSTEHEND
	 * 
	 * @param netz Das Netz, dass geborgen werden soll
	 * @return Seite, auf die der Benutzer weitergeleitet werden soll
	 */
	public void bergungAnmelden(final GhostNet netz) {
		if (this.userSession != null && this.userSession.getBenutzer() != null
				&& this.userSession.getBenutzer().isBerger()) {
			// Daten aktualisieren
			this.userSession.getBenutzer().addGhostNet(netz);
			netz.setStatus(NetzStatus.BERGUNG_BEVORSTEHEND);
			netz.setBergendePerson(this.userSession.getBenutzer());
			// persistieren
			this.ghostNetFishing.updateBergendePersonUndNetz(this.userSession.getBenutzer(), netz);
			// Zum Schluss die Marker einmal neu aufbauen (Sonst behalten diese die alte farbliche Markierung)
			refreshMarkers();
		}
	}
	
	/**
	 * Das Netz, dass übergeben wird, ist geborgen
	 * (NetzStatus.GEBORGEN)
	 * 
	 * @param netz Das Netz, dass geborgen wurde
	 * @return Seite, auf die der Benutzer weitergeleitet werden soll
	 */
	public void bergeNetz(final GhostNet netz) {
		if (this.userSession != null && this.userSession.getBenutzer() != null
				&& this.userSession.getBenutzer().isBerger()) {
			// Daten aktualisieren
			netz.setStatus(NetzStatus.GEBORGEN);
			// persistieren
			this.ghostNetFishing.updateNetz(netz);
			// Zum Schluss die Marker einmal neu aufbauen (Sonst behalten diese die alte farbliche Markierung)
			refreshMarkers();
		}
	}
	
	private void refreshMarkers() {
		this.currentMarker=null;
		createMapWithGhostNets();
	}

	///////////////////////
	// Getter und Setter //
	///////////////////////
	public Person getBenutzer() {
		return this.userSession.getBenutzer();
	}

	public List<GhostNet> getNetze() {
		return netze;
	}

	public void setNetze(List<GhostNet> netze) {
		this.netze = netze;
	}

	public MapModel getGhostNetModel() {
		return ghostNetModel;
	}

	public Marker getCurrentMarker() {
		return this.currentMarker;
	}

	public BigDecimal getNewLat() {
		return newLat;
	}

	public void setNewLat(final BigDecimal newLat) {
		this.newLat = newLat;
	}

	public BigDecimal getNewLong() {
		return newLong;
	}

	public void setNewLong(final BigDecimal newLong) {
		this.newLong = newLong;
	}

	public BigDecimal getNewGroesse() {
		return newGroesse;
	}

	public void setNewGroesse(final BigDecimal newGroesse) {
		this.newGroesse = newGroesse;
	}
	
	/** 
	 * Gibt an, ob der Button "Bergung anmelden" geklickt werden darf
	 * 
	 * @param netz Das Netz für das geprüft werden soll, ob der Status noch nach NetzStatus.BERGUNG_BEVORSTEHEND geändert werden kann
	 * @return true, wenn geändert werden kann
	 */
	public boolean isBergenAnmeldenAllowed(final GhostNet netz) {
		if(this.getBenutzer() != null && getBenutzer().isBerger() && netz != null && (netz.getStatus() != NetzStatus.BERGUNG_BEVORSTEHEND && netz.getStatus() != NetzStatus.GEBORGEN && netz.getStatus() != NetzStatus.VERSCHOLLEN)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gibt an, ob der Button "Netz bergen" geklickt werden darf
	 * 
	 * @param netz Das Netz für das geprüft werden soll, ob der Status noch nach NetzStatus.GEBORGEN geändert werden kann
	 * @return truem wenn geändert werden kann (Nutzer ist eine bergende Person und Status ist aktuell NetzStatus.BERGUNG_BEVORSTEHEND); false sonst
	 */
	public boolean isBergenAllowed(final GhostNet netz) {
		if(this.getBenutzer() != null && this.getBenutzer().isBerger() && netz != null && (netz.getStatus() == NetzStatus.BERGUNG_BEVORSTEHEND && netz.getStatus() != NetzStatus.GEBORGEN)) {
			return true;
		}
		return false;
	}
}
