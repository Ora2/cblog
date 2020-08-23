package co.happymeal.cblog.service;

import co.happymeal.cblog.data.dos.TagDo;
import co.happymeal.cblog.pojo.Tag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lyd
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagServiceImpl {

    public static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private TagDo tagDo;

    public List<Tag> selectAllTags() {
        List<Tag> tags = tagDo.selectAllTags();
        tags.stream().filter(t -> t.getNum() == null).forEach(tag -> tag.setNum(0));
        return tags;
    }

    public void deleteTags(@NotNull List<String> tagNames) {
        Map<String, Integer> allTags = tagDo.selectAllTags().stream().collect(Collectors.toMap(Tag::getName, Tag::getId));
        List<Integer> ids = tagNames.stream().map(allTags::get).collect(Collectors.toList());
        tagDo.removeAllTagComicPairByTagId(ids);
        tagDo.removePidByTagId(ids);
        tagDo.removeAllTagsByTagId(ids);
    }

    public List<Tag> getTagTree() {
        return tagDo.selectTagsWithChildren();
    }

    public void updateTagCascade(String tagName, String dropNode, String position) {
        tagDo.updateTagCascade(tagName, dropNode, position);
    }
}
