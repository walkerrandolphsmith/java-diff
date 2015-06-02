package util;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FileChooser extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5185811260167571440L;
	final JFileChooser chooser;
	public FileChooser() {
		JPanel content = new JPanel();

		chooser = new JFileChooser();
		chooser.setCurrentDirectory(chooser.getFileSystemView().getParentDirectory(new File("C:\\")));
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.showDialog(content, "Select file");
		this.setContentPane(content);
		this.setTitle("Choose Program ");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	public JFileChooser getFileChooser() {
		return chooser;
	}

	public File getFile() {
		return getFileChooser().getSelectedFile();
	}
}
