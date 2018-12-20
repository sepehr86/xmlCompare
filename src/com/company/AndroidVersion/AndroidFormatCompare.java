package com.company.AndroidVersion;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;


public class AndroidFormatCompare {

    /*
        Put XML file in a map & return a map
         */
    private Map<String, String> loadFileFromDictionary(String dictPath) throws ParserConfigurationException, IOException, SAXException {
        HashMap<String, String> map = new HashMap<>();
        NodeList nodeList = createTheDocument(dictPath);

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                map.put(element.getAttribute("name"), element.getTextContent());
            }
        }
        return map;
    }

    /*
    Read a XML file and create a node-list from it & return a node-list
     */
    private NodeList createTheDocument(String path) throws ParserConfigurationException, IOException, SAXException {
        Document doc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(path);
        doc.getDocumentElement().normalize();
        return doc.getElementsByTagName("string");
    }

    /*
    Create our new result XML file & compare two given XML files from Core and Dictionary and put the result in result XML file
     */
    public void compareXML(String coreFilePath, String dicFilePath, String resultFilePath, String logfile) {

        long start = System.currentTimeMillis();
        int counter = 0;

        try {
            Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument(),
                    logDoc = DocumentBuilderFactory
                            .newInstance()
                            .newDocumentBuilder()
                            .newDocument(),
                    deletedLogFileDoc = DocumentBuilderFactory
                            .newInstance()
                            .newDocumentBuilder()
                            .newDocument(),
                    sameKeyDifValueLogDoc = DocumentBuilderFactory
                            .newInstance()
                            .newDocumentBuilder()
                            .newDocument();
            Text sameKeyValueLineBreaker = sameKeyDifValueLogDoc.createTextNode("\n");
            Text deletedLogLineBreaker = deletedLogFileDoc.createTextNode("\n");
            Text linebreaker = doc.createTextNode("\n");
            Text logLineBreak = logDoc.createTextNode("\n");
            Element sameKeyValueRootElement = sameKeyDifValueLogDoc.createElement("resource");
            Element deletedLogFileRootElement = deletedLogFileDoc.createElement("resource");
            Element rootElement = doc.createElement("resource");
            Element logRootElement = logDoc.createElement("resource");
            sameKeyDifValueLogDoc.appendChild(sameKeyValueRootElement);
            deletedLogFileDoc.appendChild(deletedLogFileRootElement);
            doc.appendChild(rootElement);
            logDoc.appendChild(logRootElement);
            sameKeyValueRootElement.appendChild(sameKeyValueLineBreaker);
            deletedLogFileRootElement.appendChild(deletedLogLineBreaker);
            rootElement.appendChild(linebreaker);
            logRootElement.appendChild(logLineBreak);
            NodeList nodeListFromCore = createTheDocument(coreFilePath);
            Map<String, String> mapFromDic = loadFileFromDictionary(dicFilePath);
            for (int j = 0; j < nodeListFromCore.getLength(); j++) {
                Node core_Node = nodeListFromCore.item(j);

                if (core_Node.getNodeType() == Node.ELEMENT_NODE) {

                    Element elementOfCore = (Element) core_Node;
                    String contentOfCore = elementOfCore.getTextContent();
                    String contentOfDic = mapFromDic.get(elementOfCore.getAttribute("name"));
                    if (contentOfDic == null || !modifier(contentOfDic).equals(modifier(contentOfCore))) {
                        System.out.println("-------------\n" + elementOfCore.getAttribute("name") + "\n" + contentOfDic);
                        System.out.println(contentOfCore + "\n----------------");

                        xmlStructureCreate(doc, rootElement, elementOfCore);

                        counter++;
                    }
                    if (contentOfDic == null) {

                        xmlStructureCreate(logDoc, logRootElement, elementOfCore);
                    }
                    if (contentOfDic != null && modifier(contentOfDic).equals(modifier(contentOfCore))) {
                        xmlStructureCreate(deletedLogFileDoc, deletedLogFileRootElement, elementOfCore);
                    }
                    if (contentOfDic != null && !modifier(contentOfDic).equals(modifier(contentOfCore))) {
                        xmlStructureCreate(sameKeyDifValueLogDoc, sameKeyValueRootElement, elementOfCore);
                    }
                }
            }

            generateXmlFile(doc, resultFilePath);
            generateXmlFile(logDoc, logfile);
            generateXmlFile(noneExistElementofDicInCoreLogCreate(dicFilePath, coreFilePath), "c:/dev/result/noneExistFilesInCore.xml");
            generateXmlFile(deletedLogFileDoc, "c:/dev/result/AndroidRemovedFileList.xml");
            generateXmlFile(sameKeyDifValueLogDoc, "c:/dev/result/AndroidSameKeyDifValue.xml");
            System.out.println("\n------------" + "\nNumber of unequal fields: " + counter);


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        NumberFormat formatter = new DecimalFormat("#0.00000");

        System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");


    }

    /*
    Modify(remove space & "" & - ) the text content from given XML file
     */
    private String modifier(String content) {

        String modified_content = content.replace("\"", "");
        return modified_content;
    }
/*
Create a log containing the element which are in Dic but not in Core
 */

    public Document noneExistElementofDicInCoreLogCreate(String dictonaryPath, String corePath) throws IOException, SAXException, ParserConfigurationException {
        Document logFromDic = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .newDocument();

        Text logFromDicLineBreaker = logFromDic.createTextNode("\n");
        Element logFromDicRootElement = logFromDic.createElement("resource");
        logFromDic.appendChild(logFromDicRootElement);
        logFromDicRootElement.appendChild(logFromDicLineBreaker);
        NodeList nodeListFromDic = createTheDocument(dictonaryPath);
        Map<String, String> mapFromCore = loadFileFromDictionary(corePath);
        for (int i = 0; i < nodeListFromDic.getLength(); i++) {
            Node dic_Node = nodeListFromDic.item(i);

            if (dic_Node.getNodeType() == Node.ELEMENT_NODE) {

                Element elementOfDic = (Element) dic_Node;
                String contentOfCore = mapFromCore.get(elementOfDic.getAttribute("name"));
                if (contentOfCore == null) {

                    xmlStructureCreate(logFromDic, logFromDicRootElement, elementOfDic);

                }

            }
        }
        return logFromDic;
    }

    /*
    Generate a xml file
     */
    public void generateXmlFile(Document doc, String resultPath) throws TransformerException, IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        File resultFile = new File(resultPath);
        if (!resultFile.exists()) {
            resultFile.createNewFile();
        }
        StreamResult result = new StreamResult(resultFile);
        transformer.transform(source, result);
        StreamResult consoleResult = new StreamResult(System.out);
        transformer.transform(source, consoleResult);


    }

    /*
    Create a structure of the xml file
     */
    public void xmlStructureCreate(Document document, Element rootElement, Element element) {
        Text lineBreaker = document.createTextNode("\n");
        Element string = document.createElement("string");
        rootElement.appendChild(string);
        rootElement.appendChild(lineBreaker);
        Attr name = document.createAttribute("name");
        name.setValue(element.getAttribute("name"));
        string.setAttributeNode(name);
        string.appendChild(document.createTextNode(element.getTextContent()));

    }


}
