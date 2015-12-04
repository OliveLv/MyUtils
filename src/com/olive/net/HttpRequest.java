/**   
* @Title: HttpRequest.java 
* @author olive  
* @date Dec 4, 2015 11:21:57 AM  
* get post 在线测试工具  http://www.coolaf.com/
* jar 
* htmlunit-2.18-OSGi.jar
* 
*/
package com.olive.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HttpRequest {
	/**
	 * 向指定url发送get方法的请求
	 * @param url
	 * @param param 请求参数，类似于 name1=value1&name2=value2
	 * @param enc 设置返回数据的编码方式
	 * @return
	 * reference http://www.cnblogs.com/zhuawang/archive/2012/12/08/2809380.html
	 */
	public static String sendGet(String url,String param,String enc){
		String res="";
		BufferedReader in=null;
		String urlNameString=url+"?"+param;
		try {
			URL URL=new URL(urlNameString);
			URLConnection conn=URL.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.connect();
			Map<String,List<String>>map=conn.getHeaderFields();
			/*for(String key:map.keySet()){
				System.out.println(key+" : "+map.get(key));
			}*/
			if(enc!=null)in=new BufferedReader(new InputStreamReader(conn.getInputStream(),enc));
			else
				in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while((line=in.readLine())!=null){
				res+=line+"\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				try {
					if(in!=null){
					in.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		return res;
	}
	/**
	 * 向指定url发送get方法的请求
	 * @param url
	 * @param param
	 * @return
	 */
	public static String sendGet(String url,String param){
		return sendGet(url,param,null);
	}
	/**
	 * 向指定url发送send方法的请求
	 * @param url
	 * @param param
	 * @param enc
	 * @return
	 * reference http://www.cnblogs.com/zhuawang/archive/2012/12/08/2809380.html
	 */
	public static String sendPost(String url, String param,String enc){
		 PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
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
	            if(enc!=null)in=new BufferedReader(new InputStreamReader(conn.getInputStream(),enc));
				else
					in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line+"\n";
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
	/**
	 * 
	 * @param url
	 * @param param
	 * @return
	 */
	public static String sendPost(String url, String param){
		return sendPost(url,param,null);
	}
	/**
	 * 将map中的值转化为get或send的参数格式，即name1=value1&name2=value2..
	 * @param map
	 * @return
	 */
	 public static String MapToStr(Map<String,String>map){
     	String s="";
     	for(String str:map.keySet()){
     		if(!s.equals(""))s+="&";
     		s+=str+"="+map.get(str);
     	}
     	return s;
     }
	 /**
	  * 将字符串表示的html网页解析成HtmlPage对象
	  * 注意 html的框架应该与url类似？？？
	  * @param url
	  * @param html
	  * @return
	 * @throws IOException 
	 * reference http://stackoverflow.com/questions/6136435/how-to-create-htmlunit-htmlpage-object-from-string%3E
	  */
	 public static HtmlPage StringToHtmlPage(String url,String html) throws IOException{
		 URL URL = new URL(url);
         StringWebResponse response = new StringWebResponse(html, URL);
         WebClient client=new WebClient();
         client.getOptions().setCssEnabled(false);
         client.getOptions().setJavaScriptEnabled(false);

         HtmlPage page = HTMLParser.parseHtml(response, client.getCurrentWindow());
         return page;
	 }
	public static void main(String []args) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		// get test
		String getRes=sendGet("http://c.spdex.com/spdex500b","","utf-8");
		System.out.println(getRes);
		// post test
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
		// 获得期刊为20151204 第3页的页面
        String postRes=sendPost("http://c.spdex.com/spdex500b",MapToStr(map));
        System.out.println(postRes);
        // String to HtmlPage
        HtmlPage p=StringToHtmlPage("http://c.spdex.com/spdex500b",postRes);
        System.out.println(p.asXml());
	}
}
