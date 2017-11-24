package com.xlibao.passport.service.address.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.constant.passport.AddressStatusEnum;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.metadata.passport.PassportAddress;
import com.xlibao.passport.data.mapper.passport.PassportDataManager;
import com.xlibao.passport.service.address.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author chinahuangxc on 2017/3/29.
 */
@Transactional
@Service("addressService")
public class AddressServiceImpl extends BasicWebService implements AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private PassportDataManager passportDataManager;

    @Override
    public JSONObject newAddress() {
        long passportId = getLongParameter("passportId");
        String name = getUTF("name");
        String alias = getUTF("alias");
        String phoneNumber = getUTF("phoneNumber");
        String country = getUTF("country", "中华人民共和国");
        String province = getUTF("province", "");
        String city = getUTF("city", "");
        String district = getUTF("district", "");
        String street = getUTF("street", "");
        String streetNum = getUTF("streetNum", "");
        String adcode = getUTF("adcode", "");
        String detailAddress = getUTF("detailAddress");
        double longitude = getDoubleParameter("longitude");
        double latitude = getDoubleParameter("latitude");
        int type = getIntParameter("type", 0);
        byte status = getByteParameter("status", AddressStatusEnum.DEFAULT.getKey());

        if (!CommonUtils.isMobileNum(phoneNumber)) {
            return fail("手机号[" + phoneNumber + "]无效");
        }

        PassportAddress address = new PassportAddress();
        address.setPassportId(passportId);
        address.setName(name);
        address.setAddressAlias(alias);
        address.setPhoneNumber(phoneNumber);
        address.setCountry(country);
        address.setProvince(province);
        address.setCity(city);
        address.setDistrict(district);
        address.setStreet(street);
        address.setStreetNum(streetNum);
        address.setAdcode(adcode);
        address.setDetailAddress(detailAddress);
        address.setLongitude(longitude);
        address.setLatitude(latitude);
        address.setType(type);
        address.setStatus(status);
        address.setCreateTime(new Date());

        if (status == AddressStatusEnum.DEFAULT.getKey()) {
            logger.info("重设" + passportId + "的默认地址");
            passportDataManager.resetAddressStatus(passportId, AddressStatusEnum.DEFAULT.getKey(), AddressStatusEnum.ENABLE.getKey());
        }
        int result = passportDataManager.createAddress(address);
        logger.info(passportId + "新增地址结果：" + result);
        if (result <= 0) {
            throw new XlibaoRuntimeException("新增地址失败");
        }
        JSONObject response = new JSONObject();
        response.put("addressId", address.getId());
        return success(response);
    }

    @Override
    public JSONObject removeAddress() {
        long passportId = getLongParameter("passportId");
        long addressId = getLongParameter("addressId");

        int result = passportDataManager.removeAddress(passportId, addressId, AddressStatusEnum.INVALID.getKey());

        return result <= 0 ? fail("移除地址失败") : success();
    }

    @Override
    public JSONObject setDefaultAddress() {
        long passportId = getLongParameter("passportId");
        long addressId = getLongParameter("addressId");

        // 检查用户和地址的合法性
        getAddress(passportId, addressId);
        // 将原来的默认地址设置为非默认地址
        passportDataManager.resetAddressStatus(passportId, AddressStatusEnum.DEFAULT.getKey(), AddressStatusEnum.ENABLE.getKey());
        // 设置默认地址
        int result = passportDataManager.setDefaultAddress(addressId, AddressStatusEnum.DEFAULT.getKey());
        if (result <= 0) {
            throw new XlibaoRuntimeException("设置失败");
        }
        return success("设置成功");
    }

    @Override
    public JSONObject modifyAddress() {
        long passportId = getLongParameter("passportId");
        long addressId = getLongParameter("addressId");
        String name = getUTF("name");
        String alias = getUTF("alias");
        String phoneNumber = getUTF("phoneNumber");
        String country = getUTF("country", "中华人民共和国");
        String province = getUTF("province", "");
        String city = getUTF("city", "");
        String district = getUTF("district", "");
        String street = getUTF("street", "");
        String streetNum = getUTF("streetNum", "");
        String adcode = getUTF("adcode", "");
        String detailAddress = getUTF("detailAddress");
        double longitude = getDoubleParameter("longitude");
        double latitude = getDoubleParameter("latitude");
        int type = getIntParameter("type", 0);
        byte status = getByteParameter("status", AddressStatusEnum.DEFAULT.getKey());

        // 检查用户和地址的合法性
        getAddress(passportId, addressId);

        PassportAddress address = new PassportAddress();
        address.setPassportId(passportId);
        address.setId(addressId);
        address.setName(name);
        address.setAddressAlias(alias);
        address.setPhoneNumber(phoneNumber);
        address.setCountry(country);
        address.setProvince(province);
        address.setCity(city);
        address.setDistrict(district);
        address.setStreet(street);
        address.setStreetNum(streetNum);
        address.setAdcode(adcode);
        address.setDetailAddress(detailAddress);
        address.setLongitude(longitude);
        address.setLatitude(latitude);
        address.setType(type);
        address.setStatus(status);

        if (status == AddressStatusEnum.DEFAULT.getKey()) {
            logger.info("重设" + passportId + "的默认地址");
            passportDataManager.resetAddressStatus(passportId, AddressStatusEnum.DEFAULT.getKey(), AddressStatusEnum.ENABLE.getKey());
        }
        int result = passportDataManager.modifyAddress(address);
        logger.info(passportId + "修改地址结果：" + result);
        if (result <= 0) {
            throw new XlibaoRuntimeException("保存地址失败");
        }
        return success("保存地址成功");
    }

    @Override
    public JSONObject getAddress() {
        long passportId = getLongParameter("passportId");
        long addressId = getLongParameter("addressId");

        PassportAddress address = getAddress(passportId, addressId);
        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(address));

        return success(response);
    }

    @Override
    public JSONObject getDefaultAddress() {
        long passportId = getLongParameter("passportId");

        PassportAddress address = passportDataManager.getDefaultAddress(passportId);
        if (address == null) {
            return fail("您未添加收货地址，请先设置！");
        }
        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(address));

        return success(response);
    }

    @Override
    public JSONObject getAddresses() {
        long passportId = getLongParameter("passportId");
        String statusSet = AddressStatusEnum.DEFAULT.getKey() + CommonUtils.SPLIT_COMMA + AddressStatusEnum.ENABLE.getKey();

        List<PassportAddress> addresses = passportDataManager.getAddresses(passportId, statusSet);

        JSONArray addressArray = new JSONArray();
        for (PassportAddress address : addresses) {
            JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(address));
            addressArray.add(data);
        }
        return success(addressArray);
    }

    private PassportAddress getAddress(long passportId, long addressId) {
        String statusSet = AddressStatusEnum.DEFAULT.getKey() + CommonUtils.SPLIT_COMMA + AddressStatusEnum.ENABLE.getKey();
        PassportAddress address = passportDataManager.getAddress(passportId, addressId, statusSet);
        if (address == null) {
            throw new XlibaoRuntimeException("您没有权限操作该地址，错误码：" + addressId);
        }
        return address;
    }
}