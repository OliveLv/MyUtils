/**   
* @Title: HttpRequest.java 
* @author olive  
* @date Dec 4, 2015 11:21:57 AM  
* get post ���߲��Թ���  http://www.coolaf.com/
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
	 * ��ָ��url����get����������
	 * @param url
	 * @param param ��������������� name1=value1&name2=value2
	 * @param enc ���÷������ݵı��뷽ʽ
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
	 * ��ָ��url����get����������
	 * @param url
	 * @param param
	 * @return
	 */
	public static String sendGet(String url,String param){
		return sendGet(url,param,null);
	}
	/**
	 * ��ָ��url����send����������
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
	            // �򿪺�URL֮�������
	            URLConnection conn = realUrl.openConnection();
	            // ����ͨ�õ���������
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // ����POST�������������������
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            // ��ȡURLConnection�����Ӧ�������
	            out = new PrintWriter(conn.getOutputStream());
	            // �����������
	            out.print(param);
	            // flush������Ļ���
	            out.flush();
	            // ����BufferedReader����������ȡURL����Ӧ
	            if(enc!=null)in=new BufferedReader(new InputStreamReader(conn.getInputStream(),enc));
				else
					in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line+"\n";
	            }
	        } catch (Exception e) {
	            System.out.println("���� POST ��������쳣��"+e);
	            e.printStackTrace();
	        }
	        //ʹ��finally�����ر��������������
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
	 * ��map�е�ֵת��Ϊget��send�Ĳ�����ʽ����name1=value1&name2=value2..
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
	  * ���ַ�����ʾ��html��ҳ������HtmlPage����
	  * ע�� html�Ŀ��Ӧ����url���ƣ�����
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
		// ����ڿ�Ϊ20151204 ��3ҳ��ҳ��
        String postRes=sendPost("http://c.spdex.com/spdex500b",MapToStr(map));
        System.out.println(postRes);
        // String to HtmlPage
        HtmlPage p=StringToHtmlPage("http://c.spdex.com/spdex500b",postRes);
        System.out.println(p.asXml());
	}
}
