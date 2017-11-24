package com.xlibao.advert.data.mapper;

import com.xlibao.advert.data.model.AdvertScreenInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by admin on 2017/9/21.
 */
public interface AdvertPlayMapper {
    List<AdvertScreenInfo> getAllAdvertInfo(@Param("mac") String mac);

    int getAdvertInfoCount(@Param("marketId") int marketId, @Param("code") String code, @Param("title") String title, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("isDown") int isDown, @Param("playStatus") int playStatus);

    void updateIsDown(@Param("isDown") int isDown, @Param("advertID") int advertID, @Param("screenID") int screenID, @Param("marketID") int marketID, @Param("successDownTime") String successDownTime);

    List<AdvertScreenInfo> getAdvertTemplates(@Param("marketId") int marketId, @Param("code") String code, @Param("title") String title, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("isDown") int isDown, @Param("playStatus") int playStatus, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    List<AdvertScreenInfo> getAdvertInfoFromID(@Param("advertID") int advertID, @Param("screenID") int screenID, @Param("marketID") int marketID);

    int addAdvertInfoForScreen(@Param("marketID") int marketID, @Param("advertID") int advertID, @Param("screenID") int screenID, @Param("beginTime") String beginTime,  @Param("style") int style,@Param("playOrder") int playOrder, @Param("playRemark") String remark);

    int updateAdvertInfoFromID(@Param("advertID") int advertID, @Param("screenID") int screenID, @Param("marketId") int marketId, @Param("cMarketID") int cMarketID, @Param("cScreenID") int cScreenID, @Param("beginTime") String beginTime,  @Param("playOrder") int playOrder, @Param("remark") String remark,@Param("style")int style);

    int deleteAdvertInfoFromID(@Param("advertID") int advertID, @Param("screenID") int screenID, @Param("marketID") int marketID);

    List<AdvertScreenInfo> getAdvertInfoFromScreenID(@Param("screenID") int screenID);
}
