package org.biblestudio.util;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public final class StringUtils {

	public static final String EMPTY = "";
	
	private StringUtils() {
		
	}
	
	public static boolean isNullOrEmpty(String s)
	{
		if (s == null) {
			return true;
		}
		return EMPTY.equals(s);
	}
	
	public static String addSuffix(String text, String suffix, boolean caseSensitive)
	{
		String testText = (caseSensitive) ? text : text.toLowerCase();
		String testSuffix = (caseSensitive) ? suffix : suffix.toLowerCase();
		String result = text;
		if (testText.endsWith(testSuffix)) {
			result = text.substring(0, text.length() - suffix.length()) + suffix;
		} else {
			result = text + suffix;
		}
		return result;
	}

	public static String removePrefix(String text, String prefix, boolean caseSensitive)
	{
		String testText = (caseSensitive) ? text : text.toLowerCase();
		String testPrefix = (caseSensitive) ? prefix : prefix.toLowerCase();
		String result = text;
		if (testText.length() > testPrefix.length() && testText.startsWith(testPrefix)) {
			result = text.substring(prefix.length());
		}
		return result;
	}

	public static String removeSuffix(String text, String suffix, boolean caseSensitive)
	{
		String testText = (caseSensitive) ? text : text.toLowerCase();
		String testSuffix = (caseSensitive) ? suffix : suffix.toLowerCase();
		String result = text;
		if (testText.length() > testSuffix.length() && testText.endsWith(testSuffix)) {
			result = text.substring(0, text.length() - suffix.length());
		}
		return result;
	}

	public static String join(List<?> array, String sep)
	{
		StringBuffer buffer = new StringBuffer();
		if(array != null && array.size() > 0) {
			buffer.append(array.get(0));
			for(int i=1; i<array.size(); i++) {
				buffer.append(sep);
				buffer.append(array.get(i).toString());
			}
		}
		return buffer.toString();
	}

	public static String join(String[] array, String sep)
	{
		return join(array, sep, 0);
	}

	public static String join(String[] array, String sep, int beginIndex)
	{
		StringBuffer buffer = new StringBuffer();
		if(array != null && array.length > beginIndex) {
			buffer.append(array[beginIndex]);
			for(int i = beginIndex + 1; i < array.length; i++) {
				buffer.append(sep);
				buffer.append(array[i]);
			}
		}
		return buffer.toString();
	}
	
	public static String firstCharToUpperCase(String text)
	{
		if (text.length() > 0) {
			return text.substring(0,1).toUpperCase()+ text.substring(1,text.length());
		}
		return text;
	}

	public static String firstCharToLowerCase(String text)
	{
		if (text.length() > 0) {
			return text.substring(0,1).toLowerCase()+ text.substring(1,text.length());
		}
		return text;
	}
	
	 /**
     * Abbreviates a string to a specified length and then adds an ellipsis
     * if the input is greater than the maxWidth. Example input:
     * <pre>
     *      user1@jivesoftware.com/home
     * </pre>
     * and a maximum length of 20 characters, the abbreviate method will return:
     * <pre>
     *      user1@jivesoftware.c...
     * </pre>
     * @param str the String to abbreviate.
     * @param maxWidth the maximum size of the string, minus the ellipsis.
     * @return the abbreviated String, or <tt>null</tt> if the string was <tt>null</tt>.
     */
    public static String abbreviate(String str, int maxWidth) {
        if (null == str) {
            return null;
        }

        if (str.length() <= maxWidth) {
            return str;
        }

        return str.substring(0, maxWidth) + "...";
    }
    
    /**
     * Replaces all instances of oldString with newString in string.
     *
     * @param string the String to search to perform replacements on.
     * @param oldString the String that should be replaced by newString.
     * @param newString the String that will replace all instances of oldString.
     * @return a String will all instances of oldString replaced by newString.
     */
    public static String replace(String string, String oldString, String newString) {
        if (string == null) {
            return null;
        }
        int i = 0;
        // Make sure that oldString appears at least once before doing any processing.
        if ((i = string.indexOf(oldString, i)) >= 0) {
            // Use char []'s, as they are more efficient to deal with.
            char[] string2 = string.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuilder buf = new StringBuilder(string2.length);
            buf.append(string2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            // Replace all remaining instances of oldString with newString.
            while ((i = string.indexOf(oldString, i)) > 0) {
                buf.append(string2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(string2, j, string2.length - j);
            return buf.toString();
        }
        return string;
    }
    
    private static final char[] zeroArray =
        "0000000000000000000000000000000000000000000000000000000000000000".toCharArray();

	/**
	 * Pads the supplied String with 0's to the specified length and returns
	 * the result as a new String. For example, if the initial String is
	 * "9999" and the desired length is 8, the result would be "00009999".
	 * This type of padding is useful for creating numerical values that need
	 * to be stored and sorted as character data. Note: the current
	 * implementation of this method allows for a maximum <tt>length</tt> of
	 * 64.
	 *
	 * @param string the original String to pad.
	 * @param length the desired length of the new padded String.
	 * @return a new String padded with the required number of 0's.
	 */
	public static String zeroPadString(String string, int length) {
	    if (string == null || string.length() > length) {
	        return string;
	    }
	    StringBuilder buf = new StringBuilder(length);
	    buf.append(zeroArray, 0, length - string.length()).append(string);
	    return buf.toString();
	}
	
	 /**
     * Escapes all necessary characters in the String so that it can be used in SQL
     *
     * @param string the string to escape.
     * @return the string with appropriate characters escaped.
     */
    public static String escapeForSQL(String string) {
        if (string == null) {
            return null;
        }
        else if (string.length() == 0) {
            return string;
        }
        char ch;
        char[] input = string.toCharArray();
        int i = 0;
        int last = 0;
        int len = input.length;
        StringBuilder out = null;
        for (; i < len; i++) {
            ch = input[i];
            if (ch == '\'') {
                if (out == null) {
                     out = new StringBuilder(len + 2);
                }
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append('\'').append('\'');
            }
        }
        if (out == null) {
            return string;
        }
        else if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }
    
    /**
     * Converts a line of text into an array of lower case words using a
     * BreakIterator.wordInstance().<p>
     * @param text a String of text to convert into an array of words
     * @return text broken up into an array of words.
     */
    public static String[] toLowerCaseWordArray(String text) {
        if (text == null || text.length() == 0) {
            return new String[0];
        }
        List<String> wordList = new ArrayList<String>();
        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(text);
        int start = 0;
        for (int end = boundary.next(); end != BreakIterator.DONE;
             start = end, end = boundary.next()) {
            String tmp = text.substring(start, end).trim();
            // Remove characters that are not needed.
            tmp = replace(tmp, "+", "");
            tmp = replace(tmp, "/", "");
            tmp = replace(tmp, "\\", "");
            tmp = replace(tmp, "#", "");
            tmp = replace(tmp, "*", "");
            tmp = replace(tmp, ")", "");
            tmp = replace(tmp, "(", "");
            tmp = replace(tmp, "&", "");
            if (tmp.length() > 0) {
                wordList.add(tmp);
            }
        }
        return wordList.toArray(new String[wordList.size()]);
    }
    
    /*
     * Returns the first non null expression among its arguments
     */
    public static String coalesce(String... args) {
    	for (String s : args) {
    		if (s != null) {
    			return s;
    		}
    	}
    	return null;
    }
    
    public static String trimAll(String text) {
    	String[] result = text.trim().split("\\s+");
    	StringBuilder buffer = new StringBuilder(text.length());
    	String last = null;
    	for (String s : result) {
    		if (last != null && s.startsWith("<") && !last.endsWith(">")) {
    			buffer.deleteCharAt(buffer.length() - 1);
    		}
    		buffer.append(s);
    		if (!s.endsWith(">")) {
    			buffer.append(" ");
    		}
    		last = s;
    	}
    	return buffer.toString().trim();
    }
    
    public static String[] getWords(String paragraph) {
    	String p = paragraph;
    	p = replace(p, ".", EMPTY);
    	p = replace(p, ",", EMPTY);
    	p = replace(p, ":", EMPTY);
    	p = replace(p, ";", EMPTY);
    	p = replace(p, "\"", EMPTY);
    	p = replace(p, "'", EMPTY);
    	p = replace(p, "(", EMPTY);
    	p = replace(p, ")", EMPTY);
    	p = replace(p, "?", EMPTY);
    	p = replace(p, "¿", EMPTY);
    	p = replace(p, "!", EMPTY);
    	p = replace(p, "¡", EMPTY);
    	p = replace(p, "¡", EMPTY);
    	p = replace(p, "-", EMPTY);
    	p = replace(p, "*", EMPTY);
    	p = replace(p, "<", EMPTY);
    	p = replace(p, ">", EMPTY);
    	return p.split("\\s+");
    }
    
    public static String removeUpperAccents(String paragraph) {
    	String p = paragraph;
    	p = replace(p, "Á", "A");
    	p = replace(p, "É", "E");
    	p = replace(p, "Í", "I");
    	p = replace(p, "Ó", "O");
    	p = replace(p, "Ú", "U");
    	return p;
    }
    
    public static String removeLowerAccents(String paragraph) {
    	String p = paragraph;
    	p = replace(p, "á", "a");
    	p = replace(p, "é", "e");
    	p = replace(p, "í", "i");
    	p = replace(p, "ó", "o");
    	p = replace(p, "ú", "u");
    	return p;
    }
    
    public static String removeAllAccents(String paragraph) {
    	return removeUpperAccents(removeLowerAccents(paragraph));
    }
    
    public static boolean isLowerVocalChar(char c) {
    	switch (c) {
    		case 'a': return true;
    		case 'e': return true;
    		case 'i': return true;
    		case 'o': return true;
    		case 'u': return true;
    	}
    	return false;
    }
    
    public static char getLowerAccentChar(char c) {
    	switch (c) {
    		case 'a': return 'á';
    		case 'e': return 'é';
    		case 'i': return 'í';
    		case 'o': return 'ó';
    		case 'u': return 'ú';
    	}
    	return ' ';
    }
    
    public static String[] getLowerAccentWords(String word) {
    	if (isNullOrEmpty(word)) {
    		return new String[]{};
    	}
    	int count = 0;
    	String noAcc = removeLowerAccents(word);
    	for (int i = 0; i < word.length(); i++) {
    		if (isLowerVocalChar(word.charAt(i))) {
    			count++;
    		}
    	}
    	String[] words = new String[count + 1];
    	words[0] = noAcc;
    	count = 1;
    	for (int i = 0; i < noAcc.length(); i++) {
    		if (isLowerVocalChar(word.charAt(i))) {
    			words[count] = noAcc.substring(0, i) + getLowerAccentChar(word.charAt(i)) + noAcc.substring(i + 1);
    			count++;
    		}
    	}
    	return words;
    }
    
    public static boolean isIntegerNumber(String n) {
    	if (isNullOrEmpty(n)) {
    		return false;
    	}
    	return n.matches("[0-9]+");
    }
}
