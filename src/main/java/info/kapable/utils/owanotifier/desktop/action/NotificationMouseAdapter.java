package info.kapable.utils.owanotifier.desktop.action;

import info.kapable.utils.owanotifier.desktop.DesktopProxy;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;

import javax.swing.JLabel;

public class NotificationMouseAdapter extends MouseAdapter {

	JLabel labelToUnderLine;
    public NotificationMouseAdapter(JLabel labelToUnderLine) {
		this.labelToUnderLine = labelToUnderLine;
	}

	@Override
    public void mouseEntered(MouseEvent e) {
		labelToUnderLine.setText("<html><u>" + labelToUnderLine.getText() + "</u></html>");
    }
	
	@Override
    public void mouseExited(MouseEvent e) {
		labelToUnderLine.setText(labelToUnderLine.getText().replace("<html><u>","").replace("</u></html>", ""));
    }
	
	@Override
    public void mouseClicked(MouseEvent e) {
        try {
			DesktopProxy.browse("https://outlook.office.com/owa/");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
    }
}
