package com.gft.backend.utils;

import com.gft.backend.entities.EBayCategory;
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
public class EBayGetCategoriesUtils {
    private static final List<EBayCategory> dummyCategoryArray = new ArrayList<>(2);
    private static final Logger logger = Logger.getLogger(EBayGetCategoriesUtils.class);
    public static HttpPost getCategoriesPost(RequestConfig requestConfig, String eBayToken,
                                             DocumentBuilder documentBuilder)
            throws TransformerException, UnsupportedEncodingException {
        Map<String, String> headersMap = new HashMap<>(3);
        headersMap.put("X-EBAY-API-COMPATIBILITY-LEVEL", "967");
        headersMap.put("X-EBAY-API-SITEID", "77");
        headersMap.put("X-EBAY-API-CALL-NAME", "GetCategories");
        HttpPost httpPost = EBayCommonUtils.createHttpPost(requestConfig,"https://api.sandbox.ebay.com/ws/api.dll"
                , headersMap);

        //write request xml
        Document requestBody = documentBuilder.newDocument();
        Element root = requestBody.createElement("GetCategoriesRequest");
        root.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
        requestBody.appendChild(root);

        Element requesterCredentials = requestBody.createElement("RequesterCredentials");
        Element eBayAuthToken = requestBody.createElement("eBayAuthToken");
        eBayAuthToken.setTextContent(eBayToken);
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

        EBayCommonUtils.addXMLBodyToRequest(httpPost, requestBody);
        return httpPost;
    }


    public static List<EBayCategory> parseGetCategoriesResponse(DocumentBuilder documentBuilder, HttpEntity entity)
            throws IOException, SAXException {
        List<EBayCategory> result = dummyCategoryArray;
        if (entity != null) {
            long contentLength = entity.getContentLength();
            logger.debug("Get response from ebay with length " + contentLength);
            try (InputStream content = entity.getContent()) {
                Document document = documentBuilder.parse(content);
                NodeList ack = document.getElementsByTagName("Ack");
                if (ack.getLength() == 1) {
                    logger.debug("Get ack from ebay: " + ack.item(0).getTextContent());
                    result = new ArrayList<>(1024);
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
        return result;
    }
}
