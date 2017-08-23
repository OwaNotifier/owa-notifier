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
 */package info.kapable.utils.owanotifier.notification.manager;

import info.kapable.utils.owanotifier.utils.Screen;
import info.kapable.utils.owanotifier.utils.Time;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.notification.Notification;
import com.notification.NotificationFactory.Location;
import com.notification.NotificationManager;

/**
 * Simply displays new Notifications in one corner of the screen on top of each other. Has an option for fading (note -
 * results will vary across different platforms).
 */
public class SimpleManager extends NotificationManager {
	private Location m_loc;
	private Screen m_screen;

	private boolean m_fadeEnabled = false;
	private Time m_fadeTime;

	private FaderRunnable m_fader;
	private Thread m_faderThread;

	private static final int FADE_DELAY = 50; // milliseconds

	{
		m_screen = Screen.standard();
		m_fadeEnabled = false;
		m_fadeTime = Time.seconds(1);
	}

	public SimpleManager() {
		m_loc = Location.NORTHEAST;
	}

	public SimpleManager(Location loc) {
		m_loc = loc;
	}

	/**
	 * @return the time for fading
	 */
	public Time getFadeTime() {
		return m_fadeTime;
	}

	/**
	 * Sets the fade time. To enable, call setFadeEnabled(boolean).
	 *
	 * @param fadeTime
	 *            the duration of the fading
	 */
	public void setFadeTime(Time fadeTime) {
		m_fadeTime = fadeTime;
	}

	/**
	 * @return whether or not fading is enabled
	 */
	public boolean isFadeEnabled() {
		return m_fadeEnabled;
	}

	/**
	 * Sets whether or not fading is enabled.
	 *
	 * @param fadeEnabled
	 *            whether or not fading is enabled
	 */
	public void setFadeEnabled(boolean fadeEnabled) {
		m_fadeEnabled = fadeEnabled;

		if (fadeEnabled) {
			m_fader = new FaderRunnable();
			m_faderThread = new Thread(m_fader);
			m_faderThread.start();
		} else {
			m_fader.stop();
			m_fader = null;
			m_faderThread = null;
		}
	}

	/**
	 * @return the location where the Notifications show up
	 */
	public Location getLocation() {
		return m_loc;
	}

	/**
	 * Sets the location where the Notifications show up.
	 *
	 * @param loc
	 *            the Location to show at
	 */
	public void setLocation(Location loc) {
		m_loc = loc;
	}

	protected Screen getScreen() {
		return m_screen;
	}

	@Override
	protected void notificationAdded(Notification note, Time time) {
		note.setLocation(m_screen.getX(m_loc, note), m_screen.getY(m_loc, note));

		if (isFadeEnabled()) {
			double opacity = note.getOpacity();
			note.setOpacity(0);
			startFade(note, opacity);
			scheduleRemoval(note, time.add(m_fadeTime));
		} else {
			scheduleRemoval(note, time);
		}

		note.show();
	}

	@Override
	protected void notificationRemoved(Notification note) {
		if (isFadeEnabled()) {
			startFade(note, -note.getOpacity());
		} else {
			note.hide();
		}
	}

	private void startFade(Notification note, double deltaOpacity) {
		m_fader.addFader(new Fader(note, getDeltaFade(deltaOpacity), note.getOpacity() + deltaOpacity));
	}

	private double getDeltaFade(double deltaOpacity) {
		return deltaOpacity / m_fadeTime.getMilliseconds();
	}

	private class FaderRunnable implements Runnable {
		private List<Fader> m_faders;
		private boolean m_shouldStop;

		public FaderRunnable() {
			m_faders = new CopyOnWriteArrayList<Fader>();
			m_shouldStop = false;
		}

		public void addFader(Fader fader) {
			m_faders.add(fader);
		}

		public void stop() {
			m_shouldStop = true;
		}

		@Override
		public void run() {
			while (!m_shouldStop) {
				for (Fader fader : m_faders) {
					fader.updateFade();
					if (fader.isFinishedFading()) {
						m_faders.remove(fader);
					}
				}
				try {
					Thread.sleep(FADE_DELAY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class Fader {
		private Notification m_note;
		private long m_fadeStartTime;
		private double m_startFade;
		private double m_stopFade;
		private double m_deltaFade; // delta fade per millisecond

		public Fader(Notification note, double deltaFade, double stopFade) {
			m_note = note;
			m_deltaFade = deltaFade;
			m_stopFade = stopFade;
			m_startFade = note.getOpacity();
			m_fadeStartTime = System.currentTimeMillis();
		}

		public void updateFade() {
			long deltaTime = System.currentTimeMillis() - m_fadeStartTime;
			if (!isFinishedFading()) {
				m_note.setOpacity(m_startFade + m_deltaFade * deltaTime);
			} else {
				if (m_deltaFade < 0) {
					m_note.hide();
				}
			}
		}

		public boolean isFinishedFading() {
			return (m_deltaFade > 0) ? m_note.getOpacity() >= m_stopFade : m_note.getOpacity() <= m_stopFade;
		}
	}
}
