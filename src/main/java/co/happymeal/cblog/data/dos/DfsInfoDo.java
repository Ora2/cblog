package co.happymeal.cblog.data.dos;

import co.happymeal.cblog.pojo.DfsInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lyd
 */
@Mapper
public interface DfsInfoDo {

    /**
     *  insert方法
     * @param dfsInfo
     * @return
     */
    int insert(DfsInfo dfsInfo);

    /**
     * 根据url删除fastdfs记录信息
     * @param dfsInfo
     * @return
     */
    int deleteByUrl(DfsInfo dfsInfo);

}
