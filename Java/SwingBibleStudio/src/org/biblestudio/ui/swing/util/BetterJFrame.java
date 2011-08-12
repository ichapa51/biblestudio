package org.biblestudio.ui.swing.util;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 28/07/2011
 */
@SuppressWarnings("serial")
public class BetterJFrame extends JFrame {

	private Rectangle maxBounds;
	
	public BetterJFrame() {
		super();
		setBetterSize();
	}
	
	public BetterJFrame(String title) {
		super(title);
		setBetterSize();
	}
	
	protected void setBetterSize() {
		//Make the big window be indented 40 pixels from each edge of the screen.
        int inset = 40;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                  screenSize.width  - inset*2,
                  screenSize.height - inset*2);
	}
	
	@Override
	public Rectangle getMaximizedBounds()
	{
	    return(maxBounds);
	}

	@Override
	public synchronized void setMaximizedBounds(Rectangle maxBounds)
	{
	    this.maxBounds = maxBounds;
	    super.setMaximizedBounds(maxBounds);
	}

	@Override
	public synchronized void setExtendedState(int state)
	{       
	    if (maxBounds == null &&
	        (state & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH)
	    {
	        Insets screenInsets = getToolkit().getScreenInsets(getGraphicsConfiguration());         
	        Rectangle screenSize = getGraphicsConfiguration().getBounds();
	        Rectangle maxBounds = new Rectangle(screenInsets.left + screenSize.x, 
	                                    screenInsets.top + screenSize.y, 
	                                    screenSize.x + screenSize.width - screenInsets.right - screenInsets.left,
	                                    screenSize.y + screenSize.height - screenInsets.bottom - screenInsets.top);
	        super.setMaximizedBounds(maxBounds);
	    }

	    super.setExtendedState(state);
	}
}
