package co.happymeal.cblog.service;

import co.happymeal.cblog.data.dos.ComicDo;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastDfsClientServiceTests {

    @Autowired
    private FastDfsClientServiceImpl service;

    @Autowired
    private ComicDo comicDo;

    @Test
    public void test() {
        System.out.println("Do nothing for build.");
    }

//    @Test
//    public void uploadTest() throws Exception {
//        File file = new File("D:\\We need to hide this\\4c\\1551723527734.gif");
//        System.out.println(service.uploadGif(file, "gif"));
//    }


//    @Test
//    public void testDelete() {
//        String url = "http://192.168.128.3:8080/group1/M00/00/00/wKiAA12J2bOAYXAYADCNr1_cHhc634.JPG";
//    }
//
//    @Test
//    public void tryCa() {
//        String url = "group1/M00/00/00/wKiAA12AOZOAJl4RACgThncL0tY229.PNG";
//        url = url.substring(0, url.lastIndexOf(".")) + "_220x300" + url.substring(url.lastIndexOf("."));
//        System.out.println(url);
//    }
//
//    @Test
//    public void deleteAllImage() {
//        comicDo.selectAll().stream().forEach(url -> {
//            try{
//                service.deleteImagePair(url);
//            } catch (Exception e) {
//
//            }
//        });
//        System.err.println("███████╗██╗███╗   ██╗██╗███████╗██╗  ██╗");
//        System.err.println("██╔════╝██║████╗  ██║██║██╔════╝██║  ██║");
//        System.err.println("█████╗  ██║██╔██╗ ██║██║███████╗███████║");
//        System.err.println("██╔══╝  ██║██║╚██╗██║██║╚════██║██╔══██║");
//        System.err.println("██║     ██║██║ ╚████║██║███████║██║  ██║");
//        System.err.println("╚═╝     ╚═╝╚═╝  ╚═══╝╚═╝╚══════╝╚═╝  ╚═╝");
//    }
//
//    @Test
//    public void repairImg() {
//        comicDo.repairImg().forEach(comic -> {
//            try {
//                File folder = new File(comic.getLocation());
//                File img = folder.listFiles()[0];
//                while (img.isDirectory()) {
//                    img = img.listFiles()[0];
//                }
//                comic.setUrl(service.upload(img));
//                comicDo.repair(comic);
//            } catch (Exception e) {
//                System.out.println(comic);
//            }
//        });
//    }

}
