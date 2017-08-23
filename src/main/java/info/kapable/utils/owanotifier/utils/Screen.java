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
 */package info.kapable.utils.owanotifier.utils;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import com.notification.Notification;
import com.notification.NotificationFactory.Location;

public class Screen {
	private int m_width;
	private int m_height;

	private int m_leftX;
	private int m_centerX;
	private int m_rightX;

	private int m_topY;
	private int m_centerY;
	private int m_bottomY;

	private int m_padding;

	private Screen(boolean spanMultipleMonitors, int padding) {
		m_padding = padding;
		setupDimensions(spanMultipleMonitors);
		calculatePositions();
	}

	public static Screen standard() {
		return new Screen(true, 80);
	}

	public static Screen withSpan(boolean spanMultipleMonitors) {
		return new Screen(spanMultipleMonitors, 80);
	}

	public static Screen withPadding(int padding) {
		return new Screen(true, padding);
	}

	public static Screen withSpanAndPadding(boolean spanMultipleMonitors, int padding) {
		return new Screen(spanMultipleMonitors, padding);
	}

	private void setupDimensions(boolean spanMultipleMonitors) {
		if (spanMultipleMonitors) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			m_width = screenSize.width;
			m_height = screenSize.height;

		} else {
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			m_width = gd.getDisplayMode().getWidth();
			m_height = gd.getDisplayMode().getHeight();
		}
	}

	private void calculatePositions() {
		m_leftX = m_padding;
		m_centerX = (int) (m_width / 2d);
		m_rightX = m_width - m_padding;

		m_topY = m_padding;
		m_centerY = (int) (m_height / 2d);
		m_bottomY = m_height - m_padding;
	}

	public int getX(Location loc, Notification note) {
		switch (loc) {
		case SOUTHWEST:
			return m_leftX;
		case WEST:
			return m_leftX;
		case NORTHWEST:
			return m_leftX;
		case NORTH:
			return m_centerX - note.getWidth() / 2;
		case SOUTH:
			return m_centerX - note.getWidth() / 2;
		case SOUTHEAST:
			return m_rightX - note.getWidth();
		case EAST:
			return m_rightX - note.getWidth();
		case NORTHEAST:
			return m_rightX - note.getWidth();
		default:
			return -1;
		}
	}

	public int getY(Location loc, Notification note) {
		switch (loc) {
		case SOUTHWEST:
			return m_bottomY - note.getHeight();
		case WEST:
			return m_centerY - note.getHeight() / 2;
		case NORTHWEST:
			return m_topY;
		case NORTH:
			return m_topY;
		case SOUTH:
			return m_bottomY - note.getHeight();
		case SOUTHEAST:
			return m_bottomY - note.getHeight();
		case EAST:
			return m_centerY - note.getHeight() / 2;
		case NORTHEAST:
			return m_topY;
		default:
			return -1;
		}
	}

	public int getPadding() {
		return m_padding;
	}
}
