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

import org.biblestudio.client.ActionStatusListener;
import org.biblestudio.client.Command;
import org.biblestudio.client.SearchQuery;
import org.biblestudio.client.event.CompleteStatusEvent;
import org.biblestudio.client.event.ErrorStatusEvent;
import org.biblestudio.client.event.MessageStatusEvent;
import org.biblestudio.client.event.PercentStatusEvent;
import org.biblestudio.model.Bible;
import org.biblestudio.model.Paragraph;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 09/08/2011
 */
@SuppressWarnings("serial")
public class SearchPanel extends JPanel implements ActionStatusListener, PropertyChangeListener {

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
		resultsPanel.addPropertyChangeListener(this);
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
		Command<SearchQuery> cmd = App.getContext().getDataClient().createSearchQueryCommand(query);
		cmd.addActionStatusListener(this);
		cmd.executeAsync();
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.ActionStatusListener#actionCompleted(org.ebible.client.event.CompleteStatusEvent)
	 */
	@Override
	public void actionCompleted(CompleteStatusEvent evt) {
		if (evt.isSuccess()) {
			SearchQuery query = (SearchQuery)evt.getTagObject();
			if (query.getResult() != null) {
				String result = query.getResult().getList().size() + " Ocurrencias";
				resultsLabel.setText(result);
				resultsPanel.setResults(query.getResult().getList());
			}
		}
	}

	@Override
	public void messageSent(MessageStatusEvent event) {

	}

	@Override
	public void percentDone(PercentStatusEvent event) {

	}

	@Override
	public void errorFound(ErrorStatusEvent event) {

	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("LastDoubleClickedParagraph".equals(evt.getPropertyName())) {
			this.firePropertyChange("LastDoubleClickedParagraph", evt.getOldValue(), evt.getNewValue());
		}
	}
}
