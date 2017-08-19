package info.kapable.utils.owanotifier;

import info.kapable.utils.owanotifier.service.EmailAddress;
import info.kapable.utils.owanotifier.service.Folder;
import info.kapable.utils.owanotifier.service.Message;

public class InboxChangeEvent {
	public static final int TYPE_MANY_NEW_MSG = 0;
	public static final int TYPE_ONE_NEW_MSG = 2;
	public static final int TYPE_LESS_NEW_MSG = 3;
	
	private Folder inbox;
	private int eventType=0;
	private String eventFrom;
	private String eventText;
	private Message message;
	
	public InboxChangeEvent(Folder inbox, int eventType, String eventText) {
		this.inbox = inbox;
		this.eventType = eventType;
		this.eventText = eventText;
	}
	
	public InboxChangeEvent(Folder inbox, Message message) {
		this(inbox, TYPE_ONE_NEW_MSG, message.getBodyPreview());
		this.message = message;
		EmailAddress addr = message.getFrom().getEmailAddress();
		this.eventFrom = addr.getName() + " <" + addr.getAddress() + ">";
	}

	public String getEventTitle() {
		if(this.eventType == TYPE_MANY_NEW_MSG) {
			return "Nouveaux Messages";
		}
		if(this.eventType == TYPE_ONE_NEW_MSG) {
			return this.message.getSubject();
		}
		if(this.eventType == TYPE_LESS_NEW_MSG) {
			return "Nouveaux Messages";
		}
		return "";
	}


	public Folder getInbox() {
		return inbox;
	}


	public void setInbox(Folder inbox) {
		this.inbox = inbox;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public String getEventText() {
		return eventText;
	}

	public void setEventText(String eventText) {
		this.eventText = eventText;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getEventFrom() {
		return eventFrom;
	}

	public void setEventFrom(String eventFrom) {
		this.eventFrom = eventFrom;
	}
	
}
