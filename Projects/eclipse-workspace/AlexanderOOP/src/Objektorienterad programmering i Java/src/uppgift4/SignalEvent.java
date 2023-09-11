package uppgift4;

import javafx.event.Event;
import javafx.event.EventType;


public class SignalEvent extends Event
{
	private static final long serialVersionUID = 7821725538814468480L;

	public SignalEvent(EventType<? extends Event> eventType)
	{
		super(eventType);
	}

}
