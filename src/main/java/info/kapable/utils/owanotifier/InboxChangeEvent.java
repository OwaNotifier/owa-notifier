/**
The MIT License (MIT)

Copyright (c) 2017 Mathieu GOULIN

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package info.kapable.utils.owanotifier;

import info.kapable.utils.owanotifier.service.EmailAddress;
import info.kapable.utils.owanotifier.service.Folder;
import info.kapable.utils.owanotifier.service.Message;

/**
 * This class store all informations to display notification
 */
public class InboxChangeEvent {
	// Different type of event
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

	public int getUnreadItemCount() {
		return inbox.getUnreadItemCount();
	}
	
}
