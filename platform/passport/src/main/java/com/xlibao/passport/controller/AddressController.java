package com.xlibao.passport.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.passport.service.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/3/29.
 */
@Controller
@RequestMapping(value = "/passport/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * <pre>
     *     <b>新增地址</b>
     *
     *     <b>请求地址：</b>http://domainName/address/newAddress
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>请求参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>name</b> - String 收货人姓名，必填参数。
     *          <b>alias</b> - String 地址别名，必填参数。
     *          <b>phoneNumber</b> - String 联系人号码，必填参数。
     *          <b>country</b> - String 归属国家，非必填参数；默认为：中华人民共和国。
     *          <b>province</b> - String 所在省份，非必填参数；直辖市时填充为直辖市名称，如：北京市、重庆市等。
     *          <b>city</b> - String 所在城市，非必填参数。
     *          <b>district</b> - String 所在行政区域，非必填参数。
     *          <b>street</b> - String 街道，非必填参数。
     *          <b>streetNum</b> - String 街道号，非必填参数。
     *          <b>adcode</b> - String 归属城市编码，非必填参数。
     *          <b>detailAddress</b> - String 具体地址，必填参数。
     *          <b>longitude</b> - double 经度，必填参数；主要用于导航、距离计算等。
     *          <b>latitude</b> - double 纬度，必填参数；主要用于导航、距离计算等。
     *          <b>type</b> - int 地址类型，非必填参数；暂时为0，表示通用，以后可扩展为仅对某个应用有效。
     *          <b>status</b> - byte 地址状态，非必填参数；具体参考：{@linkplain com.xlibao.common.constant.passport.AddressStatusEnum}；默认为{@linkplain com.xlibao.common.constant.passport.AddressStatusEnum#DEFAULT}。
     *
     *     <b>返回结果：</b>
     *          <b>addressId</b> - long 地址ID
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "newAddress")
    public JSONObject newAddress() {
        return addressService.newAddress();
    }

    /**
     * <pre>
     *     <b>移除地址</b>
     *
     *     <b>请求地址：</b>http://domainName/address/removeAddress
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>请求参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>addressId</b> - long 地址ID，必填参数。
     *
     *     <b>返回结果：</b>仅通知成功或失败
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "removeAddress")
    public JSONObject removeAddress() {
        return addressService.removeAddress();
    }

    /**
     * <pre>
     *     <b>设置默认地址</b>
     *
     *     <b>请求地址：</b>http://domainName/address/setDefaultAddress
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>请求参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>addressId</b> - long 地址ID，必填参数。
     *
     *     <b>返回结果：</b>仅通知成功或失败
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "setDefaultAddress")
    public JSONObject setDefaultAddress() {
        return addressService.setDefaultAddress();
    }

    /**
     * <pre>
     *     <b>修改地址</b>
     *
     *     <b>请求地址：</b>http://domainName/address/modifyAddress
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>请求参数：</b>所有数据，即使本次没有修改，也必须将原来的数据带回。
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>addressId</b> - long 需修改信息的地址ID，必填参数。
     *          <b>name</b> - String 收货人姓名，必填参数。
     *          <b>alias</b> - String 地址别名，必填参数。
     *          <b>phoneNumber</b> - String 联系人号码，必填参数。
     *          <b>country</b> - String 归属国家，非必填参数；默认为：中华人民共和国。
     *          <b>province</b> - String 所在省份，非必填参数；直辖市时填充为直辖市名称，如：北京市、重庆市等。
     *          <b>city</b> - String 所在城市，非必填参数。
     *          <b>district</b> - String 所在行政区域，非必填参数。
     *          <b>street</b> - String 街道，非必填参数。
     *          <b>streetNum</b> - String 街道号，非必填参数。
     *          <b>adcode</b> - String 归属城市编码，非必填参数。
     *          <b>detailAddress</b> - String 具体地址，必填参数。
     *          <b>longitude</b> - double 经度，必填参数；主要用于导航、距离计算等。
     *          <b>latitude</b> - double 纬度，必填参数；主要用于导航、距离计算等。
     *          <b>type</b> - int 地址类型，非必填参数；暂时为0，表示通用，以后可扩展为仅对某个应用有效。
     *          <b>status</b> - byte 地址状态，非必填参数；具体参考：{@linkplain com.xlibao.common.constant.passport.AddressStatusEnum}；默认为{@linkplain com.xlibao.common.constant.passport.AddressStatusEnum#DEFAULT}。
     *
     *     <b>返回结果：</b>仅返回成功或失败的描述
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "modifyAddress")
    public JSONObject modifyAddress() {
        return addressService.modifyAddress();
    }

    /**
     * <pre>
     *     <b>获取地址</b>
     *
     *     <b>请求地址：</b>http://domainName/address/getAddress
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>请求参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>addressId</b> - long 地址ID，必填参数。
     *
     *     <b>返回结果：</b>
     *          <b>id</b> - long 地址ID
     *          <b>passportId</b> - long 通行证ID
     *          <b>addressAlias</b> - String 地址别名
     *          <b>name</b> - String 收货人姓名
     *          <b>phoneNumber</b> - String 联系号码
     *          <b>country</b> - String 国家
     *          <b>province</b> - String 省份
     *          <b>city</b> - String 城市
     *          <b>district</b> - String 行政区域
     *          <b>street</b> - String 街道
     *          <b>streetNum</b> - String 街道号
     *          <b>detailAddress</b> - String 详细地址
     *          <b>adcode</b> - String 城市编码
     *          <b>longitude</b> - double 经度
     *          <b>latitude</b> - double 纬度
     *          <b>type</b> - int 地址类型
     *          <b>status</b> - byte 地址状态，具体参考：{@linkplain com.xlibao.common.constant.passport.AddressStatusEnum}
     *          <b>createTime</b> - long 建立时间(无需使用)
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getAddress")
    public JSONObject getAddress() {
        return addressService.getAddress();
    }

    /**
     * <pre>
     *     <b>获取默认地址</b>
     *
     *     <b>请求地址：</b>http://domainName/address/getDefaultAddress
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>请求参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>addressId</b> - long 地址ID，必填参数。
     *
     *     <b>返回结果：</b>
     *          <b>id</b> - long 地址ID
     *          <b>passportId</b> - long 通行证ID
     *          <b>addressAlias</b> - String 地址别名
     *          <b>name</b> - String 收货人姓名
     *          <b>phoneNumber</b> - String 联系号码
     *          <b>country</b> - String 国家
     *          <b>province</b> - String 省份
     *          <b>city</b> - String 城市
     *          <b>district</b> - String 行政区域
     *          <b>street</b> - String 街道
     *          <b>streetNum</b> - String 街道号
     *          <b>detailAddress</b> - String 详细地址
     *          <b>adcode</b> - String 城市编码
     *          <b>longitude</b> - double 经度
     *          <b>latitude</b> - double 纬度
     *          <b>type</b> - int 地址类型
     *          <b>status</b> - byte 地址状态，具体参考：{@linkplain com.xlibao.common.constant.passport.AddressStatusEnum}
     *          <b>createTime</b> - long 建立时间(无需使用)
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getDefaultAddress")
    public JSONObject getDefaultAddress() {
        return addressService.getDefaultAddress();
    }

    /**
     * <pre>
     *     <b>获取全部地址</b>
     *
     *     <b>请求地址：</b>http://domainName/address/getAddresses
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>请求参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *
     *     <b>返回结果：</b>
     *          <b>datas</b> - JSONArray 地址集合，每个元素为JSONObject结构，数据：
     *              <b>id</b> - long 地址ID
     *              <b>passportId</b> - long 通行证ID
     *              <b>addressAlias</b> - String 地址别名
     *              <b>name</b> - String 收货人姓名
     *              <b>phoneNumber</b> - String 联系号码
     *              <b>country</b> - String 国家
     *              <b>province</b> - String 省份
     *              <b>city</b> - String 城市
     *              <b>district</b> - String 行政区域
     *              <b>street</b> - String 街道
     *              <b>streetNum</b> - String 街道号
     *              <b>detailAddress</b> - String 详细地址
     *              <b>adcode</b> - String 城市编码
     *              <b>longitude</b> - double 经度
     *              <b>latitude</b> - double 纬度
     *              <b>type</b> - int 地址类型
     *              <b>status</b> - byte 地址状态，具体参考：{@linkplain com.xlibao.common.constant.passport.AddressStatusEnum}
     *              <b>createTime</b> - long 建立时间(无需使用)
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getAddresses")
    public JSONObject getAddresses() {
        return addressService.getAddresses();
    }
}