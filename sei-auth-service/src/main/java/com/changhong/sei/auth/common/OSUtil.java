package com.changhong.sei.auth.common;

import org.apache.commons.lang3.StringUtils;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-06-22 17:25
 */
public final class OSUtil {

    private final static String[] BROWSER = new String[] {
            "Chrome", "Firefox", "Microsoft Edge", "Safari", "Opera"
    };
    private final static String[] OPERATING_SYSTEM = new String[]{
            "Android", "Linux", "Mac OS X", "Ubuntu", "Windows 10", "Windows 8", "Windows 7", "Windows XP", "Windows Vista"
    };

    public static String simplifyOperatingSystem(String operatingSystem) {
        for (String b : OPERATING_SYSTEM) {
            if (StringUtils.containsIgnoreCase(operatingSystem, b)) {
                return b;
            }
        }
        return operatingSystem;
    }

    public static String simplifyBrowser(String browser) {
        for (String b : BROWSER) {
            if (StringUtils.containsIgnoreCase(browser, b)) {
                return b;
            }
        }
        return browser;
    }
}
