package co.happymeal.cblog.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author lyd
 */
@Data
public class SearchCriteria {

    private Integer page;
    private Integer itmPerPage;
    private String searchString;
    private Boolean allTag;
    private List<String> checkedTagList;
}
