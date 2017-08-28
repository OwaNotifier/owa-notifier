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
