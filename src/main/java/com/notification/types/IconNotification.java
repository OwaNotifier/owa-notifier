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

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * An IconNotification displays text, but with an icon.
 */
public class IconNotification extends TextNotification {
	private JLabel m_iconLabel;

	public static final int ICON_PADDING = 10;

	public IconNotification() {
		super();
		m_iconLabel = new JLabel();

		this.removeComponent(m_titleLabel);
		this.removeComponent(m_subtitleArea);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(m_titleLabel, BorderLayout.NORTH);
		panel.add(m_subtitleArea, BorderLayout.CENTER);
		panel.setBorder(new EmptyBorder(0, ICON_PADDING, 0, 0));
		
		this.addComponent(m_iconLabel, BorderLayout.WEST);
		this.addComponent(panel, BorderLayout.CENTER);
	}

	/**
	 * Sets the icon to use.
	 *
	 * @param icon
	 *            the icon to use
	 */
	public void setIcon(Icon icon) {
		m_iconLabel.setIcon(icon);
	}

	/**
	 * @return the icon to use
	 */
	public Icon getIcon() {
		return m_iconLabel.getIcon();
	}
}
