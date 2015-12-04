/**   
* @Title: crawl.java 
* @author lxh  
* @date Dec 4, 2015 11:21:00 AM  
* 
*/
package com.olive.net;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
public class Crawl {
	public static HtmlPage getPage(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		WebClient wc = new WebClient(BrowserVersion.CHROME);
		wc.getOptions().setCssEnabled(false);
		HtmlPage page=wc.getPage(url);
		return page;
	}
	public static HtmlPage doJS(HtmlPage page,String jsCode){
		return (HtmlPage)page.executeJavaScript(jsCode).getNewPage();
	}

}
