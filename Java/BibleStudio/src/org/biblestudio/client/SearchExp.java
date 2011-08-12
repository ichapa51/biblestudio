package org.biblestudio.client;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.biblestudio.util.StringUtils;

/**
 * 
 * @author Israel Chapa
 * Creation Date: 20/07/2011
 */
public class SearchExp {

	private static final Pattern SEARCHWORD_TOKEN = Pattern.compile("[0-9a-zA-ZÁÉÍÓÚáéíóúÑñ?*]+");
	private static final Pattern BOOK_BRAKET_TOKEN = Pattern.compile("\\[[0-9a-zA-ZÁÉÍÓÚáéíóúÑñ_ ]+\\]");
	private static final Pattern BOOK_WORD_TOKEN = Pattern.compile("[0-9a-zA-ZÁÉÍÓÚáéíóúÑñ_]+\\.?");
	private static final Pattern INTEGER_TOKEN = Pattern.compile("[0-9]+");
	private static final Pattern STRING_TOKEN = Pattern.compile("\"[^\\t\\n\\f\\r\"_%]+\"");
	private static final Pattern DIV_TOKEN = Pattern.compile("\"[0-9a-zA-ZÁÉÍÓÚáéíóúÑñ_ ]+\"|[0-9]+");
	private static final Pattern PIPE_TOKEN = Pattern.compile("\\|");
	private static final Pattern ATBIBLE_TOKEN = Pattern.compile("@[0-9a-zA-ZÁÉÍÓÚáéíóúÑñ_][0-9a-zA-ZÁÉÍÓÚáéíóúÑñ_.]+");
	
	private SearchType type = SearchType.Auto;
	private String inputText;
	private Passage passage;
	private Keyword keyword;
	
	public SearchExp(String inputText) {
		this.inputText = inputText;
	}
	
	public SearchExp(SearchType type, String inputText) {
		this.type = type;
		this.inputText = inputText;
	}
	
	public SearchType getType() {
		return type;
	}

	public String getInputText() {
		return inputText;
	}
	
	public Passage getPassage() {
		return this.passage;
	}
	
	public Keyword getKeyword() {
		return this.keyword;
	}
	
	public void compile() throws Exception {
		switch(type) {
			case Passage:
				compilePassage();
				break;
			case Keyword:
				compileKeyword();
				break;
			default:
				compileAuto();
		}
	}
	
	public static class Passage {
		private String idBible;
		private BookPsg[] passages;
		
		public Passage(String idBible, BookPsg[] passages) {
			this.idBible = idBible;
			this.passages = passages;
		}
		
		public String getIdBible() {
			return idBible;
		}
		public BookPsg[] getPassages() {
			return passages;
		}
		@Override
		public String toString() {
			StringBuilder buffer = new StringBuilder();
			if (passages != null && passages.length > 0) {
				buffer.append(passages[0]);
				for (int i = 1; i < passages.length; i++) {
					buffer.append("; ").append(passages[i]);
				}
			}
			if (idBible != null) {
				if (passages != null && passages.length > 1) {
					buffer.append(";");
				}
				buffer.append("@").append(idBible);
			}
			return buffer.toString();
		}
	}
	
	public static class BookPsg {
		private String idBook;
		private BookIndex[] indexes;

		public BookPsg(String idBook, BookIndex[] indexes) {
			this.idBook = idBook;
			this.indexes = indexes;
		}
		
		public String getIdBook() {
			return idBook;
		}
		public BookIndex[] getIndexes() {
			return indexes;
		}
		public boolean hasIndexes() {
			return indexes != null && indexes.length > 0;
		}
		@Override
		public String toString() {
			StringBuilder buffer = new StringBuilder();
			if (idBook != null) {
				if (idBook.matches(INTEGER_TOKEN.pattern())) {
					buffer.append("[" + idBook + "]");
				} else {
					buffer.append(idBook);
				}
			}
			if (indexes != null && indexes.length > 0) {
				buffer.append(" ");
				buffer.append(indexes[0]);
				for (int i = 1; i < indexes.length; i++) {
					buffer.append(";").append(indexes[i]);
				}
			}
			return buffer.toString();
		}
	}
	
	public static class BookIndex {
		private boolean isPageIndex = false;
		private String divKey;
		private IntRange[] intRange;
		
		public BookIndex(boolean isPage, String divKey) {
			this.isPageIndex = isPage;
			this.divKey = divKey;
		}
		
