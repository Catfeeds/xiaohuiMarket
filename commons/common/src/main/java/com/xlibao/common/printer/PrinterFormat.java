package com.xlibao.common.printer;

import com.xlibao.common.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @author chinahuangxc
 */
public class PrinterFormat {

    private static final Logger logger = LoggerFactory.getLogger(PrinterFormat.class);

    private List<String> lines = new LinkedList<>();

    private String printerCode;
    // 商品名字的最长空间(其中必须两个空格：前后)
    private static final int ITEM_NAME_LENGTH = 23;
    // 单价的最长空间(包括最后的空格) 即最大值：9999.99
    private static final int PRICE_LENGTH = 8;
    // 数量的最长空间(包括最后的空间) 即最大值：99999
    private static final int COUNT_LENGTH = 6;
    // 总价的最长空间
    private static final int TOTAL_PRICE_LENGTH = 11;

    public PrinterFormat(String printerCode) {
        this.printerCode = printerCode;
    }

    /**
     * 添加 title
     */
    private void addTitle(String title) {
        lines.add(String.format("<CB>%s</CB><BR>", title));
    }

    public void addMenu() {
        // 固定格式：商品                      单价  数量       总价
        lines.add("商品                      单价  数量       总价");
    }

    public PrinterFormat append(String content) {
        lines.add(content);
        return this;
    }

    public void enter() {
        enter(1);
    }

    public void enter(int count) {
        for (int i = 0; i < count; i++) {
            lines.add(CommonUtils.ENTER);
        }
    }

    /**
     * 添加商品
     */
    public void addItem(String name, String price, String number, String totalPrice) {
        if (price.length() < (PRICE_LENGTH - 1)) { // 当单价长度小于规定长度时，自动填充空格(统一右对齐)
            int size = (PRICE_LENGTH - 1) - price.length();
            for (int i = 0; i < size; i++) {
                price = CommonUtils.SPACE + price;
            }
        }
        if (number.length() < (COUNT_LENGTH - 1)) { // 当购买数量长度小于规定长度时，自动填充空格(统一右对齐)
            int size = (COUNT_LENGTH - 1) - number.length();
            for (int i = 0; i < size; i++) {
                number = CommonUtils.SPACE + number;
            }
        }
        if (totalPrice.length() < (TOTAL_PRICE_LENGTH - 1)) { // 当总价长度小于规定长度时，自动填充空格(统一右对齐)
            int size = (TOTAL_PRICE_LENGTH - 1) - totalPrice.length();
            for (int i = 0; i < size; i++) {
                totalPrice = CommonUtils.SPACE + totalPrice;
            }
        }
        // 商品名字总长度，注意：中文字符算两个长度
        int nameLength = CommonUtils.totalCharacterLength(name);
        // 第一行数据名字的结束位置，默认为最后的位置
        int firstIndex = name.length();

        if (nameLength > (ITEM_NAME_LENGTH - 1)) { // 当字符总长度大于规定长度时，通过以下算法计算第一行的名字内容以及截断的位置
            firstIndex = 0;
            int charLength = 0;

            char[] cs = name.toCharArray();
            for (char c : cs) {
                if ((charLength + 1) > (ITEM_NAME_LENGTH - 1)) { // 字符长度已达到规定长度时 退出整个循环
                    break;
                }
                if (CommonUtils.isChinese(c)) { // 如果为中文时 字符长度为2
                    if ((charLength + 2) > (ITEM_NAME_LENGTH - 1)) { // 当字符长度已达到规定长度时 退出整个循环
                        break;
                    }
                    charLength++; // 中文字符长度先自增(中文字符长度为两位)
                }
                firstIndex += 1; // 位置后移
                charLength++; // 字符后移
            }
        }
        String firstValue = name.substring(0, firstIndex); // 截取第一行文字内容
        if (CommonUtils.totalCharacterLength(firstValue) < (ITEM_NAME_LENGTH - 1)) { // 如果第一行名字长度未达标 那么必须往名字后补充空格
            int size = (ITEM_NAME_LENGTH - 1) - CommonUtils.totalCharacterLength(firstValue); // 需补充空格数量
            for (int i = 0; i < size; i++) { // 往右边填充
                firstValue += CommonUtils.SPACE;
            }
        }
        // 第一行内容为：商品名字 单价 数量 总价
        firstValue += CommonUtils.SPACE + price + CommonUtils.SPACE + number + CommonUtils.SPACE + totalPrice;

        lines.add(firstValue);

        if (nameLength > (ITEM_NAME_LENGTH - 1)) { // 存在多余商品名内容时 直接填充到第二行
            lines.add(name.substring(firstIndex));
        }
    }

