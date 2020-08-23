package co.happymeal.cblog.service;

import co.happymeal.cblog.data.dos.DfsInfoDo;
import co.happymeal.cblog.pojo.DfsInfo;
import co.happymeal.cblog.util.Constant;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author lyd
 */
@Service
public class FastDfsClientServiceImpl {

    private final Logger LOG = LoggerFactory.getLogger(FastDfsClientServiceImpl.class);
    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private ThumbImageConfig thumbImageConfig;
    @Autowired
    private DfsInfoDo dfsInfoDo;

    //上传文件
    public String upload(MultipartFile myfile) throws Exception {
        //文件名
        String originalFilename = myfile.getOriginalFilename().substring(myfile.getOriginalFilename().lastIndexOf(".") + 1);
        // 文件扩展名
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(myfile.getInputStream(), myfile.getSize(), originalFilename, null);

        String path = storePath.getFullPath();

        return path;
    }

    /**
     *
     * 上传文件并返回大图url<br/>
     * 使用{@link co.happymeal.cblog.util.Constant#toThumbUrl(String)}获得缩略图
     * @param file 所需上传图片
     * @return 可访问的大图url
     * @throws Exception Anything you unexpected
     */
    public String upload(@NotNull File file) throws Exception {

        String extName = file.getName().substring(file.getName().lastIndexOf(".") + 1).toUpperCase();
        long fileSize = file.length();

        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(new FileInputStream(file), fileSize, extName, null);
        String path = Constant.completUrl(storePath.getFullPath());
        dfsInfoDo.insert(new DfsInfo(path, "comic"));
        return path;
    }

    /**
     *
     * 上传文件并返回大图url<br/>
     * 使用{@link co.happymeal.cblog.util.Constant#toThumbUrl(String)}获得缩略图
     * @param file 所需上传图片
     * @return 可访问的大图url
     * @throws Exception Anything you unexpected
     */
    public String upload(@NotNull File file, String type) throws Exception {
        String extName = file.getName().substring(file.getName().lastIndexOf(".") + 1).toUpperCase();
        long fileSize = file.length();

        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(new FileInputStream(file), fileSize, extName, null);
        String path = Constant.completUrl(storePath.getFullPath());
        dfsInfoDo.insert(new DfsInfo(path, type));
        return path;
    }

    /**
     *
     * 上传文件<br/>
     * @param file 所需上传图片
     * @return 可访问的url
     * @throws Exception Anything you unexpected
     */
    public String uploadGif(@NotNull File file, String type) throws Exception {
        String extName = file.getName().substring(file.getName().lastIndexOf(".") + 1).toUpperCase();
        long fileSize = file.length();
        StorePath storePath = this.storageClient.uploadFile(new FileInputStream(file), fileSize, extName, null);
        String path = Constant.completUrl(storePath.getFullPath());
        dfsInfoDo.insert(new DfsInfo(path, type));
        return path;
    }


    /**
     * 删除文件
     *
     * @Param fileUrl 文件访问地址
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
            dfsInfoDo.deleteByUrl(new DfsInfo(fileUrl));
        } catch (FdfsUnsupportStorePathException e) {
            LOG.warn(e.getMessage());
        }
    }

    /**
     * 根据大图url删除大图和缩略图
     * @param url
     */
    public void deleteImagePair(String url) {
        String thumbUrl = Constant.toThumbUrl(url);
        this.deleteFile(url);
        this.deleteFile(thumbUrl);
    }
}
