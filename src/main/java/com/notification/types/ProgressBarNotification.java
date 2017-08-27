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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import info.kapable.utils.owanotifier.theme.TextTheme;

public class ProgressBarNotification extends BorderLayoutNotification {
	private JLabel m_label;
	private JProgressBar m_progress;

	public ProgressBarNotification() {
		m_label = new JLabel();
		m_progress = new JProgressBar();

		this.addComponent(m_label, BorderLayout.NORTH);

		JPanel progressPanel = new JPanel(new BorderLayout());
		progressPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
		progressPanel.add(m_progress, BorderLayout.CENTER);
		this.addComponent(progressPanel, BorderLayout.CENTER);
	}

	/**
	 * This will set the text font to that of the title font.
	 *
	 * @param theme
	 *            the TextTheme to set
	 */
	public void setTextTheme(TextTheme theme) {
		m_label.setFont(theme.title);
		m_label.setForeground(theme.titleColor);
	}

	public String getTitle() {
		return m_label.getText();
	}

	public void setTitle(String title) {
		m_label.setText(title);
	}

	/**
	 * Sest the progress of the progress bar, from 0 to 100.
	 *
	 * @param progress
	 *            the progress to set
	 */
	public void setProgress(int progress) {
		m_progress.setValue(progress);
	}

	/**
	 * @return the progress of the progress bar, from 0 to 100
	 */
	public int getProgress() {
		return m_progress.getValue();
	}
}
