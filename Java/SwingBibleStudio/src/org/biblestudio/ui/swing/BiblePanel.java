package org.biblestudio.ui.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.biblestudio.client.ActionStatusListener;
import org.biblestudio.client.Command;
import org.biblestudio.client.CommonQueries;
import org.biblestudio.client.IdQuery;
import org.biblestudio.client.IndexQuery;
import org.biblestudio.client.IndexType;
import org.biblestudio.client.SearchQuery;
import org.biblestudio.client.event.CompleteStatusEvent;
import org.biblestudio.client.event.ErrorStatusEvent;
import org.biblestudio.client.event.MessageStatusEvent;
import org.biblestudio.client.event.PercentStatusEvent;
import org.biblestudio.model.Bible;
import org.biblestudio.model.Book;
import org.biblestudio.model.ModelEntity;
import org.biblestudio.model.Paragraph;
import org.biblestudio.ui.swing.event.AppEvent;
import org.biblestudio.ui.swing.event.AppEventType;
import org.biblestudio.ui.swing.html.HtmlPanel;
import org.biblestudio.ui.swing.model.EntityTreeModel;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 28/07/2011
 */
@SuppressWarnings("serial")
public class BiblePanel extends JPanel implements AppListener, ActionStatusListener, PropertyChangeListener {

	private JTabbedPane tabbedPane;
	private JSplitPane splitPane;
	private ContentsPanel contentsPanel;
	private SearchPanel searchPanel;
	private JButton previousButton;
	private JButton nextButton;
	private JLabel titleLabel;
	private Bible bible;
	private HtmlPanel reader;
	private IndexQuery indexQuery;
	private IndexType indexType;
	
	public BiblePanel() {
		init(App.getContext().getDefaultBible());
	}
	
	public BiblePanel(Bible bible) {
		init(bible);
	}
	
