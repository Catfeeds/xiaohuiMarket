package com.xlibao.advert.data.mapper;

import com.xlibao.advert.data.model.DownloadRecord;

public interface DownloadRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DownloadRecord record);

    int insertSelective(DownloadRecord record);

    DownloadRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DownloadRecord record);

    int updateByPrimaryKey(DownloadRecord record);
}