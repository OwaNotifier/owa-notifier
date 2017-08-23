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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Provides the core methods that a Notification needs.
 */
public abstract class Notification {
	private NotificationManager m_manager;
	private List<NotificationListener> m_listeners;

	public Notification() {
		m_listeners = new CopyOnWriteArrayList<NotificationListener>();
	}

	/**
	 * Listens for events on the Notification (e.g., a click).
	 *
	 * @param listener
	 *            the NotificationListener to add
	 */
	public void addNotificationListener(NotificationListener listener) {
		m_listeners.add(listener);
	}

	/**
	 * Removes a listener for events on the Notification (e.g., a click).
	 *
	 * @param listener
	 *            the NotificationListener to remove
	 */
	public void removeNotificationListener(NotificationListener listener) {
		m_listeners.remove(listener);
	}

	/**
	 * @return whether or not this Notification has been added to a NotificationManager
	 */
	public boolean isManaged() {
		return m_manager != null;
	}

	/**
	 * @return the NotificationManager managing this Notification
	 */
	public NotificationManager getNotificationManager() {
		return m_manager;
	}

	protected void setNotificationManager(NotificationManager manager) {
		m_manager = manager;
	}

	/**
	 * Removes the Notification from the Manager. In some cases, this has the same effect as calling hide(); however,
	 * hide() doesn't invoke Manager-related things like fading, etc.
	 */
	public void removeFromManager() {
		m_manager.removeNotification(this);
	}

	protected void fireListeners(String action) {
		for (NotificationListener nl : m_listeners) {
			nl.actionCompleted(this, action);
		}
	}

	public abstract int getX();

	public abstract int getY();

	public abstract void setLocation(int x, int y);

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract void setSize(int width, int height);

	public abstract double getOpacity();

	public abstract void setOpacity(double opacity);

	/**
	 * Reveals the Notification on the desktop.
	 */
	public abstract void show();

	/**
	 * Hides the Notification on the desktop.
	 */
	public abstract void hide();

	/**
	 * @return whether the Notification is being shown
	 */
	public abstract boolean isShown();
}
