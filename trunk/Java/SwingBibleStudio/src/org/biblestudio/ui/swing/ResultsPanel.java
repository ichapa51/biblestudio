package org.biblestudio.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.AbstractListModel;

import org.biblestudio.client.Command;
import org.biblestudio.client.CommonQueries;
import org.biblestudio.client.IdQuery;
import org.biblestudio.model.Book;
import org.biblestudio.model.Paragraph;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 10/08/2011
 */
@SuppressWarnings("serial")
public class ResultsPanel extends JPanel {

	private JScrollPane scroll;
	private JList list;
	protected ParagraphListModel listModel;
	protected Paragraph lastDoubleClickedParagraph;
	protected Paragraph lastPopupClickedParagraph;
	private JPopupMenu popupMenu;
	private JMenuItem popCopy;
	private Clipboard clipboard;
	
	public ResultsPanel() {
		super(new BorderLayout());
		listModel = new ParagraphListModel();
		list = new JList(listModel);
		list.setCellRenderer(new ParagraphCellRenderer());
		scroll = new JScrollPane(list);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setMinimumSize(new Dimension(150,300));
		add(scroll, BorderLayout.CENTER);
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Paragraph p = this.getSelectedParagraph(e);
					if (p != null) {
						setLastDoubleClickedParagraph(p);
					}
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				showPopMenu(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				showPopMenu(e);
			}
			public Paragraph getSelectedParagraph(MouseEvent e) {
				int index = list.locationToIndex(e.getPoint());
				if (index >= 0) {
					return (Paragraph) listModel.getElementAt(index);
				}
				return null;
			}
			public void showPopMenu(MouseEvent e) {
				if (e.isPopupTrigger()) {
					Paragraph p = this.getSelectedParagraph(e);
					if (p != null) {
						setLastPopupClickedParagraph(p);
						popCopy.setText(App.getContext().getResources().getButtonCopy());
						popupMenu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		};
		list.addMouseListener(mouseListener);
		popupMenu = new JPopupMenu();
		popCopy = new JMenuItem("Copy");
		popCopy.setMnemonic('C');
		popupMenu.add(popCopy);
		popCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				Paragraph p = getLastPopupClickedParagraph();
				if (p != null) {
					if (clipboard == null) {
						clipboard = App.getContext().getMainFrame().getToolkit().getSystemClipboard();
					}
					StringSelection data = new StringSelection(p.getPlainText());
					clipboard.setContents(data, data);
				}
			}
		});
	}

	public void setResults(List<Paragraph> results) {
		listModel.setData(results);
	}

	public List<Paragraph> getResults() {
		return listModel.getData();
	}
	
	public void setLastDoubleClickedParagraph(Paragraph p) {
		Paragraph old = this.lastDoubleClickedParagraph;
		this.lastDoubleClickedParagraph = p;
		this.firePropertyChange("LastDoubleClickedParagraph", old, p);
	}

	public Paragraph getLastDoubleClickedParagraph() {
		return lastDoubleClickedParagraph;
	}
	
	public void setLastPopupClickedParagraph(Paragraph p) {
		//Paragraph old = this.lastPopupClickedParagraph;
		this.lastPopupClickedParagraph = p;
		//this.firePropertyChange("LastPopupClickedParagraph", old, p);
	}
	
	public Paragraph getLastPopupClickedParagraph() {
		return lastPopupClickedParagraph;
	}

	private class ParagraphListModel extends AbstractListModel {
		
		private List<Paragraph> data;
		
		public List<Paragraph> getData() {
			return data;
		}
		
		public void setData(List<Paragraph> data) {
			if (this.data != null) {
				this.fireIntervalRemoved(this, 0, getSize());
			}
			this.data = data;
			if (this.data != null) {
				this.fireIntervalAdded(this, 0, getSize());
			}
		}
		
		@Override
		public int getSize() {
			if (data == null) {
				return 0;
			}
			return data.size();
		}
		@Override
		public Object getElementAt(int index) {
			return data.get(index);
		}
	}
	
	private class ParagraphCellRenderer extends JPanel implements ListCellRenderer {

		private Color initialColor;
		private JLabel titleLabel;
		private JTextArea textArea; 
		
		public ParagraphCellRenderer() {
			super();
			this.setLayout(new BorderLayout());
			titleLabel = new JLabel();
			titleLabel.setOpaque(true);
			textArea = new JTextArea();
			//textArea.setRows(2);
			initialColor = textArea.getBackground();
			textArea.setWrapStyleWord(true);
			textArea.setLineWrap(true);
			add(titleLabel, BorderLayout.NORTH);
			add(textArea, BorderLayout.CENTER);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Paragraph p = (Paragraph) value;
			int idBook = p.getIdBook();
			IdQuery q = CommonQueries.getBookById(idBook, null);
			Book book = null;
			if (App.getContext().getModelCache().contains(q)) {
				book = App.getContext().getModelCache().get(q);
			} else {
				Command<?> cmd = App.getContext().getDataClient().createGetEntityCommand(q);
				cmd.execute();
				book = (Book) q.getResult();
				App.getContext().getModelCache().put(q);
			}
			if (book == null) {
				book = new Book();
				book.setTitle("[" + idBook + "]");
			}
			titleLabel.setText(book.getTitle() + " " + p.getDivKey() + ":" + p.getVerse());
			textArea.setText(p.getPlainText());
			setOpaque(true);
			if (isSelected) {
				textArea.setBackground(textArea.getSelectionColor());
			} else {
				textArea.setBackground(initialColor);
			}
			return this;
		}
	}
	
	@Override
	public void updateUI() {
		super.updateUI();
		if (popupMenu != null) {
			popupMenu.updateUI();
		}
	}
}
