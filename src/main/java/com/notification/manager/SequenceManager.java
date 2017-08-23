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
 */package com.notification.manager;

import info.kapable.utils.owanotifier.utils.Time;

import java.util.ArrayList;
import java.util.List;

import com.notification.Notification;
import com.notification.NotificationFactory.Location;
import com.notification.NotificationListener;
import com.notification.types.WindowNotification;

/**
 * Displays Notifications one after another in a certain location. As soon as the current Notification is hidden, a new
 * one will appear in its place.
 */
public class SequenceManager extends SimpleManager {
	private List<NotificationShowTime> m_sequence;
	private Notification m_currentNotification;

	{
		m_sequence = new ArrayList<NotificationShowTime>();
	}

	public SequenceManager() {
		super();
	}

	public SequenceManager(Location loc) {
		super(loc);
	}

	@Override
	public void notificationAdded(Notification notification, Time time) {
		notification.addNotificationListener(new CloseListener());
		if (m_currentNotification == null) {
			m_currentNotification = notification;
			superAdded(notification, time);
		} else {
			m_sequence.add(new NotificationShowTime(notification, time));
		}
	}

	private void superAdded(Notification notification, Time time) {
		super.notificationAdded(notification, time);
	}

	private class CloseListener implements NotificationListener {
		@Override
		public void actionCompleted(Notification notification, String action) {
			if (action.equals(WindowNotification.HIDDEN)) {
				m_currentNotification.removeNotificationListener(this);
				m_currentNotification = null;
				if (!m_sequence.isEmpty()) {
					NotificationShowTime showing = m_sequence.remove(0);
					m_currentNotification = showing.notification;
					superAdded(showing.notification, showing.time);
				}
			}
		}
	}

	private class NotificationShowTime {
		public Notification notification;
		public Time time;

		public NotificationShowTime(Notification notification, Time time) {
			this.notification = notification;
			this.time = time;
		}
	}
}
