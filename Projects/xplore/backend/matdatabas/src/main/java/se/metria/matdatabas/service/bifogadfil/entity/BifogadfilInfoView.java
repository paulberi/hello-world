package se.metria.matdatabas.service.bifogadfil.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public interface BifogadfilInfoView {
	
	UUID getId();
	String getFilnamn();
	String getMimeTyp();
	LocalDateTime getSkapadDatum();

}
