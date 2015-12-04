/**   
* @Title: Toolkit.java 
* @author olive 
* @date Dec 3, 2015 7:39:25 PM  
* 
*/
/**
 * jar
 * htmlunit-2.18-OSGi.jar
 * commons-httpclient-3.1.jar
 * org.apache.commons.lang.jar
 * 
 */
package com.olive.net;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/** 
* HTTP工具箱 
* 
*/ 
public final class HttpTookit { 
        private static Log log = LogFactory.getLog(HttpTookit.class); 

        /** 
         * 执行一个HTTP GET请求，返回请求响应的HTML 
         * 
         * @param url                 请求的URL地址 
         * @param queryString 请求的查询参数,可以为null 
         * @return 返回请求响应的HTML 
         */ 
        public static String doGet(String url, String queryString) { 
                String response = null; 
                HttpClient client = new HttpClient(); 
                HttpMethod method = new GetMethod(url); 
                try { 
                        if (StringUtils.isNotBlank(queryString)) 
                                method.setQueryString(URIUtil.encodeQuery(queryString)); 
                        client.executeMethod(method); 
                        if (method.getStatusCode() == HttpStatus.SC_OK) { 
                                response = method.getResponseBodyAsString(); 
                        } 
                } catch (URIException e) { 
                        log.error("执行HTTP Get请求时，编码查询字符串“" + queryString + "”发生异常！", e); 
                } catch (IOException e) { 
                        log.error("执行HTTP Get请求" + url + "时，发生异常！", e); 
                } finally { 
                        method.releaseConnection(); 
                } 
                return response; 
        } 

