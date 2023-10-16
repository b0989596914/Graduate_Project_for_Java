package com.project.demo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Search_PomXml {
    public static int FindSpring(String path) {
        int R_Flag = 0, Web_Flag = 0;

        if (Files.exists(Path.of(path))) {
            System.out.println("檔案存在");

            // Xpath 尋找
            try {
                File inputFile = new File(path);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                Document doc = dbFactory.newDocumentBuilder().parse(inputFile);
                doc.getDocumentElement().normalize();
                XPath xPath =  XPathFactory.newInstance().newXPath();

                // 判斷在 <parent> 中的 <groupId> 與 <artifactId> 是否為正確的spring boot專案
                String parent_expr = "/project/parent";   //找到 <project> <parent> 的地方
                NodeList parent_nodeList = (NodeList) xPath.compile(parent_expr).evaluate(doc, XPathConstants.NODESET);
                if (parent_nodeList.item(0) != null){
                    Node nNode = parent_nodeList.item(0);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if ((eElement.getElementsByTagName("groupId").item(0).getTextContent().contains("org.springframework.boot"))
                                && (eElement.getElementsByTagName("artifactId").item(0).getTextContent().contains("spring-boot-starter-parent")) ){
                            R_Flag = 1;
                        }
                    }
                }

                // 是否有 "spring-boot-starter-web" 在 <dependency> 中
                String dependency_expr = "/project/dependencies/dependency";
                NodeList depend_nodeList = (NodeList) xPath.compile(dependency_expr).evaluate(doc, XPathConstants.NODESET);
                for (int i=0; i < depend_nodeList.getLength(); i++) {       // 有幾個 dependency 就跑幾次 -> 無法只跑一次
                    if (depend_nodeList.item(i) != null){
                        Node nNode = depend_nodeList.item(i);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            if ((eElement.getElementsByTagName("groupId").item(0).getTextContent().contains("org.springframework.boot"))
                                    && (eElement.getElementsByTagName("artifactId").item(0).getTextContent().contains("spring-boot-starter-web")) ){
                                Web_Flag = 1;
                                break;
                            }
                        }
                    }
                }
            } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
                e.printStackTrace();
            }
            return R_Flag & Web_Flag;
        } else {
            System.out.println("檔案不存在");
            return 0;
        }


    }
}
