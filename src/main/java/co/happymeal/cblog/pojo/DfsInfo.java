package co.happymeal.cblog.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author lyd
 */
@Data
public class DfsInfo {

    private String type;
    private Date date;
    private String url;

    public DfsInfo(String url) {
        this.date = new Date();
        this.url = url;
    }

    public DfsInfo(String url, String type) {
        this.date = new Date();
        this.url = url;
        this.type = type;
    }
}
