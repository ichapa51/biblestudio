package org.biblestudio.ui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.biblestudio.model.ModelEntity;
import org.biblestudio.ui.swing.model.EntityTreeModel;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 09/08/2011
 */
@SuppressWarnings("serial")
public class ContentsPanel extends JPanel {

	private ModelEntity lastSingleClickedEntity;
	private ModelEntity lastDoubleClickedEntity;
	JScrollPane scroll;
	EntityTreeModel model;
	JTree tree;
	
	public ContentsPanel() {
		super(new BorderLayout());
		model = new EntityTreeModel();
		tree = new JTree(model);
		MouseListener ml = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				if (path != null && path.getLastPathComponent() != null) {
					ModelEntity entity = model.getModelEntity(path.getLastPathComponent());
					if (entity != null) {
						if (e.getClickCount() == 1) {
							setLastSingleClickedEntity(entity);
						} else if (e.getClickCount() == 2) {
							setLastDoubleClickedEntity(entity);
						}
					}
				}
			}
		};
		tree.addMouseListener(ml);
		tree.setShowsRootHandles(true);
		tree.setRootVisible(false);
		tree.setExpandsSelectedPaths(true);
		scroll = new JScrollPane(tree);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setMinimumSize(new Dimension(150,300));
		add(scroll, BorderLayout.CENTER);
	}
	
	public void setModelRoot(ModelEntity root) {
		model.setRootEntity(root);
		if (root != null) {
			tree.expandPath(new TreePath(model.getRoot()));
		}
	}
	
	public ModelEntity getModelRoot() {
		return model.getRootEntity();
	}

	protected void setLastSingleClickedEntity(ModelEntity e) {
		ModelEntity old = this.lastSingleClickedEntity;
		this.lastSingleClickedEntity = e;
		this.firePropertyChange("LastSingleClickedEntity", old, e);
	}

	public ModelEntity getLastSingleClickedEntity() {
		return lastSingleClickedEntity;
	}

	protected void setLastDoubleClickedEntity(ModelEntity e) {
		ModelEntity old = this.lastDoubleClickedEntity;
		this.lastDoubleClickedEntity = e;
		this.firePropertyChange("LastDoubleClickedEntity", old, e);
	}

	public ModelEntity getLastDoubleClickedEntity() {
		return lastDoubleClickedEntity;
	}
}
