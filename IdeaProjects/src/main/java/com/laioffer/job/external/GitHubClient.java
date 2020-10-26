package com.laioffer.job.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.job.entity.Item;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GitHubClient {
    private static final String URL_TEMPLATE = "https://jobs.github.com/positions.json?description=%s&lat=%s&long=%s";
    private static final String DEFAULT_KEYWORD = "";

    //search method
    public List<Item> search(double lat, double lon, String keyword) {
        if (keyword == null) {
            keyword = DEFAULT_KEYWORD;
        }
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //format URL based on lat,lon,keyword
        String url = String.format(URL_TEMPLATE, keyword, lat, lon);
        //create an http client
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // Create a custom response handler. If status is not valid(!= 200) we return "", if yes, we change the response
        //entity to string
        //ResponseHandler<String> responseHandler = response -> {
          ResponseHandler<List<Item>> responseHandler = response -> {
            if (response.getStatusLine().getStatusCode() != 200) {
                //return "";
                return Collections.emptyList();
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                //return "";
                return Collections.emptyList();
            }
            //return EntityUtils.toString(entity);
              ObjectMapper mapper = new ObjectMapper();
              List<Item> items = Arrays.asList(mapper.readValue(entity.getContent(), Item[].class));
              extractKeywords(items);
              return items;

          };
        //let the httpClient execute the query based on url and responsehandler
        try {
            return httpclient.execute(new HttpGet(url), responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return "";

        return Collections.emptyList();
    }
    private void extractKeywords(List<Item> items) {
        MonkeyLearnClient monkeyLearnClient = new MonkeyLearnClient();


//        List<String> descriptions = new ArrayList<>();
//        for (Item item : items) {
//            descriptions.add(item.getDescription());
//        }
        //get the list of item descriptions(in strings)
        List<String> descriptions = items.stream()
                .map(Item::getDescription)
                .collect(Collectors.toList());

        List<Set<String>> keywordList = monkeyLearnClient.extract(descriptions);
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setKeywords(keywordList.get(i));
        }
    }


}
