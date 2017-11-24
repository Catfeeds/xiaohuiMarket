package com.xlibao.order.data.mapper.order;

import com.xlibao.metadata.order.OrderItemSnapshot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemSnapshotMapper {

    int createOrderItemSnapshot(OrderItemSnapshot orderItemSnapshot);

    List<OrderItemSnapshot> getOrderItemSnapshots(@Param("orderId") long orderId);

    List<OrderItemSnapshot> batchGetOrderItemSnapshots(@Param("orderSet") String orderSet);

    int receiptItem(@Param("itemSnapshotId") long itemSnapshotId, @Param("receiptQuantity") int receiptQuantity);

    int shippingItem(@Param("itemSnapshotId") long itemSnapshotId, @Param("shippingQuantity") int shippingQuantity);

    int distributionItem(@Param("itemSnapshotId") long itemSnapshotId, @Param("distributionCount") int distributionCount);

    int totalDistributionItems(@Param("itemSnapshotId") long itemSnapshotId);

    int arriveItem(@Param("itemSnapshotId") long itemSnapshotId);

    int modifyItemSnapshot(@Param("orderId") long orderId, @Param("itemId") long itemId, @Param("normalPrice") long normalPrice, @Param("discountPrice") long discountPrice, @Param("normalQuantity") int normalQuantity,
                                @Param("discountQuantity") int discountQuantity, @Param("totalPrice") long totalPrice);
}