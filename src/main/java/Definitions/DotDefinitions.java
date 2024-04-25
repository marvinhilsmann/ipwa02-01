package Definitions;

import ghostNets.NetzStatus;

public class DotDefinitions {

	static final String GEMELDET = "https://maps.google.com/mapfiles/ms/micons/red-dot.png";
	static final String BERGUNG_BEVORSTEHEND = "https://maps.google.com/mapfiles/ms/micons/yellow-dot.png";
	static final String GEBORGEN = "https://maps.google.com/mapfiles/ms/micons/green-dot.png";
	static final String VERSCHOLLEN = "https://maps.google.com/mapfiles/ms/micons/blue-dot.png";
	
	public static String getDotForStatus(final NetzStatus status) {
		switch (status) {
		case GEMELDET:
			return GEMELDET;
		case BERGUNG_BEVORSTEHEND:
			return BERGUNG_BEVORSTEHEND;
		case GEBORGEN:
			return GEBORGEN;
		case VERSCHOLLEN:
			return VERSCHOLLEN;
		default: return "";
		}
	}
}
