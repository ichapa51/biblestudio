package org.biblestudio.ui.swing.model;

import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.biblestudio.client.Command;
import org.biblestudio.client.CommonQueries;
import org.biblestudio.client.ModelQuery;
import org.biblestudio.model.Bible;
import org.biblestudio.model.Book;
import org.biblestudio.model.BookGroup;
import org.biblestudio.model.ModelEntity;
import org.biblestudio.ui.swing.App;
/**
 * 
 * @author Israel Chapa
 * @since 13/09/2011
 */
public class EntityTreeNode implements TreeNode {

	boolean isLoaded;
	ModelEntity entity;
	DefaultMutableTreeNode node;
	
	public EntityTreeNode() {
		isLoaded = true;
		node = new DefaultMutableTreeNode();
	}
	
	public EntityTreeNode(ModelEntity e) {
		isLoaded = false;
		this.entity = e;
		node = new DefaultMutableTreeNode(this);
	}
	
	public ModelEntity getModelEntity() {
		return this.entity;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildAt(int)
	 */
	@Override
	public TreeNode getChildAt(int childIndex) {
		if (!isLoaded) {
			loadChildren();
		}
		if (this.node.getUserObject() != null) {
			return (TreeNode)((DefaultMutableTreeNode)node.getChildAt(childIndex)).getUserObject();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildCount()
	 */
	@Override
	public int getChildCount() {
		if (!isLoaded) {
			loadChildren();
		}
		return node.getChildCount();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getParent()
	 */
	@Override
	public TreeNode getParent() {
		if (node.getParent() != null) {
			return (TreeNode)((DefaultMutableTreeNode)node.getParent()).getUserObject();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
	 */
	@Override
	public int getIndex(TreeNode node) {
		if (!isLoaded) {
			loadChildren();
		}
		if (this.node.getUserObject() != null) {
			EntityTreeNode eNode = (EntityTreeNode)node;
			return this.node.getIndex(eNode.node);
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getAllowsChildren()
	 */
	@Override
	public boolean getAllowsChildren() {
		if (entity != null) {
			if (entity instanceof ChapterNode) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#isLeaf()
	 */
	@Override
	public boolean isLeaf() {
		if (!isLoaded) {
			loadChildren();
		}
		return node.getChildCount() == 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#children()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Enumeration children() {
		return node.children();
	}
	
	@Override
	public String toString() {
		if (entity != null) {
			return entity.toString();
		}
		return null;
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
			node.removeAllChildren();
			EntityTreeNode child = null;
			for (ModelEntity e : list) {
				child = new EntityTreeNode(e);
				node.add(child.node);
			}
		}
	}
	
	protected void loadChildren() {
		ModelEntity e = entity;
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
						EntityTreeNode child = new EntityTreeNode(new ChapterNode(book, i));
						node.add(child.node);
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
				cmd.execute();
				if (cmd.getLastError() == null) {
					fillNode(node, q.getResult());
				}
			}
		}
		isLoaded = true;
	}
}
