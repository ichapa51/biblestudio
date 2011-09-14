package org.biblestudio.util;
/**
 * General Utilities
 * @author Israel Chapa
 * @since 12/09/2011
 */
public final class Utils {

	private Utils() {
		
	}
	
	public static boolean isWindowsOs() {
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Win")) {
			return true;
		}
		return false;
	}
	
	public static boolean isMacOs() {
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Mac ")) {
			return true;
		}
		if (osName.startsWith("Darwin")) {
			return true;
		}
		return false;
	}
}