    /**
     * 填充长度
     *
     * @param value 值
     * @param size  总共需要的长度
     * @param flag  1: 前置补齐; 2: 后置补齐
     */
    private String formatLength(String value, int size, int flag) {
        String subStr = fillSpaceChar(size - CommonUtils.totalCharacterLength(value));
        if (flag == 1) {
            value = subStr + value;
        } else {
            value = value + subStr;
        }
        return value;
    }

    private String fillSpaceChar(int spaceCharSize) {
        String subStr = CommonUtils.SPACE;
        for (int i = 0; i < spaceCharSize; i++) {
            subStr += CommonUtils.SPACE;
        }
        return subStr;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line).append(CommonUtils.ENTER);
        }
        return builder.toString();
    }

    public void addProperty(String key, String value) {
        key = formatLength(key + ":", 16, 2);
        lines.add(key + value);
    }

    public void print(int times) {
        String content = toString();
        logger.info("正在执行打印小票功能，打印联数：" + times + "；本次打印内容：\r\n" + content);
        String response = FeiEPrinter.print(printerCode, content, times);
        logger.info("打印服务返回结果：" + response);
    }

    public static void main(String[] args) {
        PrinterFormat printerFormat = new PrinterFormat("817800284");
        printerFormat.addTitle("销售订单");
        // 1号生活
        printerFormat.append("------------------- 1号生活 --------------------");

        printerFormat.addProperty("当日排号", "001");
        printerFormat.addProperty("订单货号", "190820170516003255736060522");
        printerFormat.addProperty("下单时间", "2017-05-16 00:37:58");

        printerFormat.addProperty("配送日期", "2017-05-17");
        printerFormat.addProperty("仓库名称", "测试供应商");
        printerFormat.enter();

        // 收货人信息
        printerFormat.append("------------------ 收货人信息 ------------------");
        printerFormat.addProperty("收 货 人", "刘平");
        printerFormat.addProperty("电    话", "13800138000");
        printerFormat.addProperty("店铺名称", "715有家猎德店");
        printerFormat.addProperty("地    址", "广州市天河区天河北路时代广场东座1108室");
        printerFormat.addProperty("商家留言", "请尽快配送");
        printerFormat.enter();

        // 配送信息
        printerFormat.append("------------------- 配送信息 -------------------");
        printerFormat.addProperty("快 递 员", "刘平");
        printerFormat.addProperty("联系号码", "13800138000");
        printerFormat.enter();

        // 货品信息
        printerFormat.append("------------------- 货品信息 -------------------");

        printerFormat.addMenu();
        printerFormat.addItem("康师傅老坛酸菜牛肉114g五连包", "9999.99", "9999", "9999999.99");
        printerFormat.addItem("奥利奥缤纷双果味芒果+橙子106g", "999.99", "999", "999999.99");
        printerFormat.addItem("雪碧2L", "9999.99", "9999", "9999999.99");
        printerFormat.addItem("哈尔滨（冰纯）330ml", "9999.99", "9999", "9999999.99");

        printerFormat.enter();
        printerFormat.append("------------------ 二维码信息 ------------------");
        printerFormat.enter();
        printerFormat.append(String.format("<QR>%s</QR>", "GYL_MERCHANT_ORDER/100001"));
        printerFormat.append("         1号生活客服热线：4008-945-917          ");

        // printerFormat.print(3);
        // printerFormat.lines.forEach(System.out::println);
        System.out.println(printerFormat.toString());
    }
}