		public BookIndex(boolean isPage, IntRange[] intRange) {
			this.isPageIndex = isPage;
			this.intRange = intRange;
		}

		public BookIndex(boolean isPage, String divKey, IntRange[] intRange) {
			this.isPageIndex = isPage;
			this.divKey = divKey;
			this.intRange = intRange;
		}
		
		public boolean isPageIndex() {
			return isPageIndex;
		}
		public String getDivKey() {
			return divKey;
		}
		public IntRange[] getIntRange() {
			return intRange;
		}
		public boolean hasIntRange() {
			return intRange != null && intRange.length > 0;
		}
		@Override
		public String toString() {
			StringBuilder buffer = new StringBuilder();
			if (isPageIndex) {
				buffer.append("#");
			}
			if (divKey != null) {
				buffer.append(divKey);
			}
			if (hasIntRange()) {
				if (!isPageIndex) {
					buffer.append(":");
				}
				buffer.append(intRange[0]);
				for (int i = 1; i < intRange.length; i++) {
					buffer.append(",").append(intRange[i]);
				}
			}
			return buffer.toString();
		}
	}
	
	public static class IntRange {
		private int leftInt;
		private Integer rightInt;
		
		public IntRange(int leftInt) {
			this.leftInt = leftInt;
		}
		
		public IntRange(int leftInt, Integer rightInt) {
			this.leftInt = leftInt;
			this.rightInt = rightInt;
		}
		
		public int getLeftInt() {
			return leftInt;
		}
		public Integer getRightInt() {
			return rightInt;
		}
		public boolean hasRightInt() {
			return rightInt != null;
		}
		@Override
		public String toString() {
			return leftInt + ((rightInt == null) ? StringUtils.EMPTY : ("-" + rightInt));
		}
	}
	
	public static class Keyword {
		private String idBible;
		private KeywordToken tokens[];

		public Keyword(String idBible, KeywordToken tokens[]) {
			this.idBible = idBible;
			this.tokens = tokens;
		}

		public String getIdBible() {
			return idBible;
		}

		public KeywordToken[] getTokens() {
			return tokens;
		}
		
		private String tokenToString(KeywordToken token) {
			if (token.getType().equals(KeywordType.QuotedWord)) {
				return "\"" + token + "\"";
			}
			return token.toString();
		}
		
		@Override
		public String toString() {
			StringBuilder buffer = new StringBuilder();
			if (tokens != null && tokens.length > 0) {
				buffer.append(tokenToString(tokens[0]));
				for (int i = 1; i < tokens.length; i++) {
					buffer.append(" ").append(tokenToString(tokens[i]));
				}
			}
			if (idBible != null) {
				buffer.append("@").append(idBible);
			}
			return buffer.toString();
		}
	}
	
	public static class KeywordToken {
		public static final String AND = "&";
		public static final String OR = "|";
		
		private KeywordType type;
		private String text;
		
		public KeywordToken(KeywordType type, String text) {
			this.type = type;
			this.text = text;
		}

		public KeywordType getType() {
			return type;
		}