	protected void init(Bible bible) {
		setLayout(new BorderLayout());
		indexType = IndexType.BookDiv;
		contentsPanel = new ContentsPanel();
		contentsPanel.addPropertyChangeListener(this);
		searchPanel = new SearchPanel();
		searchPanel.addPropertyChangeListener(this);
		tabbedPane = new JTabbedPane();
		tabbedPane.add(App.getContext().getResources().getLabelContents(), contentsPanel);
		tabbedPane.add(App.getContext().getResources().getLabelSearch(), searchPanel);
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		reader = new HtmlPanel();
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(createToolBar(), BorderLayout.NORTH);
		mainPanel.add(reader, BorderLayout.CENTER);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, tabbedPane, mainPanel);
		splitPane.setOneTouchExpandable(true);
		add(splitPane, BorderLayout.CENTER);
		App.getContext().addAppListener(this);
		setBible(bible);
	}
	
	public void setBible(Bible bible) {
		Bible oldBible = this.bible;
		this.bible = bible;
		if (bible == null) {
			contentsPanel.setModelRoot(null);
			searchPanel.setBible(null);
			searchPanel.setEnabled(false);
		} else if (bible != null && App.getContext().getDataClient() != null) {
			contentsPanel.setModelRoot(bible);
			searchPanel.setBible(bible);
			searchPanel.setEnabled(true);
		}
		this.firePropertyChange("Bible", oldBible, bible);
	}

	public Bible getBible() {
		return bible;
	}
	
	public boolean hasBible() {
		return getBible() != null;
	}
	
	protected JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		titleLabel = new JLabel();
		toolBar.addSeparator();
		toolBar.add(titleLabel);
		toolBar.add(Box.createHorizontalGlue());
		previousButton = new JButton("<");
		previousButton.setToolTipText(App.getContext().getResources().getLabelPrevious());
		previousButton.setEnabled(false);
		previousButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				previousIndex();
			}
		});
		toolBar.add(previousButton);
		nextButton = new JButton(">");
		nextButton.setToolTipText(App.getContext().getResources().getLabelNext());
		nextButton.setEnabled(false);
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextIndex();
			}
		});
		toolBar.add(nextButton);
		return toolBar;
	}
	
	protected void previousIndex() {
		if (indexQuery != null) {
			IndexQuery previous = indexQuery.getPreviousQuery("UpdateIndex");
			if (previous != null) {
				showIndex(previous.getIdParagraph());
			}
		}
	}
	
	protected void nextIndex() {
		if (indexQuery != null) {
			IndexQuery next = indexQuery.getNextQuery("UpdateIndex");
			if (next != null) {
				showIndex(next.getIdParagraph());
			}
		}
	}

	protected void showChapter(String bookName, int chapter) {
		SearchQuery query = new SearchQuery("ShowChapter");
		query.setIncludingHeaders(true);
		query.setBibleKey(bible.getName());
		query.setText(bookName + " " + chapter);
		Command<?> cmd = App.getContext().getDataClient().createSearchQueryCommand(query);
		cmd.addActionStatusListener(this);
		cmd.executeAsync();
	}
	
	protected void showIndex(int pId) {
		IndexQuery query = new IndexQuery("ShowIndex", getBible().getName(), getIndexType(), pId);
		Command<?> cmd = App.getContext().getDataClient().createIndexQueryCommand(query);
		cmd.addActionStatusListener(this);
		cmd.executeAsync();
	}
	
	protected void showContent(List<Paragraph> content) {
		if (content != null && content.size() > 0) {
			Paragraph p = content.get(0);
			IdQuery q = CommonQueries.getBookById(p.getIdBook(), null);
			Book book = App.getContext().getModelCache().get(q);
			if (book == null) {
				Command<?> cmd = App.getContext().getDataClient().createGetEntityCommand(q);
				cmd.execute();
				book = (Book) q.getResult();
			}
			titleLabel.setText(book.getTitle() + " " + p.getDivKey());
		}
		reader.setContent(content);
	}

	public void setIndexType(IndexType indexType) {
		this.indexType = indexType;
	}

	public IndexType getIndexType() {
		return indexType;
	}

	/* (non-Javadoc)
	 * @see org.ebible.ui.swing.AppListener#applicationChanged(org.ebible.ui.swing.event.AppEvent)
	 */
	@Override
	public void applicationChanged(AppEvent event) {
		if (event.getEventType().equals(AppEventType.DataSourceChanged)) {
			if (hasBible()) {
				//TODO Get the same bible?
			} else {
				setBible(App.getContext().getDefaultBible());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.ActionStatusListener#actionCompleted(org.ebible.client.event.CompleteStatusEvent)
	 */
	@Override
	public void actionCompleted(CompleteStatusEvent e) {
		if (e.isSuccess()) {
			if ("ShowChapter".equals(e.getTag())) {
				SearchQuery q = (SearchQuery) e.getTagObject();
				showContent(q.getResult().getList());
				if (q.getResult().getList().size() > 0) {
					indexQuery = new IndexQuery("UpdateIndex", getBible().getName(), getIndexType(), q.getResult().getList().get(0).getId());
					Command<?> cmd = App.getContext().getDataClient().createIndexQueryCommand(indexQuery);
					cmd.addActionStatusListener(this);
					cmd.executeAsync();
				}
			} else if ("ShowIndex".equals(e.getTag())) {
				IndexQuery q = (IndexQuery) e.getTagObject();
				showContent(q.getResult().getList());
				previousButton.setEnabled(q.hasPrevious());
				nextButton.setEnabled(q.hasNext());
				indexQuery = q;
			} else if ("ShowIndex".equals(e.getTag()) || "UpdateIndex".equals(e.getTag())) {
				IndexQuery q = (IndexQuery) e.getTagObject();
				previousButton.setEnabled(q.hasPrevious());
				nextButton.setEnabled(q.hasNext());
				indexQuery = q;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.ActionStatusListener#messageSent(org.ebible.client.event.MessageStatusEvent)
	 */
	@Override
	public void messageSent(MessageStatusEvent event) {
		
	}

	@Override
	public void percentDone(PercentStatusEvent event) {}

	/* (non-Javadoc)
	 * @see org.ebible.client.ActionStatusListener#errorFound(org.ebible.client.event.ErrorStatusEvent)
	 */
	@Override
	public void errorFound(ErrorStatusEvent e) {
		e.getError().printStackTrace();
		App.getContext().showError(e.getError());
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("LastDoubleClickedEntity")) {
			ModelEntity e = contentsPanel.getLastDoubleClickedEntity();
			if (e != null && bible != null && App.getContext().getDataClient() != null) {
				if (e instanceof EntityTreeModel.ChapterNode) {
					EntityTreeModel.ChapterNode chapter = (EntityTreeModel.ChapterNode)e;
					showChapter(chapter.getBook().getTitle(), chapter.getChapter());
				}
			}
		} else if (evt.getPropertyName().equals("LastDoubleClickedParagraph")) {
			Paragraph p = searchPanel.getLastDoubleClickedParagraph();
			showIndex(p.getId());
		}
	}
	
	public void dispose() {
		App.getContext().removeAppListener(this);
	}
}
