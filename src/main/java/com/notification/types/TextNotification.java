package com.notification.types;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import info.kapable.utils.owanotifier.desktop.DesktopProxy;
import info.kapable.utils.owanotifier.theme.TextTheme;
import info.kapable.utils.owanotifier.theme.WindowTheme;

/**
 * A text notification which will display a title and a subtitle.
 */
public class TextNotification extends BorderLayoutNotification {
	protected JLabel m_titleLabel;
	protected JTextArea m_subtitleArea;
	protected JLabel m_fromLabel;
	private TextTheme m_textTheme;

	public void addActionListener(MouseListener l) {
		this.m_titleLabel.addMouseListener(l);
		this.m_subtitleArea.addMouseListener(l);
	}
	
	public TextNotification() {
		m_fromLabel = new JLabel();
		m_titleLabel = new JLabel();
		m_titleLabel.addMouseListener(new MouseAdapter() {
			
			
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
					DesktopProxy.browse("https://outlook.office.com/owa/");
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }

        });
		m_subtitleArea = new JTextArea();
		JPanel panelHeader = new JPanel();
		panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.PAGE_AXIS));
		JButton dimissButton = new JButton();
		dimissButton.setText("X");
		final TextNotification me = this;
		dimissButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				me.removeFromManager();
			}
			
		});
		dimissButton.setOpaque(false);
		dimissButton.setContentAreaFilled(false);
		dimissButton.setBorderPainted(false);
		dimissButton.setBounds((int) (this.getWidth() - dimissButton.getPreferredSize().getWidth()), 0, (int) dimissButton.getPreferredSize().getWidth(), (int) dimissButton.getPreferredSize().getHeight());
		m_panel.add(dimissButton);
		
		
		panelHeader.add(m_fromLabel);
		panelHeader.add(m_titleLabel);
		this.addComponent(panelHeader, BorderLayout.NORTH);
		this.addComponent(m_subtitleArea, BorderLayout.CENTER);
	}

	public String getTitle() {
		return m_titleLabel.getText();
	}

	public void setTitle(String title) {
		m_titleLabel.setText("<html><u>" + title + "</u></html>");
	}

	public String getSubtitle() {
		return m_subtitleArea.getText();
	}

	public void setSubtitle(String subtitle) {
		m_subtitleArea.setText(subtitle);
	}

	protected TextTheme getTextTheme() {
		return m_textTheme;
	}

	/**
	 * @param theme
	 *            the two Fonts that should be used.
	 */
	public void setTextTheme(TextTheme theme) {
		m_textTheme = theme;
		m_titleLabel.setFont(theme.title);
		m_subtitleArea.setFont(theme.subtitle);
		m_fromLabel.setFont(theme.from);
		m_titleLabel.setForeground(theme.titleColor);
		m_subtitleArea.setForeground(theme.subtitleColor);
		m_fromLabel.setForeground(theme.fromColor);
	}

	@Override
	public void setWindowTheme(WindowTheme theme) {
		super.setWindowTheme(theme);

		if (m_textTheme != null) {
			m_titleLabel.setForeground(m_textTheme.titleColor);
			m_subtitleArea.setForeground(m_textTheme.subtitleColor);
		}
	}

	public void setFrom(String string) {
		if(string == null) {
			m_fromLabel.setVisible(false);
		} else {
			m_fromLabel.setText(string);
		}
		
	}
}
