package my.com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpClientUtil {

	protected Log logger = LogFactory.getLog(getClass());

	// private static String url =
	// "http://192.168.1.16:8080/fc.app/appSiteUserLogin.htm";
	// private static String
	// url="http://www.edbangong.com/fc.app/appSiteUserLogin.htm";

	public static String sendHttpGet(String url, Map<String, Object> params) throws IOException {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url + "?" + getStrParams(params));
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			/*
			 * // 获取所有响应头字段 Map<String, List<String>> map =
			 * connection.getHeaderFields(); // 遍历所有的响应头字段 for (String key :
			 * map.keySet()) { System.out.println(key + "--->" + map.get(key));
			 * }
			 */
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return result;
	}

	
	
	
	public static String sendHttpPost(String url, Map<String, Object> params) throws IOException {
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(getStrParams(params));
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			out.close();
			in.close();
		}
		return result.toString();
	}

 

	public static String getStrParams(Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			sb.append("&");
		}
		String str = sb.toString().substring(0, sb.toString().length() - 1);
		return str;
	}
}
