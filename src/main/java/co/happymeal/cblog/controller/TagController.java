package co.happymeal.cblog.controller;

import co.happymeal.cblog.pojo.Tag;
import co.happymeal.cblog.service.TagServiceImpl;
import co.happymeal.cblog.util.Constant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyd
 */
@Controller
@CrossOrigin
@RequestMapping("/tag")
public class TagController {

    public static final Logger LOG = LoggerFactory.getLogger(TagController.class);

    @Autowired
    private TagServiceImpl tagService;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public String selectAllTags() {
        return JSON.toJSONString(tagService.selectAllTags());
    }

    @RequestMapping(value = "/cascade", method = RequestMethod.POST)
    @ResponseBody
    public String updateTagCascade(@RequestBody String jsonString) {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String tagName = jsonObject.getString("tagName");
        String dropNode = jsonObject.getString("dropNode");
        String position = jsonObject.getString("position");
        tagService.updateTagCascade(tagName, dropNode, position);
        return Constant.SUCCESS;
    }

    @RequestMapping(value = "/info", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteTags(@RequestBody List<String> tagNames) {
        tagService.deleteTags(tagNames);
        return JSON.toJSONString(tagNames);
    }

    @RequestMapping(value = "/info2", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteTagDialog(@RequestBody String tagName) {
        List<String> list = new ArrayList<>();
        list.add(tagName);
        tagService.deleteTags(list);
        return Constant.SUCCESS;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Tag> getTagTree() {
        return tagService.getTagTree();
    }
}
