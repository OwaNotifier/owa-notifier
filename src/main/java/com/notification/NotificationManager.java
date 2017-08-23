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
 */package com.notification;

import info.kapable.utils.owanotifier.utils.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Manages the creation and movement of Notifications. Once a Notification is added, all aspects of it except for click
 * handeling are managed by the NotificationManager. This includes things such as showing and hiding.
 */
public abstract class NotificationManager {
	private List<Notification> m_notifications;

	public NotificationManager() {
		m_notifications = new ArrayList<Notification>();
	}

	/**
	 * @return all the Notifications being managed by the NotificationManager
	 */
	public final List<Notification> getNotifications() {
		return m_notifications;
	}

	/**
	 * Adds a Notification and will also make it visible.
	 *
	 * @param note
	 *            the Notification to be added
	 * @param time
	 *            the amount of time the Notification should display (e.g., Time.seconds(1) will make the Notification
	 *            display for one second).
	 */
	public final void addNotification(Notification note, Time time) {
		if (!m_notifications.contains(note)) {
			note.setNotificationManager(this);
			m_notifications.add(note);
			notificationAdded(note, time);
		}
	}

	/**
	 * Removes a Notification and will also hide it.
	 *
	 * @param note
	 *            the Notification to be removed
	 */
	public final void removeNotification(Notification note) {
		if (m_notifications.contains(note)) {
			m_notifications.remove(note);
			notificationRemoved(note);
			note.setNotificationManager(null);
		}
	}

	protected abstract void notificationAdded(Notification note, Time time);

	protected abstract void notificationRemoved(Notification note);

	protected void scheduleRemoval(Notification note, Time time) {
		if (!time.isInfinite()) {
			java.util.Timer removeTimer = new java.util.Timer();
			removeTimer.schedule(new RemoveTask(note), time.getMilliseconds());
		}
	}

	private class RemoveTask extends TimerTask {
		private Notification m_note;

		public RemoveTask(Notification note) {
			m_note = note;
		}

		@Override
		public void run() {
			NotificationManager.this.removeNotification(m_note);
		}
	}
}
