package com.xlibao.passport.data.mapper.passport;

import com.xlibao.common.constant.passport.AddressStatusEnum;
import com.xlibao.metadata.passport.Passport;
import com.xlibao.metadata.passport.PassportAddress;
import com.xlibao.metadata.passport.PassportProperties;
import com.xlibao.passport.data.model.PassportAlias;
import com.xlibao.passport.data.model.PassportLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chinahuangxc on 2017/2/7.
 */
@Component
public class PassportDataManager {

    @Autowired
    private PassportMapper passportMapper;
    @Autowired
    private PassportAliasMapper passportAliasMapper;
    @Autowired
    private PassportPropertiesMapper passportPropertiesMapper;
    @Autowired
    private PassportLoggerMapper passportLoggerMapper;
    @Autowired
    private PassportAddressMapper addressMapper;

    public int createPassport(Passport passport) {
        return passportMapper.createPassport(passport);
    }

    public Passport getPassport(long passportId) {
        return passportMapper.getPassport(passportId);
    }

    public Passport getPassport(String username) {
        PassportAlias alias = passportAliasMapper.getPassportAlias(username);
        if (alias == null) {
            return null;
        }
        return passportMapper.getPassport(alias.getPassportId());
    }

    public int modifyPassportPassword(long id, String password) {
        return passportMapper.modifyPassportPassword(id, password);
    }

    public int modifyNickname(long id, String nickname) {
        return passportMapper.modifyNickname(id, nickname);
    }

    public int perfectPassportInformation(long passportId, String nickName, String headImageUrl, byte sex, int status) {
        return passportMapper.perfectPassportInformation(passportId, nickName, headImageUrl, sex, status);
    }

    public int modifyAccessToken(long id, String accessToken, String appointTime) {
        return passportMapper.modifyAccessToken(id, accessToken, appointTime);
    }

    public int extendAccessToken(long passportId, String accessToken, String appointTime) {
        return passportMapper.extendAccessToken(passportId, accessToken, appointTime);
    }

    public int modifyPhoneNumber(long passportId, String mobileNumber) {
        return passportMapper.modifyPhoneNumber(passportId, mobileNumber);
    }

    public int createPassportAlias(PassportAlias passportAlias) {
        return passportAliasMapper.createPassportAlias(passportAlias);
    }

    public int createPassportLogger(PassportLogger passportLogger) {
        return passportLoggerMapper.createPassportLogger(passportLogger);
    }

    public int createPassportProperties(long passportId, int type, String k, String v) {
        PassportProperties properties = new PassportProperties();
        properties.setPassportId(passportId);
        properties.setType(type);
        properties.setK(k);
        properties.setV(v);
        return passportPropertiesMapper.createPassportProperties(properties);
    }

    public PassportProperties getPassportProperties(long passportId, int type, String k) {
        return passportPropertiesMapper.getPassportProperties(passportId, type, k);
    }

    public List<PassportProperties> getPassportProperties(long passportId, int type) {
        return passportPropertiesMapper.getPassportPropertiesForType(passportId, type);
    }

    public int createAddress(PassportAddress address) {
        return addressMapper.createAddress(address);
    }

    public PassportAddress getAddress(long passportId, long addressId, String statusSet) {
        return addressMapper.getAddress(passportId, addressId, statusSet);
    }

    public PassportAddress getDefaultAddress(long passportId) {
        return addressMapper.getAddressByStatus(passportId, AddressStatusEnum.DEFAULT.getKey());
    }

    public List<PassportAddress> getAddresses(long passportId, String statusSet) {
        return addressMapper.getAddresses(passportId, statusSet);
    }

    public int resetAddressStatus(long passportId, byte beforeStatus, byte afterStatus) {
        return addressMapper.resetAddressStatus(passportId, beforeStatus, afterStatus);
    }

    public int modifyAddress(PassportAddress address) {
        return addressMapper.modifyAddress(address);
    }

    public int setDefaultAddress(long addressId, byte status) {
        return addressMapper.setDefaultAddress(addressId, status);
    }

    public int removeAddress(long passportId, long addressId, byte status) {
        return addressMapper.removeAddress(passportId, addressId, status);
    }
}