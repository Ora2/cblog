package co.happymeal.cblog.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author lyd
 */
@Data
public class Tag {

    private Integer id;
    private String name;
    private Integer num;
    private Integer pid;
    private List<Tag> childrenList;

    public Tag(String name) {
        this.name = name;
    }
}
