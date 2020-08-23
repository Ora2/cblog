package co.happymeal.cblog.util;

public abstract class Constant {

    /**
     * 分页查询每页记录数
     */
    public static final Integer RECORD_PER_PAGE = 20;

    /**
     * 缩略图后缀
     */
    public static final String THUMB_SUBFIX = "_220x300";

    public static final String FASTDFS_PREFIX = "http://192.168.128.3:8080/";

    public static final String SUCCESS = "success";
    public static final String Fail = "fail";

    /**
     * 根据大图url生成成缩略图url。
     * @param url 大图url
     * @return
     */
    public static String toThumbUrl(String url) {
        return url.substring(0, url.lastIndexOf(".")) + THUMB_SUBFIX + url.substring(url.lastIndexOf("."));
    }

    /**
     * 返回可访问的地址
     * @param url
     * @return
     */
    public static String completUrl(String url) {
        return FASTDFS_PREFIX + url;
    }

    /**
     * 将服务器地址前缀去除
     * @param url
     * @return
     */
    public static String removePrefix(String url) {
        return url.replaceAll(FASTDFS_PREFIX, "");
    }
}
