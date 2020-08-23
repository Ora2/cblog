package co.happymeal.cblog.data.dos;

import co.happymeal.cblog.pojo.Comic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ComicDo {

    /**
     * 分页查询comic集合
     * @param start
     * @param nums
     * @param tagIds
     * @return
     */
    List<Comic> selectByPage(int start, int nums, List<Integer> tagIds);

    /**
     * 分页查询所有有tag的comic集合,
     * @param start
     * @param nums
     * @return
     */
    List<Comic> selectByPageWithAllTag(int start, int nums);

    /**
     * 根据comicId查询所有标签id
     * @param comicId
     * @return
     */
    List<Integer> selectTagIds(Integer comicId);

    List<String> selectAllAuthors();

    /**
     * Insert awesome to her.
     * @param comic You know what is this.
     * @return  You also know this too.
     */
    int insert(Comic comic);

    /**
     * Never mind this.
     * @param comic Ha ha ha!
     * @return em hmm.
     */
    int update(Comic comic);

    /**
     * 删除所有图片时所需的方法，勿用<br/>
     * <b>返回所有url</b>
     * @return 所有url的list
     */
    List<String> selectAll();

    /**
     * Delete comic record by comicId.
     * @param comic
     * @return
     */
    int remove(Comic comic);

    /**
     * 根据comicId查询所有关联Comic
     * @param id
     * @return
     */
    List<Comic> selectRelatedComicById(Integer id);

    /**
     * 根据comicId查询所有关联ComicIDs
     * @param id
     * @return
     */
    List<Integer> selectRelatedIdsById(Integer id);

    /**
     * 根据文件夹地址查询comic
     * @param location
     * @return
     */
    Comic selectComicByLocation(String location);

    /**
     * 绑定关联
     * @param from 主体comicId
     * @param to 被绑定的comicId
     * @return
     */
    int bindRalation(Integer from, Integer to);

    /**
     * 解绑关联
     * @param from 主体comicId
     * @param to 被绑定的comicId
     * @return
     */
    int removeRalation(Integer from, Integer to);

    /**
     * 返回库内漫画数量
     * @return
     */
    Integer selectComicTotalNum();

    /**
     * 根据comicId删除所有相关的关联
     * @param comic
     * @return
     */
    int removeAllRelationsByComicId(Comic comic);

    /**
     * 根据搜索串查询list，tag无效
     * @param start
     * @param nums
     * @param strings 根据空格分隔的搜索串
     * @return
     */
    List<Comic> selectByPageWithSearchString(int start, Integer nums, String[] strings);


    List<Comic> repairImg();
    int repair(Comic comic);
}
