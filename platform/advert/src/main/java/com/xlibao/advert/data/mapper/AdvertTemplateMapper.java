package com.xlibao.advert.data.mapper;

import com.xlibao.advert.data.model.AdvertInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by admin on 2017/9/21.
 */
public interface AdvertTemplateMapper {
    List<AdvertInfo> searchAdvertTemplates(@Param("title") String title, @Param("timeType") int timeType, @Param("isUsed") int isUsed, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    int getAdvertCount();

    int getMaxAdvertID();

    int uploadAdvertInfo(AdvertInfo advertInfo);

    List<AdvertInfo> searchAdvertFromID(@Param("advertID") int advertID);

    void updateAdvertInfo(@Param("title") String title, @Param("timeSize") int timeSize, @Param("remark") String remark, @Param("advertID") int advertID);

    void deleteAdvertFromID(@Param("advertID") int advertID);

    int isExistAdvert();

    List<AdvertInfo> getAdvertByGroupId(Long groupId);

    List<AdvertInfo> getAdvertsByParameter(@Param("groupId") Long groupId,@Param("screenId") Long screenId,@Param("marketId") Long marketId);

}
