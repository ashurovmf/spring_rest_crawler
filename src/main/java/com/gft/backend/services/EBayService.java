package com.gft.backend.services;

import com.gft.backend.annotations.LogMethodTime;
import com.gft.backend.entities.EBayCategory;
import com.gft.backend.entities.EBayItem;
import com.gft.backend.utils.EBayContext;
import com.gft.backend.utils.EBayGetCategoriesUtils;
import com.gft.backend.utils.EBaySearchItemsUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by miav on 2016-09-27.
 */

@Service
public class EBayService {

    private static final Logger logger = Logger.getLogger(EBayService.class);

    private static final List<EBayItem> dummyItemsArray = new ArrayList<>(2);
    private static final List<EBayCategory> dummyCategoryArray = new ArrayList<>(2);

    @Autowired
    EBayContext context;

    @Autowired
    HttpClient client;

    @Autowired
    RequestConfig requestConfig;

    @LogMethodTime(millisecondsThreshold = 150)
    public List<EBayItem> searchItems(String categoryId, List<String> keywords, Map<String, String> filters) {
        List<EBayItem> result = dummyItemsArray;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.
                    newInstance().newDocumentBuilder();
            HttpPost httpPost = EBaySearchItemsUtils.getSearchItemsPost(requestConfig,
                    context.getApiCredential().getAppId(),
                    categoryId, keywords, filters, documentBuilder);

            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EBaySearchItemsUtils.parseSearchItemsResponse(documentBuilder, entity);
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

    @LogMethodTime(millisecondsThreshold = 500)
    public List<EBayCategory> getCategories() {
        List<EBayCategory> result = dummyCategoryArray;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.
                    newInstance().newDocumentBuilder();
            HttpPost httpPost = EBayGetCategoriesUtils.getCategoriesPost(requestConfig,
                    context.getApiCredential().geteBayToken(),documentBuilder);

            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EBayGetCategoriesUtils.parseGetCategoriesResponse(documentBuilder, entity);
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
