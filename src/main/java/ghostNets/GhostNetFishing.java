package ghostNets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

@Named
@ApplicationScoped
public class GhostNetFishing implements Serializable {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("ghostNetFishing");

	/** Standardkonstruktor */
	public GhostNetFishing() {
	}

	public List<GhostNet> holeGeisternetze() {
		List<GhostNet> netze = new ArrayList<>();
		try {
			EntityManager em = emf.createEntityManager();
			Query q = em.createQuery("select g from GhostNet g");
			netze = q.getResultList();
		} catch (final Exception ex) {
			System.out.println("Beim Laden der Geisternetze ist ein unerwarteter Fehler aufgetreten");
		}
		return netze;
	}

	public List<Person> holePersonen() {
		List<Person> personen = new ArrayList<>();
		try {
			EntityManager em = emf.createEntityManager();
			Query q = em.createQuery("select p from Person p");
			personen = q.getResultList();
		} catch (final Exception ex) {
			System.out.println("Beim Laden der Personen ist ein unerwarteter Fehler aufgetreten");
		}
		return personen;
	}

	/**
	 * Nach der Anmeldung wollen wir alle Infos zum Benutzer holen
	 * 
	 * @param benutzer Der Benutzer, von dem wir die Daten haben wollen (Zu diesem
	 *                 Zeitpunkt nur mit Name und Passwort gefüllt)
	 * @return {@link Person} mit allen geladenen Infos
	 */
	public Person holeAlleDatenZurPerson(final Person benutzer) {
		Person personMitDaten = new Person();
		if (benutzer != null) {
			try {
				EntityManager em = emf.createEntityManager();
				Query q = em.createQuery("select p from Person p where p.name= :name and p.passwort= :pw");
				q.setParameter("name", benutzer.getName());
				q.setParameter("pw", benutzer.getPasswort());
				personMitDaten = (Person) q.getSingleResult();
			} catch (final Exception ex) {
				System.out.println("Fehler beim Laden aller Informationen zum Benutzer");
			}
		}
		return personMitDaten;
	}

	/**
	 * Persistiert das übergebene Netz
	 * 
	 * @param newGhostNet Das Geisternetz, das ich bergen möchte
	 */
	public void storeNewGhostNet(GhostNet newGhostNet) {
		EntityManager em = emf.createEntityManager();
		try {
			EntityTransaction transaction = em.getTransaction();
			// Beginne die neue Transaktion, speichere das neue Netz und committe
			transaction.begin();
			em.persist(newGhostNet);
			transaction.commit();
		} catch (final Exception ex) {
			System.out.println("Beim Anlegen des neuen Netzes ist ein unerwarteter Fehler aufgetreten");
		} finally {
			em.close();
		}
	}

	/**
	 * Updatet die bergende Person (in den Entitäten {@link GhostNet} und
	 * {@link Person})
	 * 
	 * @param benutzer Der Benutzer, der das Netz birgt
	 * @param netz     Das Netz, dass geborgen werden soll
	 */
	public void updateBergendePersonUndNetz(Person benutzer, GhostNet netz) {
		EntityManager em = emf.createEntityManager();
		try {
			EntityTransaction transaction = em.getTransaction();
			// Beginne Transaktion und update das Netz und die Person
			transaction.begin();
			em.merge(benutzer);
			em.merge(netz);
			transaction.commit();
		} catch(final Exception ex) {
			System.out.println("Beim Updaten des zu bergenden Netzes und der bergenden Person ist ein Fehler aufgetreten");
		} finally {
			em.close();
		}
	}
	
	/**
	 * Updatet eine Netz-Entität (Wird gebraucht, wenn der Status auf "GEBROGEN" gesetzt wird)
	 * 
	 * @param netz Das Netz, das persistiert werden soll
	 */
	public void updateNetz(final GhostNet netz) {
		EntityManager em = emf.createEntityManager();
		try {
			EntityTransaction transaction = em.getTransaction();
			// Beginne Transaktion und update das Netz und die Person
			transaction.begin();
			em.merge(netz);
			transaction.commit();
		} catch(final Exception ex) {
			System.out.println("Beim Updaten des zu bergenden Netzes ist ein Fehler aufgetreten");
		} finally {
			em.close();
		}
	}
}
