package org.biblestudio.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.biblestudio.ui.swing.resx.AppResources;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 12/08/2011
 */
@SuppressWarnings("serial")
public class OptionsDialog extends JPanel {

	private Component modalParent;
	JComboBox uiLangBox;
	JComboBox lookAndFeelBox;
	
	public OptionsDialog() {
		super(new BorderLayout());
		AppConfig config = App.getContext().getConfiguration();
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		AppResources resx = App.getContext().getResources();
		JLabel uiLangLabel = new JLabel(resx.getLabelUILanguage());
		JLabel lookAndFeelLabel = new JLabel(resx.getLabelLookAndFeel());
		lookAndFeelBox = new JComboBox();
		UIManager.LookAndFeelInfo[] lfInfos = UIManager.getInstalledLookAndFeels();
		int selIndex = 0;
		for (int i = 0; i < lfInfos.length; i++) {
			lookAndFeelBox.addItem(lfInfos[i].getName());
			if (lfInfos[i].getClassName().equals(config.getLookAndFeel())) {
				selIndex = i;
			}
		}
		lookAndFeelBox.setSelectedIndex(selIndex);
		uiLangBox = new JComboBox();
		uiLangBox.addItem(resx.getLanguageEnglish());
		uiLangBox.addItem(resx.getLanguageSpanish());
		if (config.getUILocale().getLanguage().equals("es")) {
			uiLangBox.setSelectedIndex(1);
		} else {
			uiLangBox.setSelectedIndex(0);
		}
		// Do Form Layout
		JPanel formPanel = new JPanel();
		GroupLayout layout = new GroupLayout(formPanel);
		formPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		hGroup.addGroup(layout.createParallelGroup().
				addComponent(uiLangLabel).addComponent(lookAndFeelLabel));
		hGroup.addGroup(layout.createParallelGroup().
	            addComponent(uiLangBox).addComponent(lookAndFeelBox));
		layout.setHorizontalGroup(hGroup);
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
				addComponent(uiLangLabel).addComponent(uiLangBox));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
				addComponent(lookAndFeelLabel).addComponent(lookAndFeelBox));
		layout.setVerticalGroup(vGroup);
		add(formPanel, BorderLayout.CENTER);
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.RIGHT);
		JPanel buttonsPanel = new JPanel(flow);
		JButton acceptButton = new JButton(resx.getButtonAccept());
		acceptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		JButton cancelButton = new JButton(resx.getButtonCancel());
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		buttonsPanel.add(acceptButton);
		buttonsPanel.add(cancelButton);
		add(buttonsPanel, BorderLayout.SOUTH);
	}
	
	public void setModalParent(Component modalParent) {
		this.modalParent = modalParent;
	}

	public Component getModalParent() {
		return modalParent;
	}
	
	public void save() {
		AppConfig config = App.getContext().getConfiguration();
		if (this.uiLangBox.getSelectedIndex() == 0) {
			config.setUILocale(Locale.ENGLISH);
		} else {
			config.setUILocale(new Locale("es"));
		}
		UIManager.LookAndFeelInfo[] lfInfos = UIManager.getInstalledLookAndFeels();
		for (LookAndFeelInfo info : lfInfos) {
			if (info.getName().equals(lookAndFeelBox.getSelectedItem())) {
				config.setLookAndFeel(info.getClassName());
			}
		}
		config.save();
		close();
	}
	
	protected void close() {
		if (modalParent instanceof JDialog) {
			((JDialog)modalParent).dispose();
		}
	}
	
	public static void showDialog() {
		JDialog dialog = new JDialog(App.getContext().getMainFrame(),
				App.getContext().getResources().getDialogOptions(),
				Dialog.ModalityType.APPLICATION_MODAL);
		OptionsDialog panel = new OptionsDialog();
		panel.setModalParent(dialog);
		dialog.setContentPane(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(App.getContext().getMainFrame());
		dialog.setVisible(true);
	}
}
