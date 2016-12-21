import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.common.collect.Lists;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by IDEA
 * User: shawntime
 * Date: 2016-12-21 14:13
 * Desc:
 */
public class TuanTest {

    private static final String DOMAIN = "http://localhost:8080";

    private List<String> getUrlList() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("url.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<String> dataList = Lists.newArrayList();
        if(br != null) {
            String line = null;
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }
        }
        br.close();
        inputStream.close();
        return dataList;
    }

    private boolean send(String url) throws UnirestException {
        HttpResponse<JsonNode> jsonNodeHttpResponse = Unirest.get(url).asJson();
        final JsonNode body = jsonNodeHttpResponse.getBody();
        if(body.getObject().getInt("returncode") == 0) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        TuanTest tuanTest = new TuanTest();
        final List<String> urlList = tuanTest.getUrlList();
        int failedNum = 0;
        for(String url : urlList) {
            url = DOMAIN.concat(url);
            boolean isSuccess = false;
            try {
                isSuccess = tuanTest.send(url);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            if(!isSuccess) {
                failedNum++;
                System.err.println(String.format("%s --> %s", url, false));
            } else {
                System.out.println(String.format("%s --> %s", url, true));
            }

        }
        System.out.println(String.format("totalNum:%d, failedNum:%d", urlList.size(), failedNum));
    }
}
