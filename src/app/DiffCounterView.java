package app;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

public class DiffCounterView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static DiffCounter diff;

	private static void createAndShowGUI() {
		final DiffCounterView frame = new DiffCounterView();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height;
		int width = screenSize.width;

		JLabel file1 = new JLabel("<html><ol>" + diff.originalFile1
				+ "</ol></html>");
		JLabel file2 = new JLabel("<html><ol>" + diff.originalFile2
				+ "</ol></html>");
		file1.setBorder(new EmptyBorder(0, 10, 0, 0));
		file2.setBorder(new EmptyBorder(0, 10, 0, 0));
		JSplitPane files = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(file1), new JScrollPane(file2));
		files.setResizeWeight(.5d);

		JLabel diffLabel = new JLabel(
				"<html><p style='font-size:42pt;'>Base LOC: "
						+ diff.originalCount + "<br>Added LOC: "
						+ diff.LOCadded + "<br>Deleted LOC: " + diff.LOCdeleted
						+ "<br>Total LOC: " + diff.newCount
						+ " <br>Changed LOC: " + diff.LOCmodified
						+ "<br>New & Changed LOC: "
						+ (diff.LOCadded + diff.LOCmodified) + "</p></html>");
		diffLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, files,
				new JScrollPane(diffLabel));
		splitPane.setResizeWeight(.8d);
		frame.add(splitPane);
		frame.setPreferredSize(new Dimension(width, height));
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) throws FileNotFoundException {

		int originalCount = 0;
		int newCount = 0;
		try {
			JavaLineCounter.main(new String[0]);
			originalCount = JavaLineCounter.LOC;
		} catch (Exception e) {
			e.printStackTrace();
		}

		File file1 = JavaLineCounter.f;

		try {
			JavaLineCounter.LOC = 0;
			JavaLineCounter.main(new String[0]);
			newCount = JavaLineCounter.LOC;
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file2 = JavaLineCounter.f;

		diff = new DiffCounter(file1, file2, originalCount, newCount);
		createAndShowGUI();
	}
}
