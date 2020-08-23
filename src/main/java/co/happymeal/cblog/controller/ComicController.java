package co.happymeal.cblog.controller;

import co.happymeal.cblog.pojo.Comic;
import co.happymeal.cblog.pojo.SearchCriteria;
import co.happymeal.cblog.service.ComicServiceImpl;
import co.happymeal.cblog.util.Constant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static co.happymeal.cblog.util.Constant.*;

/**
 * 等着完成吧。
 *
 * @author lyd
 */
@Controller
@Validated
@RequestMapping("/comic")
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true", allowedHeaders = "*")
public class ComicController {

    public static final Logger LOG = LoggerFactory.getLogger(ComicController.class);

    @Autowired
    private ComicServiceImpl comicService;

    @RequestMapping(value = "/folder", method = RequestMethod.POST)
    @ResponseBody
    public String scanFolderAndAddComic(@RequestBody String path) {
        path = String.valueOf(JSON.parseObject(path).get("path"));
        comicService.scanFolderAndAddComic(path);
        LOG.info("███████╗██╗███╗   ██╗██╗███████╗██╗  ██╗");
        LOG.info("██╔════╝██║████╗  ██║██║██╔════╝██║  ██║");
        LOG.info("█████╗  ██║██╔██╗ ██║██║███████╗███████║");
        LOG.info("██╔══╝  ██║██║╚██╗██║██║╚════██║██╔══██║");
        LOG.info("██║     ██║██║ ╚████║██║███████║██║  ██║");
        LOG.info("╚═╝     ╚═╝╚═╝  ╚═══╝╚═╝╚══════╝╚═╝  ╚═╝");
        return SUCCESS;
    }

    @RequestMapping(value = "/clear", method = RequestMethod.DELETE)
    @ResponseBody
    public String clearFastDfs() {
        comicService.clearFastDfs();
        return SUCCESS;
    }

    @RequestMapping(value = "/folder/state", method = RequestMethod.GET)
    @ResponseBody
    public String processState() {
        return comicService.getProcessState();
    }

    @RequestMapping(value = "/info", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteComic(@RequestBody Comic comic) {
        comicService.deleteComicById(comic);
        return SUCCESS;
    }

    @RequestMapping(value = "/info/{page}", method = RequestMethod.GET)
    @ResponseBody
    public String loadComic(@PathVariable @Min(0) Integer page) {
        return loadComic(page, null);
    }

    @RequestMapping(value = "/info/{page}/{tags}", method = RequestMethod.GET)
    @ResponseBody
    public String loadComic(@PathVariable @Min(0) Integer page,
                            @PathVariable @Nullable String tags) {
        int start = page * RECORD_PER_PAGE;
        List<String> tagNames = null;
        if (tags != null) {
            tagNames = Arrays.asList(tags.split(","));
        }

        String result = comicService.loadComicByPage(start, RECORD_PER_PAGE, tagNames);
        return result;
    }

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ResponseBody
    public String updateOrAddComic(@NotNull @RequestBody Comic comic) {
        boolean isSuccess = true;
        if (comic.getId() != null) {
            isSuccess = comicService.updateComic(comic);
        } else {
            isSuccess = comicService.addComic(comic);
        }
        if (isSuccess) {
            return SUCCESS;
        }
        return Fail;
    }

    @RequestMapping(value = "/thumb", method = RequestMethod.POST)
    @ResponseBody
    public Comic updateThumb(@RequestBody Comic comic) throws Exception {
        return comicService.updateThumb(comic);
    }

    @RequestMapping(value = "/num", method = RequestMethod.GET)
    @ResponseBody
    public String loadNums() {
        return comicService.loadNums();
    }

    @RequestMapping(value = "/relation/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String loadRelations(@PathVariable Integer id) {
        return JSON.toJSONString(comicService.loadRelations(id));
    }

    @RequestMapping(value = "/relation", method = RequestMethod.POST)
    @ResponseBody
    public String addRelation(@RequestBody String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer id = jsonObject.getInteger("id");
        String location = jsonObject.getString("location");
        Comic comic = comicService.addRelation(id, location);
        JSONObject result = new JSONObject();
        if (comic != null) {
            result.put("state", SUCCESS);
            result.put("comic", comic);
        } else {
            result.put("state", Fail);
        }
        return result.toJSONString();
    }

    @RequestMapping(value = "/relation", method = RequestMethod.DELETE)
    @ResponseBody
    public String removeRelation(@RequestBody String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer id = jsonObject.getInteger("id");
        String location = jsonObject.getString("location");
        Comic comic = comicService.removeRelation(id, location);
        JSONObject result = new JSONObject();
        if (comic != null) {
            result.put("state", SUCCESS);
            result.put("comic", comic);
        } else {
            result.put("state", Fail);
        }
        return result.toJSONString();
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public List<Comic> search(@RequestBody SearchCriteria searchCriteria) {
        List<Comic> comicList = comicService.selectComicList(searchCriteria);
        return comicList;
    }

    @RequestMapping(value = "/open", method = RequestMethod.POST)
    @ResponseBody
    public void open(@NotNull @RequestBody String json) {
        String location = JSON.parseObject(json).getString("location");
        try {
            Desktop.getDesktop().open(new File(location));
        } catch (Exception e) {
            LOG.error(json + " ::: 打开目录时出错");
        }
    }
}
