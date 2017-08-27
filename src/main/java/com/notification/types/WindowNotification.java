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
 */package com.notification.types;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JWindow;

import com.notification.Notification;

import info.kapable.utils.owanotifier.theme.WindowTheme;

/**
 * A Notification which displays in a JWindow, handles click events, and allows subclasses to supply a JPanel. The
 * default Notification dimensions are set; if subclasses want to override this, they can do so in their constructors.
 */
public abstract class WindowNotification extends Notification {
	private JWindow m_window;
	private JPanel m_panel;
	private boolean m_closeOnClick;
	private MouseAdapter m_listener;

	private WindowTheme m_theme;

	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 100;
	public static final String CLICKED = "clicked";
	public static final String SHOWN = "shown";
	public static final String HIDDEN = "hidden";

	public WindowNotification() {
		m_window = new JWindow();
		m_window.setAlwaysOnTop(true);

		m_listener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireListeners(CLICKED);
				if (m_closeOnClick)
					removeFromManager();
			}
		};

		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setPanel(new JPanel());
	}

	protected JWindow getWindow() {
		return m_window;
	}

	protected void setPanel(JPanel panel) {
		if (m_panel != null) {
			m_window.remove(m_panel);
			m_panel.removeMouseListener(m_listener);
		}

		m_panel = panel;

		m_window.add(m_panel);
		m_panel.addMouseListener(m_listener);
	}

	/**
	 * @return whether or not the Notification should close when it's clicked
	 */
	public boolean isCloseOnClick() {
		return m_closeOnClick;
	}

	/**
	 * @param close
	 *
	 *            whether or not the Notification should close when it's clicked
	 */
	public void setCloseOnClick(boolean close) {
		m_closeOnClick = close;
	}

	protected WindowTheme getWindowTheme() {
		return m_theme;
	}

	/**
	 * Sets the theme of the WindowNotification. It is up to the subclasses how they want to interpret the "image"
	 * attribute of the theme.
	 *
	 * @param theme
	 *            the WindowTheme to set
	 */
	public void setWindowTheme(WindowTheme theme) {
		m_theme = theme;

		m_window.setBackground(theme.background);
		m_window.setForeground(theme.foreground);
		//m_window.setOpacity((float) theme.opacity);
		m_window.setSize(theme.width, theme.height);

		m_panel.setBackground(theme.background);
		m_panel.setForeground(theme.foreground);

		for (Component comp : m_panel.getComponents()) {
			recursiveSetTheme(theme, comp);
		}
	}

	private void recursiveSetTheme(WindowTheme theme, Component comp) {
		comp.setBackground(theme.background);
		comp.setForeground(theme.foreground);

		if (comp instanceof Container) {
			Container container = (Container) comp;
			for (Component component : container.getComponents()) {
				recursiveSetTheme(theme, component);
			}
		}
	}

	@Override
	public int getX() {
		return m_window.getX();
	}

	@Override
	public int getY() {
		return m_window.getY();
	}

	@Override
	public void setLocation(int x, int y) {
		m_window.setLocation(x, y);
	}

	@Override
	public int getWidth() {
		return m_window.getWidth();
	}

	@Override
	public int getHeight() {
		return m_window.getHeight();
	}

	@Override
	public void setSize(int width, int height) {
		m_window.setSize(width, height);
	}

	@Override
	public void show() {
		m_window.setVisible(true);
		fireListeners(SHOWN);
	}

	@Override
	public void hide() {
		m_window.dispose();
		fireListeners(HIDDEN);
	}

	@Override
	public boolean isShown() {
		return m_window.isVisible();
	}

	@Override
	public double getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setOpacity(double opacity) {
		// TODO Auto-generated method stub
		
	}
}
