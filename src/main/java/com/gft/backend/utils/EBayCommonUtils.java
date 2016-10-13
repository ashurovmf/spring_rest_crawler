package com.gft.backend.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by miav on 2016-10-13.
 */
public class EBayCommonUtils {

    public static HttpPost createHttpPost(RequestConfig requestConfig, String url, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        headers.entrySet().stream().forEach(map -> httpPost.setHeader(map.getKey(), map.getValue()));
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "text/xml");
        return httpPost;
    }

    public static void addXMLBodyToRequest(HttpPost httpPost, Document requestBody)
            throws TransformerException, UnsupportedEncodingException {
        StringWriter xmlFormatWriter = new StringWriter(32);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(requestBody), new StreamResult(xmlFormatWriter));

        HttpEntity xmlBody = new StringEntity(xmlFormatWriter.toString());
        httpPost.setEntity(xmlBody);
    }
}
