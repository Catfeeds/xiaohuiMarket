package com.xlibao.passport.data.mapper.passport;

import com.xlibao.metadata.passport.PassportAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PassportAddressMapper {

    int createAddress(PassportAddress address);

    PassportAddress getAddress(@Param("passportId") long passportId, @Param("addressId") long addressId, @Param("statusSet") String statusSet);

    PassportAddress getAddressByStatus(@Param("passportId") long passportId, @Param("status") int status);

    List<PassportAddress> getAddresses(@Param("passportId") long passportId, @Param("statusSet") String statusSet);

    int resetAddressStatus(@Param("passportId") long passportId, @Param("beforeStatus") byte beforeStatus, @Param("afterStatus") byte afterStatus);

    int modifyAddress(PassportAddress address);

    int setDefaultAddress(@Param("addressId") long addressId, @Param("status") byte status);

    int removeAddress(@Param("passportId") long passportId, @Param("addressId") long addressId, @Param("status") byte status);
}