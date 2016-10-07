package com.gft.backend.controllers;

import com.gft.backend.entities.EBayCategory;
import com.gft.backend.entities.EBayItem;
import com.gft.backend.utils.EBayContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by miav on 2016-09-27.
 */

@Service
public class EBayService {

    private static final Logger logger = Logger.getLogger(EBayService.class);

    private static final List<EBayItem> dummyArray = new ArrayList<>(2);

    @Autowired
    EBayContext context;

    @Autowired
    HttpClient client;

    @Autowired
    RequestConfig requestConfig;

    public List<EBayItem> searchItems(String categoryId, List<String> keywords, Map<String, String> filters) {
        List<EBayItem> result = dummyArray;
        try {
            Map<String, String> headersMap = new HashMap<>(3);
            headersMap.put("X-EBAY-SOA-SECURITY-APPNAME", context.getApiCredential().getAppId());
            headersMap.put("X-EBAY-SOA-GLOBAL-ID", "EBAY-DE");
            headersMap.put("X-EBAY-SOA-OPERATION-NAME", "findCompletedItems");

            HttpPost httpPost = createHttpPost("http://svcs.sandbox.ebay.com/services/search/FindingService/v1",
                    headersMap);

            //write request xml
            DocumentBuilder documentBuilder = DocumentBuilderFactory.
                    newInstance().newDocumentBuilder();
            Document requestBody = documentBuilder.newDocument();
            Element root = requestBody.createElement("findCompletedItemsRequest");
            root.setAttribute("xmlns", "http://www.ebay.com/marketplace/search/v1/services");
            requestBody.appendChild(root);

            Element keysNode = requestBody.createElement("keywords");
            StringBuilder concatBuilder = new StringBuilder(24);
            keywords.stream().forEach(concatBuilder::append);
            keysNode.setTextContent(concatBuilder.toString());
            root.appendChild(keysNode);

            Element categoryNode = requestBody.createElement("categoryId");
            categoryNode.setTextContent(categoryId);
            root.appendChild(categoryNode);

            if (filters != null && !filters.isEmpty()) {
                filters.entrySet().stream().forEach(map -> {
                    Element itemFilterNode = requestBody.createElement("itemFilter");
                    Element itemFilterNameNode = requestBody.createElement("name");
                    Element itemFilterValueNode = requestBody.createElement("value");
                    itemFilterNameNode.setTextContent(map.getKey());
                    itemFilterValueNode.setTextContent(map.getValue());
                    itemFilterNode.appendChild(itemFilterNameNode);
                    itemFilterNode.appendChild(itemFilterValueNode);
                    root.appendChild(itemFilterNode);
                });
            }

            Element sortNode = requestBody.createElement("sortOrder");
            sortNode.setTextContent("PricePlusShippingLowest");
            root.appendChild(sortNode);

            Element paginationNode = requestBody.createElement("paginationInput");
            Element perPageNode = requestBody.createElement("entriesPerPage");
            perPageNode.setTextContent("10");
            paginationNode.appendChild(perPageNode);
            root.appendChild(paginationNode);

            addXMLBodyToRequest(httpPost, requestBody);

            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    long contentLength = entity.getContentLength();
                    logger.debug("Get response from ebay with length " + contentLength);
                    try (InputStream content = entity.getContent()) {
                        Document document = documentBuilder.parse(content);
                        NodeList ack = document.getElementsByTagName("ack");
                        if (ack.getLength() == 1) {
                            logger.debug("Get ack from ebay: " + ack.item(0).getTextContent());
                            NodeList searchResult = document.getElementsByTagName("searchResult");
                            String itemCount = searchResult.item(0).getAttributes()
                                    .getNamedItem("count").getNodeValue().toString();
                            NodeList items = document.getElementsByTagName("item");
                            result = new ArrayList<EBayItem>(Integer.parseInt(itemCount));
                            for (int i = 0; i < items.getLength(); ++i) {
                                NodeList childContent = items.item(i).getChildNodes();
                                EBayItem eBayItem = new EBayItem();
                                for (int j = 0; j < childContent.getLength(); ++j) {
                                    Node itemParam = childContent.item(j);
                                    if ("itemId".equals(itemParam.getNodeName())) {
                                        eBayItem.setItemId(itemParam.getTextContent());
                                    }
                                    if ("title".equals(itemParam.getNodeName())) {
                                        eBayItem.setTitle(itemParam.getTextContent());
                                    }
                                    if ("paymentMethod".equals(itemParam.getNodeName())) {
                                        eBayItem.setPaymentMethods(itemParam.getTextContent());
                                    }
                                    if ("viewItemURL".equals(itemParam.getNodeName())) {
                                        eBayItem.setItemURL(itemParam.getTextContent());
                                    }
                                    if ("primaryCategory".equals(itemParam.getNodeName())) {
                                        eBayItem.setCategoryId(itemParam.getChildNodes().item(0).getTextContent());
                                    }
                                    if ("sellingStatus".equals(itemParam.getNodeName())) {
                                        NodeList sellingParams = itemParam.getChildNodes();
                                        for (int k = 0; k < sellingParams.getLength(); ++k) {
                                            Node sellPar = sellingParams.item(k);
                                            if ("currentPrice".equals(sellPar.getNodeName())) {
                                                eBayItem.setCurrency(sellPar.getAttributes().item(0).getNodeValue());
                                                eBayItem.setCurrentPrice(sellPar.getTextContent());
                                            }
                                        }

                                    }
                                }
                                result.add(eBayItem);
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            logger.error("XML parser creation is fail", e);
        } catch (SAXException e) {
            logger.error("XML parsing is fail", e);
        } catch (IOException e) {
            logger.error("Fail with http get request to eBay service", e);
        } catch (TransformerConfigurationException e) {
            logger.error("Transform configuration is fail", e);
        } catch (TransformerException e) {
            logger.error("Transformation is fail", e);
        }
        return result;
    }

    private void addXMLBodyToRequest(HttpPost httpPost, Document requestBody) throws TransformerException, UnsupportedEncodingException {
        StringWriter xmlFormatWriter = new StringWriter(32);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(requestBody), new StreamResult(xmlFormatWriter));

        HttpEntity xmlBody = new StringEntity(xmlFormatWriter.toString());
        httpPost.setEntity(xmlBody);
    }

    private HttpPost createHttpPost(String url, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        headers.entrySet().stream().forEach(map -> httpPost.setHeader(map.getKey(), map.getValue()));
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "text/xml");
        return httpPost;
    }


    public List<EBayCategory> getCategories() {
        List<EBayCategory> result = new ArrayList<>(1024);
        try {
            Map<String, String> headersMap = new HashMap<>(3);
            headersMap.put("X-EBAY-API-COMPATIBILITY-LEVEL", "967");
            headersMap.put("X-EBAY-API-SITEID", "77");
            headersMap.put("X-EBAY-API-CALL-NAME", "GetCategories");
            HttpPost httpPost = createHttpPost("https://api.sandbox.ebay.com/ws/api.dll"
                    , headersMap);

            //write request xml
            DocumentBuilder documentBuilder = DocumentBuilderFactory.
                    newInstance().newDocumentBuilder();
            Document requestBody = documentBuilder.newDocument();
            Element root = requestBody.createElement("GetCategoriesRequest");
            root.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
            requestBody.appendChild(root);

            Element requesterCredentials = requestBody.createElement("RequesterCredentials");
            Element eBayAuthToken = requestBody.createElement("eBayAuthToken");
            eBayAuthToken.setTextContent(context.getApiCredential().geteBayToken());
            requesterCredentials.appendChild(eBayAuthToken);
            root.appendChild(requesterCredentials);

            Element categorySiteIDNode = requestBody.createElement("CategorySiteID");
            categorySiteIDNode.setTextContent("0");
            root.appendChild(categorySiteIDNode);

            Element detailsNode = requestBody.createElement("DetailLevel");
            detailsNode.setTextContent("ReturnAll");
            root.appendChild(detailsNode);

            Element levelLimit = requestBody.createElement("LevelLimit");
            levelLimit.setTextContent("2");
            root.appendChild(levelLimit);

            addXMLBodyToRequest(httpPost, requestBody);

            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    long contentLength = entity.getContentLength();
                    logger.debug("Get response from ebay with length " + contentLength);
                    try (InputStream content = entity.getContent()) {
                        Document document = documentBuilder.parse(content);
                        NodeList ack = document.getElementsByTagName("Ack");
                        if (ack.getLength() == 1) {
                            logger.debug("Get ack from ebay: " + ack.item(0).getTextContent());
                            NodeList items = document.getElementsByTagName("Category");
                            for (int i = 0; i < items.getLength(); ++i) {
                                NodeList childContent = items.item(i).getChildNodes();
                                EBayCategory eBayCategory = new EBayCategory();
                                for (int j = 0; j < childContent.getLength(); ++j) {
                                    Node itemParam = childContent.item(j);
                                    if ("CategoryID".equals(itemParam.getNodeName())) {
                                        eBayCategory.setId(itemParam.getTextContent());
                                    }
                                    if ("CategoryLevel".equals(itemParam.getNodeName())) {
                                        eBayCategory.setLevel(itemParam.getTextContent());
                                    }
                                    if ("CategoryName".equals(itemParam.getNodeName())) {
                                        eBayCategory.setName(itemParam.getTextContent());
                                    }
                                    if ("CategoryParentID".equals(itemParam.getNodeName())) {
                                        eBayCategory.setParent(itemParam.getTextContent());
                                    }
                                }
                                result.add(eBayCategory);
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            logger.error("XML parser creation is fail", e);
        } catch (SAXException e) {
            logger.error("XML parsing is fail", e);
        } catch (IOException e) {
            logger.error("Fail with http get request to eBay service", e);
        } catch (TransformerConfigurationException e) {
            logger.error("Transform configuration is fail", e);
        } catch (TransformerException e) {
            logger.error("Transformation is fail", e);
        }
        return result;
    }
}