		public String getText() {
			return text;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
	
	public static enum KeywordType {
		Word,
		QuotedWord,
		Operator
	}
	
	private void compilePassage() throws Exception {
		String idBible = null;
		String[] passageExps = inputText.split(";");
		int limit = passageExps.length;
		if (passageExps.length > 1) {
			if (passageExps[passageExps.length - 1].matches(ATBIBLE_TOKEN.pattern())) {
				idBible = passageExps[passageExps.length - 1].substring(1);
				limit = passageExps.length - 1;
			}
		} else {
			String passage = passageExps[0];
			int i = passage.indexOf('@');
			if (i > 0) {
				if (i != passage.lastIndexOf('@')) {
					throw new Exception("Invalid @bible");
				}
				String bibleExp = passage.substring(i);
				if (bibleExp.matches(ATBIBLE_TOKEN.pattern())) {
					idBible = bibleExp.substring(1);
					passage = passage.substring(0, i);
				}
			}
		}
		BookPsg[] passages = new BookPsg[limit];
		for (int i = 0; i < limit; i++) {
			passages[i] = parseBookPassage(passageExps[i].trim(), i== 0, i == (limit - 1));
		}
		if (limit > 1) {
			String idBook = passages[0].idBook;
			for (int i = 1; i < limit; i++) {
				if (passages[i].idBook == null) {
					passages[i].idBook = idBook;
				}
				idBook = passages[i].idBook;
			}
			ArrayList<BookPsg> list = new ArrayList<BookPsg>();
			idBook = passages[0].idBook;
			list.add(passages[0]);
			for (int i = 1; i < limit; i++) {
				if (passages[i].idBook == idBook) {
					if (passages[i].indexes != null && passages[i].indexes.length > 0) {
						BookIndex[] old = list.get(list.size() - 1).indexes;
						if (old == null) {
							old = new BookIndex[0];
						}
						BookIndex newIndex = passages[i].indexes[0];
						list.get(list.size() - 1).indexes = new BookIndex[old.length + 1];
						System.arraycopy(old, 0, list.get(list.size() - 1).indexes, 0, old.length);
						list.get(list.size() - 1).indexes[old.length] = newIndex;
					}
				} else {
					idBook = passages[i].idBook;
					list.add(passages[i]);
				}
			}
			passages = list.toArray(new BookPsg[list.size()]);
		}
		this.passage = new Passage(idBible, passages);
	}
	
	private BookPsg parseBookPassage(String passage, boolean isFirst, boolean isLast) throws Exception {
		String idBook = null;
		BookIndex index = null;
		BookIndex[] indexes = null;
		String divKey = null;
		IntRange[] intRanges = null;
		if (!isFirst && StringUtils.isIntegerNumber(passage.trim())) {
			return new BookPsg(null, new BookIndex[] {new BookIndex(false, passage.trim())});
		}
		Scanner scanner = new Scanner(passage.trim());
		String delim = "[ \t]+";
		scanner.useDelimiter(delim);
		String find = null;
		if ((find = scanner.findWithinHorizon("\\A" + BOOK_BRAKET_TOKEN, 0)) != null) {
			idBook = find.substring(1, find.length() - 1);
		} else {
			String search = "\\A[ \t]*[0-9]+[ ]*[.:].+";
			if ((find = scanner.findWithinHorizon(search, 0)) != null) {
				// There's no book
				scanner = new Scanner(passage);
				scanner.useDelimiter(delim);
			} else {
				StringBuilder buffer = new StringBuilder();
				if (scanner.hasNext(BOOK_WORD_TOKEN)) {
					buffer.append(scanner.next());
					scanner = new Scanner(scanner.findWithinHorizon(".*", 0));
					scanner.useDelimiter(delim);
				}
				while (scanner.hasNext(BOOK_WORD_TOKEN)) {
					if ((find = scanner.findWithinHorizon(search, 0)) != null) {
						scanner = new Scanner(find);
						scanner.useDelimiter(delim);
					} else {
						buffer.append(" ").append(scanner.next());
						scanner = new Scanner(scanner.findWithinHorizon(".*", 0));
						scanner.useDelimiter(delim);
					}
				}
				if (buffer.length() > 0) {
					idBook = buffer.toString();
				}
			}
		}
		if (isFirst && idBook == null) {
			throw new Exception("Passage must provide a book");
		}
		if ((find = scanner.findWithinHorizon("\\A[ \t]*#.+", 0)) != null) {
			intRanges = parseIntRanges(find.trim().substring(1), isLast);
			index = new BookIndex(true, intRanges);
		} else {
			if ((find = scanner.findWithinHorizon("\\A[ \t]*" + DIV_TOKEN.pattern() + ".*", 0)) != null) {
				String temp = find;
				scanner = new Scanner(find);
				scanner.useDelimiter(delim);
				divKey = scanner.findWithinHorizon(DIV_TOKEN, 0);
				scanner = new Scanner(temp.substring(divKey.length()));
				scanner.useDelimiter(delim);
				if ((find = scanner.findWithinHorizon("\\A[ \t]*[.:].+", 0)) != null) {
					intRanges = parseIntRanges(find.trim().substring(1), isLast);
				}
				index = new BookIndex(false, divKey, intRanges);
			} else if (scanner.hasNext()) {
				throw new Exception("Invalid passage");
			}
		}
		if (index != null) {
			indexes = new BookIndex[] {index};
		}
		return new BookPsg(idBook, indexes);
	}
	
	private IntRange[] parseIntRanges(String input, boolean isLast) {
		if (StringUtils.isNullOrEmpty(input)) {
			return null;
		}
		input = input.trim();
		if (isLast && input.endsWith(".")) {
			input = input.substring(0, input.length() - 1);
		}
		String[] arr = input.split(",");
		IntRange[] intRange = new IntRange[arr.length];
		int n, k;
		Integer right;
		for (int i = 0; i < arr.length; i++) {
			k = arr[i].indexOf('-');
			if (k == -1) {
				n = Integer.parseInt(arr[i]);
				right = null;
			} else {
				n = Integer.parseInt(arr[i].substring(0, k));
				right = Integer.parseInt(arr[i].substring(k + 1));
			}
			intRange[i] = new IntRange(n, right);
		}
		return intRange;
	}
	
	private void compileKeyword() throws Exception {
		String idBible = null;
		ArrayList<KeywordToken> tokens = new ArrayList<KeywordToken>();
		Scanner scanner = new Scanner(inputText.trim());
		String delim = "[ \t]+";
		scanner.useDelimiter(delim);
		String find = null, temp = null;
		if (scanner.hasNext(SEARCHWORD_TOKEN)) {
			find = scanner.next();
			tokens.add(new KeywordToken(KeywordType.Word, find));
			scanner = new Scanner(scanner.findWithinHorizon(".*", 0));
			scanner.useDelimiter(delim);
		} else if ((find = scanner.findWithinHorizon("\\A" + STRING_TOKEN.pattern() + ".*", 0)) != null) {
			scanner = new Scanner(find);
			scanner.useDelimiter(delim);
			temp = scanner.findWithinHorizon(STRING_TOKEN, 0);
			tokens.add(new KeywordToken(KeywordType.QuotedWord, temp.substring(1, temp.length() - 1)));
		} else {
			throw new Exception("Invalid first keyword");
		}
		int index = 0;
		while(scanner.hasNext()) {
			if (scanner.hasNext(PIPE_TOKEN)) {
				find = scanner.next();
				tokens.add(new KeywordToken(KeywordType.Operator, find));
				scanner = new Scanner(scanner.findWithinHorizon(".*", 0));
				scanner.useDelimiter(delim);
			} else if (scanner.hasNext(KeywordToken.AND)) {
				find = scanner.next();
				tokens.add(new KeywordToken(KeywordType.Operator, find));
				scanner = new Scanner(scanner.findWithinHorizon(".*", 0));
				scanner.useDelimiter(delim);
			} else if (scanner.hasNext(SEARCHWORD_TOKEN)) {
				if (!tokens.get(index).getType().equals(KeywordType.Operator)) {
					tokens.add(new KeywordToken(KeywordType.Operator, KeywordToken.AND));
					index++;
				}
				tokens.add(new KeywordToken(KeywordType.Word, scanner.next()));
				scanner = new Scanner(scanner.findWithinHorizon(".*", 0));
				scanner.useDelimiter(delim);
			} else if ((find = scanner.findWithinHorizon("\\A[ \t]*" + STRING_TOKEN.pattern() + ".*", 0)) != null) {
				if (!tokens.get(index).getType().equals(KeywordType.Operator)) {
					tokens.add(new KeywordToken(KeywordType.Operator, KeywordToken.AND));
					index++;
				}
				scanner = new Scanner(find);
				scanner.useDelimiter(delim);
				temp = scanner.findWithinHorizon(STRING_TOKEN, 0);
				tokens.add(new KeywordToken(KeywordType.QuotedWord, temp.substring(1, temp.length() - 1)));
			} else if (scanner.hasNext(ATBIBLE_TOKEN)) {
				idBible = scanner.next().substring(1);
				if (scanner.hasNext()) {
					throw new Exception("Invalid bible");
				}
			} else {
				throw new Exception("Invalid keyword search");
			}
			index++;
		}
		KeywordToken[] tokenArr = tokens.toArray(new KeywordToken[tokens.size()]);
		this.keyword = new Keyword(idBible, tokenArr);
	}

	private void compileAuto() throws Exception {
		boolean failed = false;
		try {
			compilePassage();
			type = SearchType.Passage;
			System.out.println(this.passage);
		} catch (Exception e) {
			failed = true;
		}
		try {
			compileKeyword();
			System.out.println(this.keyword);
			if (failed) {
				type = SearchType.Keyword;
			} else {
				type = SearchType.Auto;
			}
		} catch (Exception e) {
			if (failed) {
				throw e;
			}
		}
	}
}
