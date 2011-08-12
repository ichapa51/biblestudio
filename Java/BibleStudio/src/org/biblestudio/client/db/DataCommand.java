package org.biblestudio.client.db;

import org.biblestudio.client.AbstractCommand;
import org.biblestudio.client.Tag;

public abstract class DataCommand<E extends Tag> extends AbstractCommand<E> {

	protected BibleDbEngine dbEngine;
	
	public DataCommand(BibleDbEngine engine) {
		dbEngine = engine;
	}
	
	public DataCommand(BibleDbEngine engine, E input) {
		super(input);
		dbEngine = engine;
	}
}
