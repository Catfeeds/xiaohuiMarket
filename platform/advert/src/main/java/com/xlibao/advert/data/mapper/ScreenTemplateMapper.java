package com.xlibao.advert.data.mapper;

import com.xlibao.advert.data.model.ScreenInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by admin on 2017/9/21.
 */
public interface ScreenTemplateMapper {
    List<ScreenInfo> getScreenTemplateList(@Param("code") String code, @Param("marketId") int marketId, @Param("screenSize") String screenSize, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    int getScreenCount();

    List<ScreenInfo> getScreenInfoFromMAC(@Param("mac") String mac);

    List<ScreenInfo> getAllScreenInfo(@Param("marketId") int marketId);

    int addScreenInfo(ScreenInfo infos);

    int getMaxScreenID();

    int deleteScreenFromID(@Param("screenID") int screenID, @Param("marketID") int marketID);

    int isMarketExist(@Param("marketID") int marketID);

    int getMaxScreenIDWithMarketID(int marketID);

    List<ScreenInfo> getScreenInfoFromAdvertID(int advertID);
}
