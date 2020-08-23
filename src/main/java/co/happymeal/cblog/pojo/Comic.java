package co.happymeal.cblog.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author lyd
 */
@Data
public class Comic {

    @JSONField
    private Integer id;
    private String name;
    private String author;
    private String url;
    private String thumb;
    private String location;
    private Float rate;
    private List<String> tags;
    private Date date;
}
