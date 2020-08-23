package co.happymeal.cblog.service;

import co.happymeal.cblog.data.dos.ComicDo;
import co.happymeal.cblog.data.dos.TagDo;
import co.happymeal.cblog.pojo.Comic;
import co.happymeal.cblog.pojo.SearchCriteria;
import co.happymeal.cblog.pojo.Tag;
import co.happymeal.cblog.util.Constant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static co.happymeal.cblog.util.Constant.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lyd
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ComicServiceImpl {

    public static final Logger LOG = LoggerFactory.getLogger(ComicServiceImpl.class);

    private static Integer TOTAL_NUM = 0;
    private static Integer DONE_NUM = 0;
    private static List<Comic> FINISH_LIST = new ArrayList<>();

    @Autowired
    private FastDfsClientServiceImpl fastDfs;

    @Autowired
    private ComicDo comicDo;
    @Autowired
    private TagDo tagDo;

    /**
     *  分页查询数据
     *
     * @param start
     *              起始位置
     * @param num
     *              每页对象数
     * @return
     *              结果集合的JSON字符串
     */
    public String loadComicByPage(Integer start, Integer num, List<String> tagNames) {

        List<Tag> tags = tagDo.selectAllTags();
        List<Integer> tagIds = new ArrayList<>();
        if (tagNames != null) {
            tagNames.forEach(name -> {
                for (Tag tag : tags) {
                    if (tag.getName().equals(name)) {
                        tagIds.add(tag.getId());
                        break;
                    }
                }
            });
        }
        List<Comic> comics = comicDo.selectByPage(start, num, tagIds);

        comics.forEach(c -> c.setThumb(toThumbUrl(c.getUrl())));
        comics.forEach(c -> {
            List<Integer> list = comicDo.selectTagIds(c.getId());
            List<String> result = new ArrayList<>();
            for (Integer tid : list) {
                for (Tag tag : tags) {
                    if (tag.getId().equals(tid)) {
                        result.add(tag.getName());
                        break;
                    }
                }
            }
            c.setTags(result);
        });
        LOG.info("loadComicByPage -> 成功，第 " + start / RECORD_PER_PAGE + " 页返回条数：" + comics.size());
        return JSON.toJSONString(comics);
    }

    public boolean updateComic(Comic comic) {
        comicDo.update(comic);
        tagDo.removeAllTagPairByComicId(comic.getId());
        insertAndUpdateNewTagByComic(comic);
        return true;
    }

    public boolean addComic(Comic comic) {
        comicDo.insert(comic);
        if (comic.getTags() != null && comic.getTags().size() > 0) {
            insertAndUpdateNewTagByComic(comic);
        }
        return true;
    }

    /**
     * 将comic中的新tag入库并更新comic_Tag对。
     * @param comic 漫画对象
     * @return 插入新标签后的所有标签集合
     */
    private void insertAndUpdateNewTagByComic(Comic comic) {
        Map<String, Integer> tags = tagDo.selectAllTags().stream().collect(Collectors.toMap(Tag::getName, Tag::getId));
        if (comic.getTags() != null) {
            comic.getTags().stream()
                    .filter(name -> !tags.containsKey(name))
                    .collect(Collectors.toList())
                    .forEach(name -> tagDo.insert(new Tag(name)));
            Map<String, Integer> newTags = tagDo.selectAllTags().stream().collect(Collectors.toMap(Tag::getName, Tag::getId));
            comic.getTags().stream()
                    .filter(newTags::containsKey)
                    .forEach(name -> tagDo.insertTagComicPair(newTags.get(name), comic.getId()));
        }
        //tagDo.removeUselessTag();
    }

    /**
     *  上传过程中动态改变本次上传数量
     * @param path 目录地址，根文件夹，下属文件不得有嵌套
     */
    public void scanFolderAndAddComic(String path) {
        synchronized (this) {
            File folder = new File(path);
            TOTAL_NUM = Objects.requireNonNull(folder.listFiles()).length;
            DONE_NUM = 0;
            FINISH_LIST.clear();
            Arrays.stream(Objects.requireNonNull(folder.listFiles())).forEach(file -> {
                String comicName = file.getName();
                while (Objects.requireNonNull(file.listFiles())[0].isDirectory()) {
                    file = Objects.requireNonNull(file.listFiles())[0];
                }
                File firstImage = Objects.requireNonNull(file.listFiles())[0];
                Comic comic = new Comic();
                comic.setName(comicName);
                comic.setDate(new Date());
                comic.setRate(3.0f);
                try {
                    comic.setUrl(fastDfs.upload(firstImage));
                } catch (Exception e) {
                    LOG.error("上传图片失败 ： " + e +" => " + comic.getName());
                }
                comic.setLocation(file.getAbsolutePath());
                if (comic.getUrl() != null && comic.getUrl().length() > 0) {
                    comicDo.insert(comic);
                    FINISH_LIST.add(comic);
                    DONE_NUM ++;
                }
            });
        }
    }

    public String getProcessState() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("percent", Integer.toString(Math.min((DONE_NUM * 100) / TOTAL_NUM, 100)));
            jsonObject.put("finishList", FINISH_LIST);
            String result = jsonObject.toJSONString();
            FINISH_LIST.clear();
            return result;
    }

    public void deleteComicById(@NotNull Comic comic) {
        fastDfs.deleteImagePair(comic.getUrl());
        tagDo.removeAllTagPairByComicId(comic.getId());
        //tagDo.removeUselessTag();
        comicDo.removeAllRelationsByComicId(comic);
        comicDo.remove(comic);
        File folder = new File(comic.getLocation());
        FileUtils.deleteQuietly(folder);
    }

    public List<Comic> loadRelations(Integer id) {
        return comicDo.selectRelatedComicById(id);
    }

    public Comic addRelation(Integer id, String location) {
        List<Integer> related = comicDo.selectRelatedIdsById(id);
        Comic target = comicDo.selectComicByLocation(location);
        if (target == null || related.contains(target.getId())) {
            return null;
        }
        comicDo.bindRalation(id, target.getId());
        return target;
    }

    public Comic removeRelation(Integer id, String location) {
        List<Integer> related = comicDo.selectRelatedIdsById(id);
        Comic target = comicDo.selectComicByLocation(location);
        if (target == null || !related.contains(target.getId())) {
            return null;
        }
        comicDo.removeRalation(id, target.getId());
        return target;
    }

    public String loadNums() {
        JSONObject jsonObject = new JSONObject();
        Integer withOutTagNum = tagDo.selectNoTagNum();
        Integer totalNum = comicDo.selectComicTotalNum();
        jsonObject.put("withOutTagNum", withOutTagNum);
        jsonObject.put("totalNum", totalNum);
        return jsonObject.toJSONString();
    }

    /**
     * 删除库中所有图片！！！
     */
    public void clearFastDfs() {
        comicDo.selectAll().forEach(url -> fastDfs.deleteImagePair(url));
    }

    /**
     * 通过搜索对象获取Comic集合
     * @param searchCriteria
     * @return
     */
    public List<Comic> selectComicList(SearchCriteria searchCriteria) {
        int start = searchCriteria.getPage() * searchCriteria.getItmPerPage();
        List<String> tagNames = searchCriteria.getCheckedTagList();
        List<Tag> tags = tagDo.selectAllTags();
        List<Integer> tagIds = new ArrayList<>();
        if (tagNames != null) {
            tagNames.forEach(name -> {
                for (Tag tag : tags) {
                    if (tag.getName().equals(name)) {
                        tagIds.add(tag.getId());
                        break;
                    }
                }
            });
        }
        List<Comic> comicList = null;
        if (searchCriteria.getSearchString() != null && searchCriteria.getSearchString().length() > 0) {
            comicList = comicDo.selectByPageWithSearchString(start, searchCriteria.getItmPerPage(),
                    searchCriteria.getSearchString().trim().split(" +"));
        }else if (searchCriteria.getAllTag()) {
            comicList = comicDo.selectByPageWithAllTag(start, searchCriteria.getItmPerPage());
        } else {
            comicList = comicDo.selectByPage(start, searchCriteria.getItmPerPage(), tagIds);
        }
        comicList.forEach(comic -> comic.setThumb(Constant.toThumbUrl(comic.getUrl())));
        comicList.forEach(c -> {
            List<Integer> list = comicDo.selectTagIds(c.getId());
            List<String> result = new ArrayList<>();
            for (Integer tid : list) {
                for (Tag tag : tags) {
                    if (tag.getId().equals(tid)) {
                        result.add(tag.getName());
                        break;
                    }
                }
            }
            c.setTags(result);
        });
        LOG.info("selectComicList -> 成功，第 " + start / RECORD_PER_PAGE + " 页返回条数：" + comicList.size());
        return comicList;
    }

    /**
     * 新url存在thumb中(相对路径)，修改后将新对象返回
     * @param comic
     * @return
     * @throws Exception
     */
    public Comic updateThumb(@NotNull Comic comic) throws Exception {
        String oldUrl = comic.getUrl();
        String newUrl = comic.getLocation() + "\\" + comic.getThumb();
        newUrl = fastDfs.upload(new File(newUrl));
        comic.setUrl(newUrl);
        comic.setThumb(Constant.toThumbUrl(newUrl));
        comicDo.update(comic);
        fastDfs.deleteImagePair(oldUrl);
        return comic;
    }
}
