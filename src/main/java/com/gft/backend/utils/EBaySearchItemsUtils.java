package com.gft.backend.utils;

import com.gft.backend.entities.EBayItem;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by miav on 2016-10-13.
 */
public class EBaySearchItemsUtils {
    private static final Logger logger = Logger.getLogger(EBaySearchItemsUtils.class);

    private static final List<EBayItem> dummyArray = new ArrayList<>(2);

    public static HttpPost getSearchItemsPost(RequestConfig requestConfig, String appID,
                                              String categoryId, List<String> keywords, Map<String, String> filters,
                                              DocumentBuilder documentBuilder)
            throws TransformerException, UnsupportedEncodingException {
        Map<String, String> headersMap = new HashMap<>(3);
        headersMap.put("X-EBAY-SOA-SECURITY-APPNAME", appID);
        headersMap.put("X-EBAY-SOA-GLOBAL-ID", "EBAY-DE");
        headersMap.put("X-EBAY-SOA-OPERATION-NAME", "findCompletedItems");

        HttpPost httpPost = EBayCommonUtils.createHttpPost(requestConfig,
                "http://svcs.sandbox.ebay.com/services/search/FindingService/v1",
                headersMap);

        //write request xml
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

        EBayCommonUtils.addXMLBodyToRequest(httpPost, requestBody);
        return httpPost;
    }

    public static List<EBayItem> parseSearchItemsResponse(DocumentBuilder documentBuilder,
                                                          HttpEntity entity) throws IOException, SAXException {
        List<EBayItem> result = dummyArray;
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
        return result;
    }
}
