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
 */
package info.kapable.utils.owanotifier.desktop;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogWindowPanel extends JFrame  {

	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 6124731365121421622L;
	
	/**
	 * The textArea
	 */
	private JTextArea jLogTextArea;
	
	private static LogWindowPanel instance;
	
	/**
	 * Singleton accessory
	 * @return
	 * 		The only instance of LogWindowPanel
	 */
	public static LogWindowPanel getInstance() {
		if(instance == null) {
			instance = new LogWindowPanel();
		}
		return instance;
	}

	/**
	 * Constructor
	 */
	private LogWindowPanel() {

        this.setSize(500, 400);
        this.setVisible(false);
		jLogTextArea = new JTextArea();
		TextAreaAppender.setTextArea(jLogTextArea);
		
        JPanel thePanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(jLogTextArea);
        thePanel.add(scrollPane);
		this.add(thePanel);
	}
}
