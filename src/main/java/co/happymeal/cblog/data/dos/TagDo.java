package co.happymeal.cblog.data.dos;

import co.happymeal.cblog.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagDo {

    public List<Tag> selectAllTags();

    /**
     * 返回没有tag的comic数量
     * @return
     */
    public Integer selectNoTagNum();

    public int insert(Tag tag);

    public int insertTagComicPair(int tid, int cid);

    public int removeAllTagPairByComicId(int cid);

    public int removeAllTagsByTagId(List<Integer> list);

    public int removeAllTagComicPairByTagId(List<Integer> list);

    public int removeUselessTag();

    public List<Tag> selectByPid(int pid);

    public List<Tag> selectTagsWithChildren();

    public int updateTagCascade(String tagName, String dropNode, String position);

    int removePidByTagId(List<Integer> list);

}
