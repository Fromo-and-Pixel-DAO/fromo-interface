package org.cell.froopyland_interface.utils;

/**
 * @author yozora
 * description
 * @version 1.0
 * @date 2024/04/09 16:45
 */
public class PageUtils {

    public static boolean hasNext(int total, int pageNum, int pageSize) {
        int startIndex = (pageNum - 1) * pageSize;
        if (startIndex < 0) {
            startIndex = 0;
        }
        return startIndex < total;
    }
}
