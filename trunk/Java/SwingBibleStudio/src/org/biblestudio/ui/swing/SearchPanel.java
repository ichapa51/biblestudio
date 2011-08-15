package org.biblestudio.ui.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.biblestudio.client.ActionStatusAdapter;
import org.biblestudio.client.SearchQuery;
import org.biblestudio.client.event.CompleteStatusEvent;
import org.biblestudio.client.event.ErrorStatusEvent;
import org.biblestudio.model.Bible;
import org.biblestudio.model.Paragraph;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 09/08/2011
 */
@SuppressWarnings("serial")
public class SearchPanel extends JPanel {

	private JTextField searchField;
	private JLabel resultsLabel;
	private ResultsPanel resultsPanel;
	private Bible bible;
	
	public SearchPanel() {
		super(new BorderLayout());
		searchField = new JTextField();
		searchField.setEnabled(false);
		searchField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		northPanel.add(searchField, BorderLayout.CENTER);
		resultsLabel = new JLabel();
		resultsLabel.setHorizontalAlignment(JLabel.RIGHT);
		northPanel.add(resultsLabel, BorderLayout.SOUTH);
		add(northPanel, BorderLayout.NORTH);
		resultsPanel = new ResultsPanel();
		resultsPanel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("LastDoubleClickedParagraph".equals(evt.getPropertyName())) {
					firePropertyChange("LastDoubleClickedParagraph", evt.getOldValue(), evt.getNewValue());
				}
			}
		});
		add(resultsPanel, BorderLayout.CENTER);
	}

	public void setBible(Bible bible) {
		this.bible = bible;
		List<Paragraph> test = new ArrayList<Paragraph>();
		Paragraph p = new Paragraph();
		p.setPlainText("a");
		test.add(p);
		resultsPanel.setResults(test);
		test.clear();
		resultsPanel.setResults(test);
	}

	public Bible getBible() {
		return bible;
	}
	
	public Paragraph getLastDoubleClickedParagraph() {
		return resultsPanel.getLastDoubleClickedParagraph();
	}
	
	@Override
	public void setEnabled(boolean e) {
		searchField.setEnabled(e);
	}
	
	protected void search() {
		resultsPanel.setResults(null);
		SearchQuery query = new SearchQuery();
		query.setBibleKey(getBible().getName());
		query.setText(searchField.getText());
		App.getContext().getDataClient().createSearchQueryCommand(query).
			setActionStatus(new ActionStatusAdapter() {
				@Override
				public void actionCompleted(CompleteStatusEvent e) {
					searchCompleted(e);
				}
				@Override
				public void errorFound(ErrorStatusEvent e) {
					e.getError().printStackTrace();
				}
		}).executeAsync();
	}

	protected void searchCompleted(CompleteStatusEvent e) {
		if (e.isSuccess()) {
			SearchQuery query = (SearchQuery)e.getTagObject();
			if (query.getResult() != null) {
				String result = query.getResult().getList().size() + " " + 
					App.getContext().getResources().getLabelSearchResults();
				resultsLabel.setText(result);
				resultsPanel.setResults(query.getResult().getList());
			}
		}
	}
}
