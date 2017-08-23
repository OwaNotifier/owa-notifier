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
	
	// The folder
	private Folder inbox;
	// The type of event
	private int eventType=TYPE_MANY_NEW_MSG;
	// Event data
	private Message message;
	
	/**
	 * Create a new event with only text
	 * @param inbox
	 * 		  The inbox object associated to event
	 * @param eventType
	 * 		  The type of event object associated to event
	 * @param eventText
	 * 		  Short description of event
	 */
	public InboxChangeEvent(Folder inbox, int eventType) {
		this.inbox = inbox;
		this.eventType = eventType;
	}
	
	/**
	 * Create a new event with a new message
	 * @param inbox
	 * 		  The inbox object associated to event
	 * @param message
	 * 		  The message object associated to event
	 */
	public InboxChangeEvent(Folder inbox, Message message) {
		this(inbox, TYPE_ONE_NEW_MSG);
		this.message = message;
	}

	/**
	 * Return the title of new event depending of eventType
	 * @return
	 * 		The title of Event to display
	 */
	public String getEventTitle() {
		if(this.eventType == TYPE_MANY_NEW_MSG) {
			return "Nouveaux Messages";
		}
		if(this.eventType == TYPE_ONE_NEW_MSG) {
			return this.message.getSubject();
		}
		if(this.eventType == TYPE_LESS_NEW_MSG) {
			if(this.getUnreadItemCount() > 0) {
				return this.getUnreadItemCount() + " message(s) non lu";
			} else {
				return "Pas de message non lu";
			}
		}
		return "";
	}


	/**
	 * Return inbox associated to event
	 * @return
	 * 		  The inbox object associated to event
	 */
	public Folder getInbox() {
		return inbox;
	}

	/**
	 * Set inbox on event
	 * @param inbox
	 * 		  The inbox object associated to event
	 */
	public void setInbox(Folder inbox) {
		this.inbox = inbox;
	}
	
	/**
	 * Return mail message
	 * @return
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * Update mail message
	 * @param message
	 */
	public void setMessage(Message message) {
		this.message = message;
	}
	
	/**
	 * Return the event type
	 * @return
	 * 		integer to identify event type
	 */
	public int getEventType() {
		return eventType;
	}

	/**
	 * Set the event type
	 * @param eventType
	 */
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	/**
	 * Return the text associated to event
	 * @return
	 * 		the text
	 */
	public String getEventText() {
		if(this.eventType == TYPE_MANY_NEW_MSG) {
			return this.getUnreadItemCount() + " message(s) non lu";
		}
		if(this.eventType == TYPE_ONE_NEW_MSG) {
			return this.message.getBodyPreview();
		}
		if(this.eventType == TYPE_LESS_NEW_MSG) {
			if(this.getUnreadItemCount() > 0) {
				return this.getUnreadItemCount() + " message(s) non lu";
			} else {
				return "Pas de message non lu";
			}
		}
		return "";
	}

	/**
	 * Get from field
	 * @return
	 * 		a string to identify from of mail
	 */
	public String getEventFrom() {
		if(message != null) {
			EmailAddress addr = message.getFrom().getEmailAddress();
			return addr.getName() + " <" + addr.getAddress() + ">";
		}
		return null;
	}

	/**
	 * Return the unread message count
	 * @return
	 */
	public int getUnreadItemCount() {
		return inbox.getUnreadItemCount();
	}
}
