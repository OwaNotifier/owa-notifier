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

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

public class TextAreaAppender extends WriterAppender {
    private static JTextArea jTextArea;
 
    /**
     *  Set the target JTextArea for the logging information to appear. 
     *  @param jTextArea
     *  	Set textArea object to receive logs
     */
    public static void setTextArea(JTextArea jTextArea) {
        TextAreaAppender.jTextArea = jTextArea;
    }
 
    /**
     * Format and then append the loggingEvent to the stored
     * JTextArea.
     */
    @Override
    public void append(LoggingEvent loggingEvent) {
        final String message = this.layout.format(loggingEvent);
 
        // Append formatted message to textarea using the Swing Thread.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	if(jTextArea == null) {
            		LogWindowPanel.getInstance();
            	}
                jTextArea.append(message);
                jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
            }
        });
    }
}
