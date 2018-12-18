package com.jinlailiao.service.impl;

import com.jinlailiao.service.SensitiveWordService;
import com.jinlailiao.utils.HttpClientUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {

    @Override
    public String replaceSensitiveWord(String msg){
        try {
            HttpClient httpClient = HttpClientUtils.getCustomerHttpClient();
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            BasicNameValuePair a = new BasicNameValuePair("friendUids","6455734251445813253");
            BasicNameValuePair b = new BasicNameValuePair("relation","1");
            params.add(a);
            params.add(b);
            String requestUrl = "https://iaas.cloud.tencent.com/cgi/capi?i=apigateway/RunApiForMarket&uin=2974387651";
            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.setHeader("cookie","_ga=GA1.2.586725772.1537151862; pgv_pvi=750322688; qcloud_uid=606649be8f4d23895bad5ce5153f286f%40devE; qcloud_visitId=1a5b985eaa562123a002f274ecf4a4c9; qcloud_from=qcloud.google.seo-1542617772769; pgv_si=s7865797632; _gcl_au=1.1.493518191.1542617775; _gat=1; lastLoginType=qq; uin=o2974387651; tinyid=144115212212653461; skey=11z4ZR43WXDDZl77BxP-GF6InwLVgA977--UGrjHZR4_; loginType=qq; intl=1; language=zh; appid=1253882772; moduleId=1253882772; opc_xsrf=739971f765cd7aa51dde584285e023be%7C1542703931; qb.sid=y8PV_ax9zEsjB2zOMg1SuXgO-6Zoez6v; qb.sid.sig=bH3oWfldCpXlLASZ1oETxz3hgtw; qcact.sid=s%3Aa2J8NtiuU0TFRpym7fGDLnhSXKe1nOGs.tzdOA1quW2l54qGWZHaEAhdNq8Yzem7WV5mi4EuiOJY; nick=yang; systemTimeGap=-16; ownerUin=O2974387651G");
            httpPost.setHeader("origin", "https://iaas.cloud.tencent.com");
            httpPost.setHeader("referer", "https://iaas.cloud.tencent.com/proxy.html");
            httpPost.setHeader("x-csrfcode", "2084430757");
            httpPost.setHeader("x-referer", "https://console.cloud.tencent.com/apimarket_test/invoke?rid=8&serviceId=service-3zhwkeob&apiId=api-luudy6hy");
            httpPost.setHeader("x-seqid", "fd93478f-28c1-69d1-8608-e687d5d86978");
            if (params != null && params.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //将参数填入POST Entity中
            }
            HttpResponse httpResponse = httpClient.execute(httpPost);
            String result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
            System.out.println("HTTP ReasonPhrase: " + result);
            System.out.println("HTTP Status: " + httpResponse.getStatusLine().getStatusCode());
            return "";
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
