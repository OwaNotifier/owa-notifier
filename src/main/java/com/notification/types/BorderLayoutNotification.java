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

import info.kapable.utils.owanotifier.theme.WindowTheme;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Lays out Swing Components in a BorderLayout.
 */
public class BorderLayoutNotification extends WindowNotification {
	protected JPanel m_panel;

	public static final int PANEL_PADDING = 10;

	public BorderLayoutNotification() {
		super();

		m_panel = new JPanel(new BorderLayout());
		m_panel.setBorder(new EmptyBorder(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));
		setPanel(m_panel);
	}

	/**
	 * Adds a Component to the Notification.
	 *
	 * @param comp
	 *            the Component to add
	 * @param borderLayout
	 *            the BorderLayout String, e.g. BorderLayout.NORTH
	 */
	public void addComponent(Component comp, String borderLayout) {
		m_panel.add(comp, borderLayout);

		WindowTheme theme = this.getWindowTheme();
		if (theme != null) {
			comp.setBackground(theme.background);
			comp.setForeground(theme.foreground);
		}
		getWindow().validate();
		getWindow().repaint();
	}

	/**
	 * Removes a component.
	 *
	 * @param comp
	 *            the Component to remove
	 */
	public void removeComponent(Component comp) {
		m_panel.remove(comp);

		getWindow().validate();
		getWindow().repaint();
	}
}
