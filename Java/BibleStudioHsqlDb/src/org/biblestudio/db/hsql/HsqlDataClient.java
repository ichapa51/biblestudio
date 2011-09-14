package org.biblestudio.db.hsql;

import org.biblestudio.client.ActionStatusListener;
import org.biblestudio.client.BibleReader;
import org.biblestudio.client.BibleSource;
import org.biblestudio.client.CommonQueries;
import org.biblestudio.client.DefaultStatusListener;
import org.biblestudio.client.ImportMode;
import org.biblestudio.client.ModelQuery;
import org.biblestudio.client.SearchQuery;
import org.biblestudio.client.SearchType;
import org.biblestudio.client.db.AbstractDataClient;
import org.biblestudio.client.db.BibleDbEngine;
import org.biblestudio.model.ModelEntity;
import org.biblestudio.model.Paragraph;
import org.biblestudio.util.ReflectionUtils;

public class HsqlDataClient extends AbstractDataClient {

	protected static ActionStatusListener printStatus = new DefaultStatusListener();
	
	@Override
	public BibleDbEngine createDbEngine() {
		return new HsqlDbEngine();
	}
	
	@SuppressWarnings("unused")
	private static void importTest() throws Exception {
		String path = "C:\\Books\\Christian\\Bible\\HTML\\ES\\RV1960\\RV1960.zip";
		java.io.File file = new java.io.File(path);
		BibleReader reader = org.biblestudio.client.BibleFactory.
								getFactory().createBibleReaderFromFile(file);
		BibleSource source = new BibleSource(reader, ImportMode.REPLACE);
		HsqlDataClient client = new HsqlDataClient();
		client.createOpenCommand(null).setActionStatus(printStatus).execute();
		client.createImportCommand(source).setActionStatus(printStatus).execute();
		client.createCloseCommand(null).setActionStatus(printStatus).execute();
	}
	
	private static SearchQuery searchTest(SearchType type, String query, boolean includeHeaders) throws Exception {
		SearchQuery input = new SearchQuery(type, query);
		input.setIncludingHeaders(includeHeaders);
		input.setIgnoringAccents(true);
		HsqlDataClient client = new HsqlDataClient();
		client.createOpenCommand(null).setActionStatus(printStatus).execute();
		client.createSearchQueryCommand(input).setActionStatus(printStatus).execute();
		client.createCloseCommand(null).setActionStatus(printStatus).execute();
		return input;
	}
	
	@SuppressWarnings("unused")
	private static void searchTest() throws Exception {
		SearchQuery query = searchTest(SearchType.Auto, "Mateo 1", false);
		if (query.getResult() != null) {
			for (Paragraph p : query.getResult().getList()) {
				System.out.println("[" + p.getIdBook() + "] " + p.getChapter() + ":" + p.getVerse() + ". " + p);
			}
			System.out.println("Total: " + query.getResult().getList().size());
		}
	}
	
	private static void modelTest() throws Exception {
		ModelQuery query = CommonQueries.getBooksByBibleId(2, null);
		HsqlDataClient client = new HsqlDataClient();
		client.createOpenCommand(null).setActionStatus(printStatus).execute();
		client.createModelQueryCommand(query).setActionStatus(printStatus).execute();
		client.createCloseCommand(null).setActionStatus(printStatus).execute();
		if (query.getResult() != null) {
			for (ModelEntity e : query.getResult()) {
				System.out.println(ReflectionUtils.toPseudoJsonString(e));
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		//importTest();
		//searchTest();
		modelTest();
	}
}