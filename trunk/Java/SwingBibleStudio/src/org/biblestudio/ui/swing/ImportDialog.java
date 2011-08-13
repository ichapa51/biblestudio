package org.biblestudio.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import org.biblestudio.client.ActionStatusListener;
import org.biblestudio.client.BibleReader;
import org.biblestudio.client.BibleSource;
import org.biblestudio.client.Command;
import org.biblestudio.client.ImportMode;
import org.biblestudio.client.event.CompleteStatusEvent;
import org.biblestudio.client.event.ErrorStatusEvent;
import org.biblestudio.client.event.MessageStatusEvent;
import org.biblestudio.client.event.PercentStatusEvent;

/**
 * 
 * @author Israel Chapa
 * Creation Date: 11/08/2011
 */
@SuppressWarnings("serial")
public class ImportDialog extends JPanel implements ActionStatusListener {

	private Component modalParent;
	JTextField fileTextField;
	JButton selectFileButton;
	JButton importButton;
	JProgressBar progressBar;
	JLabel statusLabel;
	
	public ImportDialog() {
		super(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setOpaque(true);
		JPanel panel = new JPanel(new BorderLayout());
		fileTextField = new JTextField();
		fileTextField.setColumns(30);
		fileTextField.setEditable(false);
		panel.add(fileTextField, BorderLayout.CENTER);
		JPanel p = new JPanel(new BorderLayout());
		selectFileButton = new JButton("...");
		selectFileButton.setToolTipText(App.getContext().getResources().getLabelSelectFile());
		selectFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectFile();
			}
		});
		p.add(selectFileButton, BorderLayout.WEST);
		importButton = new JButton(App.getContext().getResources().getLabelImport());
		importButton.setEnabled(false);
		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				importBible();
			}
		});
		p.add(importButton, BorderLayout.EAST);
		panel.add(p, BorderLayout.EAST);
		add(panel, BorderLayout.NORTH);
		progressBar = new JProgressBar();
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		p.add(Box.createRigidArea(new Dimension(10,40)), BorderLayout.WEST);
		p.add(progressBar, BorderLayout.CENTER);
		p.add(Box.createRigidArea(new Dimension(10,40)), BorderLayout.EAST);
		add(p, BorderLayout.CENTER);
		statusLabel = new JLabel();
		add(statusLabel, BorderLayout.SOUTH);
	}
	
	public void setModalParent(Component modalParent) {
		this.modalParent = modalParent;
	}

	public Component getModalParent() {
		return modalParent;
	}

	protected void selectFile() {
		JFileChooser fileChooser = new JFileChooser();
		int r = fileChooser.showOpenDialog(modalParent);
		if (JFileChooser.APPROVE_OPTION == r) {
			fileTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
			importButton.setEnabled(true);
		}
	}
	
	protected void importBible() {
		selectFileButton.setEnabled(false);
		importButton.setEnabled(false);
		progressBar.setIndeterminate(true);
		java.io.File file = new java.io.File(fileTextField.getText());
		BibleReader reader = org.biblestudio.client.BibleFactory.
								getFactory().createBibleSourceFromFile(file);
		BibleSource source = new BibleSource(reader, ImportMode.REPLACE);
		Command<?> cmd = App.getContext().getDataClient().createImportCommand(source);
		cmd.addActionStatusListener(this);
		cmd.executeAsync();
	}
	
	@Override
	public void actionCompleted(CompleteStatusEvent e) {
		progressBar.setIndeterminate(false);
		if (e.isSuccess()) {
			App.getContext().getDesktop().closeAll();
			App.getContext().restart();
			statusLabel.setText("Bible imported successfully");
		}
		selectFileButton.setEnabled(true);
		importButton.setEnabled(true);
	}

	@Override
	public void messageSent(MessageStatusEvent e) {
		statusLabel.setText(e.getMessage());
	}

	@Override
	public void percentDone(PercentStatusEvent e) {
		if (e.getPercent() == null) {
			progressBar.setIndeterminate(true);
		} else {
			progressBar.setIndeterminate(false);
			progressBar.setValue(e.getPercent().intValue());
		}
	}

	@Override
	public void errorFound(ErrorStatusEvent e) {
		e.getError().printStackTrace();
		statusLabel.setText(e.getError().getMessage());
	}
	
	public static void showDialog() {
		JDialog dialog = new JDialog(App.getContext().getMainFrame(),
				App.getContext().getResources().getDialogImport(),
				Dialog.ModalityType.APPLICATION_MODAL);
		ImportDialog panel = new ImportDialog();
		panel.setModalParent(dialog);
		dialog.setContentPane(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(App.getContext().getMainFrame());
		dialog.setVisible(true);
	}
}
