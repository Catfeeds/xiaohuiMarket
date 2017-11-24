package com.xlibao.saas.market.data.mapper.item;

import com.xlibao.market.data.model.MarketItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketItemMapper {

    int marketItemUpdateStatus(@Param("itemId") long itemId, @Param("status") long status);

    int createItem(MarketItem item);

    MarketItem getItem(@Param("marketId") long marketId, @Param("itemTemplateId") long itemTemplateId);

    MarketItem getMarketItem(@Param("id") long id);

    List<MarketItem> specialProducts(@Param("marketId") long marketId, @Param("appointType") long appointType, @Param("timeout") long timeout, @Param("sortType") int sortType, @Param("sortValue") int sortValue,
                                     @Param("requestSource") int requestSource, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    List<MarketItem> conditionOrdering(@Param("marketId") long marketId, @Param("itemTemplateSet") String itemTemplateSet, @Param("sortType") int sortType, @Param("sortValue") int sortValue,
                                       @Param("requestSource") int requestSource, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    List<MarketItem> getItemsForItemTemplateSet(@Param("marketId") long marketId, @Param("itemTemplateSet") String itemTemplateSet, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    List<MarketItem> getItemForItemTemplates(@Param("marketId") long marketId, @Param("itemTemplateSet") String itemTemplateSet);

    List<MarketItem> getItems(@Param("itemSet") String itemSet);

    List<Long> existItemTemplates(@Param("marketId") long marketId, @Param("itemTemplateSet") String itemTemplateSet, @Param("requestSource") int requestSource);

    List<MarketItem> searchMarketItems(@Param("marketId") long marketId, @Param("searchType") String searchType, @Param("searchKey") String searchKey, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    int  searchMarketItemCount(@Param("marketId") long marketId, @Param("searchType") String searchType, @Param("searchKey") String searchKey, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    int lockItemStock(@Param("itemId") long itemId, @Param("lockQuantity") int lockQuantity);

    int itemPending(@Param("itemId") long itemId, @Param("quantity") int quantity);

    int incrementPending(@Param("itemId") long itemId, @Param("quantity") int quantity);

    int decrementItemStock(@Param("itemId") long itemId, @Param("quantity") int quantity);

    int offShelves(@Param("itemId") long itemId, @Param("quantity") int quantity, @Param("status") int status);

    int updateItem(@Param("itemId") long itemId, @Param("costPrice") long costPrice, @Param("sellPrice") long sellPrice, @Param("marketPrice") long marketPrice, @Param("discountPrice") long discountPrice, @Param("status") byte status, @Param("description") String description);

    int releaseItemLockQuantity(@Param("itemId") long itemId, @Param("releaseLockQuantity") int releaseLockQuantity);
}