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
 */package info.kapable.utils.owanotifier.desktop.action;

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