        /** 
         * 执行一个HTTP POST请求，返回请求响应的HTML 
         * 
         * @param url        请求的URL地址 
         * @param params 请求的查询参数,可以为null 
         * @return 返回请求响应的HTML 
         */ 
        // 对一些特殊的参数，如要encode、中文，不一定有效果 
        public static String doPost(String url, Map<String, String> params) { 
                String response = null; 
                HttpClient client = new HttpClient(); 
                HttpMethod method = new PostMethod(url); 
                method.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
                /*for (Iterator it = params.entrySet().iterator(); it.hasNext();) { 
System.out.println("yes");
                } */
                //设置Http Post数据 
                if (params != null) { 
                        HttpMethodParams p = new HttpMethodParams(); 
                        for (Map.Entry<String, String> entry : params.entrySet()) { 
                        	System.out.println(entry.getKey()+" "+entry.getValue());
                                p.setParameter(entry.getKey(), entry.getValue()); 
                        } 
                        method.setParams(p);
                } 
                try { 
                        client.executeMethod(method); 
                        if (method.getStatusCode() == HttpStatus.SC_OK) { 
                                response = method.getResponseBodyAsString(); 
                        } 
                } catch (IOException e) { 
                	e.printStackTrace();
                        log.error("执行HTTP Post请求" + url + "时，发生异常！", e); 
                } finally { 
                        method.releaseConnection(); 
                } 

                return response; 
        } 
        /** 
         * 执行一个HTTP POST请求，返回请求响应的HTML String类型 
         * 
         * @param url        请求的URL地址 
         * @param params 请求的查询参数,可以为null 
         * @return 返回请求响应的HTML String类型
         */ 
        public static String sendPost(String url, String param) {
            PrintWriter out = null;
            BufferedReader in = null;
            String result = "";
            try {
                URL realUrl = new URL(url);
                // 打开和URL之间的连接
                URLConnection conn = realUrl.openConnection();
                // 设置通用的请求属性
               // conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
               // conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
               // conn.setRequestProperty("Accept-Encoding", "gzip, deflate,utf-8");
                // 发送POST请求必须设置如下两行
                conn.setDoOutput(true);
                conn.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();
                // 定义BufferedReader输入流来读取URL的响应
                in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(),"utf-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } catch (Exception e) {
                System.out.println("发送 POST 请求出现异常！"+e);
                e.printStackTrace();
            }
            //使用finally块来关闭输出流、输入流
            finally{
                try{
                    if(out!=null){
                        out.close();
                    }
                    if(in!=null){
                        in.close();
                    }
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
            return result;
        } 
        public static String MapToStr(Map<String,String>map){
        	String s="";
        	for(String str:map.keySet()){
        		if(!s.equals(""))s+="&";
        		s+=str+"="+map.get(str);
        	}
        	return s;
        }
        public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException { 
        	 	WebClient wc=new WebClient(BrowserVersion.CHROME);
        	 	 
        	 	HtmlPage page=wc.getPage("http://c.spdex.com/spdex500b");
        	 	HtmlForm form=page.getForms().get(0);
        	 	String viewstate=form.getInputByName("__VIEWSTATE").getValueAttribute();
        	 	System.out.println(java.net.URLEncoder.encode(viewstate,"utf-8"));
        		Map<String,String>map=new HashMap<String,String>();
        		map.put("__EVENTTARGET", "AspNetPager1");
        		map.put("__EVENTARGUMENT", "3");
        		map.put("__LASTFOCUS", "");
        		map.put("__VIEWSTATE", java.net.URLEncoder.encode(viewstate,"utf-8"));
        		map.put("DropJcId", "20151204");
        		map.put("AspNetPager1_input", "1");
        		map.put("__VIEWSTATEGENERATOR", "8B24BAF8");
        		String s="__EVENTTARGET=AspNetPager1&__EVENTARGUMENT=2&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwUINTA0MjQ0NDIPZBYCAgEPZBYGZg8QDxYCHgtfIURhdGFCb3VuZGdkDxYCZgIBFgIQBQgyMDE1MTIwMwUIMjAxNTEyMDNnEAUIMjAxNTEyMDQFCDIwMTUxMjA0ZxYBZmQCAQ8WAh4LXyFJdGVtQ291bnQCCBYQZg9kFgJmDxUB9QI8ZGl2IGNsYXNzPSdkYXRhdGl0bGUnPjxoMyB0aWQ9JzI3NjEwNjg2Jz5b56ue5b2pMjAxNTEyMDPmnJ%2FnrKwwMDHlnLpdIFzkuK3lpK7mtbcgVlMg5aKo5bCU5pys5biCPC9oMz48c3BhbiBjbGFzcz0nbWF0Y2h0aW1lJz7lvIDotZvml7bpl7TvvJoyMDE1LzEyLzMgMTc6MDA8L3NwYW4%2BPGRpdiBjbGFzcz0nY2xlYXInPjwvZGl2PjwvZGl2PjxkaXYgY2xhc3M9J2RhdGF2aWV3ZXInIGlkPScyNzYxMDY4Nic%2BPC9kaXY%2BPHNjcmlwdCB0eXBlPSd0ZXh0L2phdmFzY3JpcHQnPiQoZG9jdW1lbnQpLnJlYWR5KGZ1bmN0aW9uICgpIHsgR2V0TWF0Y2hEYXRhKDI3NjEwNjg2LDAsJy9JRnJhbWUvSWZyYW1lVmlld2VyUVEuYXNweD9pZD0nKTsgfSk7PC9zY3JpcHQ%2BZAIBD2QWAmYPFQGBAzxkaXYgY2xhc3M9J2RhdGF0aXRsZSc%2BPGgzIHRpZD0nMjc2MTA5NzYnPlvnq57lvakyMDE1MTIwM%2Bacn%2BesrDAwMuWcul0gXOmYv%2BWnhuWNoeWwlCBWUyDojqvmlq%2Fnp5HkuK3lpK7pmYblhps8L2gzPjxzcGFuIGNsYXNzPSdtYXRjaHRpbWUnPuW8gOi1m%2BaXtumXtO%2B8mjIwMTUvMTIvMyAyMjowMDwvc3Bhbj48ZGl2IGNsYXNzPSdjbGVhcic%2BPC9kaXY%2BPC9kaXY%2BPGRpdiBjbGFzcz0nZGF0YXZpZXdlcicgaWQ9JzI3NjEwOTc2Jz48L2Rpdj48c2NyaXB0IHR5cGU9J3RleHQvamF2YXNjcmlwdCc%2BJChkb2N1bWVudCkucmVhZHkoZnVuY3Rpb24gKCkgeyBHZXRNYXRjaERhdGEoMjc2MTA5NzYsMCwnL0lGcmFtZS9JZnJhbWVWaWV3ZXJRUS5hc3B4P2lkPScpOyB9KTs8L3NjcmlwdD5kAgIPZBYCZg8VAfoCPGRpdiBjbGFzcz0nZGF0YXRpdGxlJz48aDMgdGlkPScyNzYxMDk4Mic%2BW%2BernuW9qTIwMTUxMjAz5pyf56ysMDAz5Zy6XSBc5Zyj5b285b6X5aCh5rO95bC854m5IFZTIOS5jOazlTwvaDM%2BPHNwYW4gY2xhc3M9J21hdGNodGltZSc%2B5byA6LWb5pe26Ze077yaMjAxNS8xMi80IDA6MDA8L3NwYW4%2BPGRpdiBjbGFzcz0nY2xlYXInPjwvZGl2PjwvZGl2PjxkaXYgY2xhc3M9J2RhdGF2aWV3ZXInIGlkPScyNzYxMDk4Mic%2BPC9kaXY%2BPHNjcmlwdCB0eXBlPSd0ZXh0L2phdmFzY3JpcHQnPiQoZG9jdW1lbnQpLnJlYWR5KGZ1bmN0aW9uICgpIHsgR2V0TWF0Y2hEYXRhKDI3NjEwOTgyLDAsJy9JRnJhbWUvSWZyYW1lVmlld2VyUVEuYXNweD9pZD0nKTsgfSk7PC9zY3JpcHQ%2BZAIDD2QWAmYPFQGMAzxkaXYgY2xhc3M9J2RhdGF0aXRsZSc%2BPGgzIHRpZD0nMjc2MTA5ODAnPlvnq57lvakyMDE1MTIwM%2Bacn%2BesrDAwNOWcul0gXOiQqOWFsOaWr%2BWFi%2BiOq%2BWwlOWkmueTpiBWUyDmoLznvZflhbnlsLznibnph4zlhYs8L2gzPjxzcGFuIGNsYXNzPSdtYXRjaHRpbWUnPuW8gOi1m%2BaXtumXtO%2B8mjIwMTUvMTIvNCAwOjAwPC9zcGFuPjxkaXYgY2xhc3M9J2NsZWFyJz48L2Rpdj48L2Rpdj48ZGl2IGNsYXNzPSdkYXRhdmlld2VyJyBpZD0nMjc2MTA5ODAnPjwvZGl2PjxzY3JpcHQgdHlwZT0ndGV4dC9qYXZhc2NyaXB0Jz4kKGRvY3VtZW50KS5yZWFkeShmdW5jdGlvbiAoKSB7IEdldE1hdGNoRGF0YSgyNzYxMDk4MCwwLCcvSUZyYW1lL0lmcmFtZVZpZXdlclFRLmFzcHg%2FaWQ9Jyk7IH0pOzwvc2NyaXB0PmQCBA9kFgJmDxUB8QI8ZGl2IGNsYXNzPSdkYXRhdGl0bGUnPjxoMyB0aWQ9JzI3NjEwOTkzJz5b56ue5b2pMjAxNTEyMDPmnJ%2FnrKwwMDXlnLpdIFzljaHmma7ph4wgVlMg57u055C05a%2BfPC9oMz48c3BhbiBjbGFzcz0nbWF0Y2h0aW1lJz7lvIDotZvml7bpl7TvvJoyMDE1LzEyLzQgMTowMDwvc3Bhbj48ZGl2IGNsYXNzPSdjbGVhcic%2BPC9kaXY%2BPC9kaXY%2BPGRpdiBjbGFzcz0nZGF0YXZpZXdlcicgaWQ9JzI3NjEwOTkzJz48L2Rpdj48c2NyaXB0IHR5cGU9J3RleHQvamF2YXNjcmlwdCc%2BJChkb2N1bWVudCkucmVhZHkoZnVuY3Rpb24gKCkgeyBHZXRNYXRjaERhdGEoMjc2MTA5OTMsMCwnL0lGcmFtZS9JZnJhbWVWaWV3ZXJRUS5hc3B4P2lkPScpOyB9KTs8L3NjcmlwdD5kAgUPZBYCZg8VAfoCPGRpdiBjbGFzcz0nZGF0YXRpdGxlJz48aDMgdGlkPScyNzYxMDkzMCc%2BW%2BernuW9qTIwMTUxMjAz5pyf56ysMDA25Zy6XSBc5p6X5oGp5pavIFZTIOavleWwlOW3tOmEguernuaKgDwvaDM%2BPHNwYW4gY2xhc3M9J21hdGNodGltZSc%2B5byA6LWb5pe26Ze077yaMjAxNS8xMi80IDM6MDA8L3NwYW4%2BPGRpdiBjbGFzcz0nY2xlYXInPjwvZGl2PjwvZGl2PjxkaXYgY2xhc3M9J2RhdGF2aWV3ZXInIGlkPScyNzYxMDkzMCc%2BPC9kaXY%2BPHNjcmlwdCB0eXBlPSd0ZXh0L2phdmFzY3JpcHQnPiQoZG9jdW1lbnQpLnJlYWR5KGZ1bmN0aW9uICgpIHsgR2V0TWF0Y2hEYXRhKDI3NjEwOTMwLDAsJy9JRnJhbWUvSWZyYW1lVmlld2VyUVEuYXNweD9pZD0nKTsgfSk7PC9zY3JpcHQ%2BZAIGD2QWAmYPFQH6AjxkaXYgY2xhc3M9J2RhdGF0aXRsZSc%2BPGgzIHRpZD0nMjc2MTA5OTgnPlvnq57lvakyMDE1MTIwM%2Bacn%2BesrDAwN%2BWcul0gXOmbt%2BS4gSBWUyDlpbPnjovlhazlm63lt6HmuLjogIU8L2gzPjxzcGFuIGNsYXNzPSdtYXRjaHRpbWUnPuW8gOi1m%2BaXtumXtO%2B8mjIwMTUvMTIvNCA0OjAwPC9zcGFuPjxkaXYgY2xhc3M9J2NsZWFyJz48L2Rpdj48L2Rpdj48ZGl2IGNsYXNzPSdkYXRhdmlld2VyJyBpZD0nMjc2MTA5OTgnPjwvZGl2PjxzY3JpcHQgdHlwZT0ndGV4dC9qYXZhc2NyaXB0Jz4kKGRvY3VtZW50KS5yZWFkeShmdW5jdGlvbiAoKSB7IEdldE1hdGNoRGF0YSgyNzYxMDk5OCwwLCcvSUZyYW1lL0lmcmFtZVZpZXdlclFRLmFzcHg%2FaWQ9Jyk7IH0pOzwvc2NyaXB0PmQCBw9kFgJmDxUB9AI8ZGl2IGNsYXNzPSdkYXRhdGl0bGUnPjxoMyB0aWQ9JzI3NjEwOTk0Jz5b56ue5b2pMjAxNTEyMDPmnJ%2FnrKwwMDjlnLpdIFzokKjntKLmtJsgVlMg5Y2h5Yip5Lqa6YeMPC9oMz48c3BhbiBjbGFzcz0nbWF0Y2h0aW1lJz7lvIDotZvml7bpl7TvvJoyMDE1LzEyLzQgNDowMDwvc3Bhbj48ZGl2IGNsYXNzPSdjbGVhcic%2BPC9kaXY%2BPC9kaXY%2BPGRpdiBjbGFzcz0nZGF0YXZpZXdlcicgaWQ9JzI3NjEwOTk0Jz48L2Rpdj48c2NyaXB0IHR5cGU9J3RleHQvamF2YXNjcmlwdCc%2BJChkb2N1bWVudCkucmVhZHkoZnVuY3Rpb24gKCkgeyBHZXRNYXRjaERhdGEoMjc2MTA5OTQsMCwnL0lGcmFtZS9JZnJhbWVWaWV3ZXJRUS5hc3B4P2lkPScpOyB9KTs8L3NjcmlwdD5kAgIPDxYCHgtSZWNvcmRjb3VudAIRZGRkzJ%2FWRgI2Y%2BrVb9ql0bJGJyzWq9o%3D&__VIEWSTATEGENERATOR=8B24BAF8&DropJcId=20151203&AspNetPager1_input=1";
               // String x = doPost("http://c.spdex.com/spdex500b", map); 
               // System.out.println(x);
        		String str="";
        		str+="__EVENTTARGET=AspNetPager1&__EVENTARGUMENT=2&__LASTFOCUS=&__VIEWSTATE="+java.net.URLEncoder.encode(viewstate,"utf-8");
        		str+="&DropJcId=20151203&AspNetPager1_input=1";
        		//System.out.println(str);
                String res=sendPost("http://c.spdex.com/spdex500b",MapToStr(map));
               
                URL url = new URL("http://c.spdex.com/spdex500b");
                StringWebResponse response = new StringWebResponse(res, url);
                WebClient client=new WebClient();
                client.getOptions().setCssEnabled(false);
                client.getOptions().setJavaScriptEnabled(false);
   
                HtmlPage page1 = HTMLParser.parseHtml(response, client.getCurrentWindow());
                System.out.println(page1.asXml());
                map.put("__EVENTTARGET", "AspNetPager1");
        		map.put("__EVENTARGUMENT", "2");
        		map.put("__LASTFOCUS", "");
        		map.put("__VIEWSTATE", java.net.URLEncoder.encode(viewstate,"utf-8"));
        		map.put("DropJcId", "20151204");
        		map.put("AspNetPager1_input", "1");
        		map.put("__VIEWSTATEGENERATOR", "8B24BAF8");
        		res=sendPost("http://c.spdex.com/spdex500b",MapToStr(map));
        		response = new StringWebResponse(res, url);
        		page1 = HTMLParser.parseHtml(response, client.getCurrentWindow());
                System.out.println(page1.asXml());
           } 
}
