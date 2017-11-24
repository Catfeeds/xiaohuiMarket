package com.xlibao.common.file;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author chinahuangxc on 2016/11/10.
 */
public class XMLSupport {

    public static String mapToXML(String rootElementName, Map<String, String> parameters) {
        Document document = DocumentHelper.createDocument();
        // 创建root节点
        Element root = document.addElement(rootElementName);
        for (Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // 生成root的一个子接点
            Element element = root.addElement(key);
            // 为节点添加文本, 也可以用addText()
            element.addCDATA(value);
        }
        return document.asXML();
    }

    public static Map<String, String> xmlToMap(String xmlContent) {
        try {
            Document document = DocumentHelper.parseText(xmlContent);
            Element root = document.getRootElement();

            List<Element> elements = root.elements();
            Map<String, String> parameters = new HashMap<>();
            for (Element element : elements) {
                parameters.put(element.getName(), element.getTextTrim());
            }
            return parameters;
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> xmlToMap(Element root) {
        List<Element> elements = root.elements();
        Map<String, String> parameters = new HashMap<>();
        for (Element element : elements) {
            parameters.put(element.getName(), element.getTextTrim());
        }
        return parameters;
    }
}