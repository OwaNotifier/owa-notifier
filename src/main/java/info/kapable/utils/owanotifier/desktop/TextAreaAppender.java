package info.kapable.utils.owanotifier.desktop;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

public class TextAreaAppender extends WriterAppender {
    private static JTextArea jTextArea;
 
    /** Set the target JTextArea for the logging information to appear. */
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
