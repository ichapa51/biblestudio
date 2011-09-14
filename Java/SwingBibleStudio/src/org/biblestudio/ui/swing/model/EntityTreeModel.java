package org.biblestudio.ui.swing.model;

import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.biblestudio.client.Command;
import org.biblestudio.client.CommonQueries;
import org.biblestudio.client.DefaultStatusListener;
import org.biblestudio.client.ModelQuery;
import org.biblestudio.client.event.CompleteStatusEvent;
import org.biblestudio.model.Bible;
import org.biblestudio.model.Book;
import org.biblestudio.model.BookGroup;
import org.biblestudio.model.ModelEntity;
import org.biblestudio.ui.swing.App;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 09/08/2011
 */
public class EntityTreeModel extends DefaultStatusListener implements TreeModel {
	
	private DefaultTreeModel treeModel;
	
	public EntityTreeModel() {
		treeModel = new DefaultTreeModel(new DefaultMutableTreeNode());
	}

	public void setRootEntity(ModelEntity entity) {
		treeModel.setRoot(new DefaultMutableTreeNode(entity));
		nodeChanged((DefaultMutableTreeNode)treeModel.getRoot());
	}

	public ModelEntity getRootEntity() {
		if (treeModel.getRoot() != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeModel.getRoot();
			return getModelEntity(node);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	@Override
	public Object getRoot() {
		return treeModel.getRoot();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	@Override
	public Object getChild(Object parent, int index) {
		return treeModel.getChild(parent, index);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	@Override
	public int getChildCount(Object parent) {
		return treeModel.getChildCount(parent);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	@Override
	public boolean isLeaf(Object node) {
		return treeModel.isLeaf(node);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		treeModel.valueForPathChanged(path, newValue);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int getIndexOfChild(Object parent, Object child) {
		return treeModel.getIndexOfChild(parent, child);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	@Override
	public void addTreeModelListener(TreeModelListener l) {
		treeModel.addTreeModelListener(l);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		treeModel.removeTreeModelListener(l);
	}
	
	public ModelEntity getModelEntity(Object treeNode) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeNode;
		if (node.getUserObject() instanceof ModelEntity) {
			return (ModelEntity) node.getUserObject();
		}
		return null;
	}
	
	protected void nodeChanged(DefaultMutableTreeNode node) {
		ModelEntity e = getModelEntity(node);
		ModelQuery q = null;
		Command<ModelQuery> cmd = null;
		if (e != null) {
			if (e instanceof Bible) {
				q = CommonQueries.getBookGroupsByBibleId(e.getId(), node);
				if (App.getContext().getModelCache().contains(q)) {
					fillNode(node, App.getContext().getModelCache().get(q));
					q = null;
				}
			} else
			if (e instanceof Book) {
				Book book = getBook(e);
				if (book.getChapters() != null) {
					for (int i = 1; i <= book.getChapters(); i++) {
						node.add(new DefaultMutableTreeNode(new ChapterNode(book, i)));
					}
					synchronized(getRoot()) {
						treeModel.nodeChanged(node);
					}
				}
			} else
			if (e instanceof BookGroup) {
				q = CommonQueries.getBooksByGroupId(e.getId(), node);
				if (App.getContext().getModelCache().contains(q)) {
					fillNode(node, App.getContext().getModelCache().get(q));
					q = null;
				}
			}
			if (q != null) {
				cmd = App.getContext().getDataClient().createModelQueryCommand(q);
				cmd.addActionStatusListener(this);
				cmd.executeAsync();
			}
		}
	}
	
	protected Bible getBible(ModelEntity e) {
		return (Bible)e;
	}
	
	protected Book getBook(ModelEntity e) {
		return (Book)e;
	}
	
	protected BookGroup getGroup(ModelEntity e) {
		return (BookGroup)e;
	}
	
	protected void fillNode(DefaultMutableTreeNode node, List<? extends ModelEntity> list) {
		if (list != null) {
			DefaultMutableTreeNode child = null;
			for (ModelEntity e : list) {
				child = new DefaultMutableTreeNode(e);
				node.add(child);
				nodeChanged(child);
			}
			synchronized(getRoot()) {
				if (node.getUserObject() instanceof BookGroup) {
					treeModel.nodeStructureChanged((TreeNode)getRoot());
				} else {
					treeModel.nodeChanged(node);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.ActionStatusListener#actionCompleted(org.ebible.client.event.CompleteStatusEvent)
	 */
	@Override
	public void actionCompleted(CompleteStatusEvent evt) {
		if (evt.isSuccess()) {
			ModelQuery q = (ModelQuery)evt.getTagObject();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) evt.getTag();
			fillNode(node, q.getResult());
		}
	}
}