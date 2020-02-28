package com.lb.base.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpPostFileUtils {
//	private String httpurl = "http://localhost:8090/facePay/faceWeCheakVideo";
//	private String httpurl = "http://localhost:8090/facePay/videoTest";
	private String httpurl = "https://ybj.zjzwfw.gov.cn/facebk/facePay/faceWeCheakVideo";
//	private String httpurl = "https://ybj.zjzwfw.gov.cn/facebk/facePay/videoTest";
	public static void main(String[] args) {
		HttpPostFileUtils tt = new HttpPostFileUtils();
		tt.ylcs ();
//		tt.doLivenessAuthByVideoFiles("F://1582786150134.mp4");
//		Map<String, byte[]> filesMap = new HashMap<String, byte[]>();
//		byte[] bt = new byte[1024];
//		filesMap.put("a.mp4", bt);
//		tt.postFiles("http://localhost:8090/facePay/faceWeCheakVideo", null, filesMap);
	}

	
	 public static HttpEntity getMultiDefaultFileEntity(String pathlj) {
	        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	        File file = new File(pathlj);
	        builder.addBinaryBody("imageFile", file);
	        try {
//	            builder.addPart("name", new StringBody("邵震洲", Charset.forName("UTF-8")));
//				builder.addPart("idNo", new StringBody("330125197609210018", Charset.forName("UTF-8")));
				builder.addPart("name", new StringBody("林博", Charset.forName("UTF-8")));
				builder.addPart("idNo", new StringBody("411123199209228013", Charset.forName("UTF-8")));
				builder.addPart("isFirst", new StringBody("1", Charset.forName("UTF-8")));
				builder.addPart("vector", new StringBody("1", Charset.forName("UTF-8")));
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	        return builder.build();
	    }
	    
	    public void doLivenessAuthByVideoFiles( String videoPath) {
	        String resJsonStr ="";

	        SkipHttpsUtil  skipHttpsUtil=new SkipHttpsUtil();
	        CloseableHttpClient httpclient = null;
	        CloseableHttpResponse response = null;
	            httpclient =  (CloseableHttpClient)skipHttpsUtil.wrapClient();
	            HttpPost post = new HttpPost(httpurl);
	            HttpEntity dataEntity = getMultiDefaultFileEntity(videoPath);// File文件格式上传
	            post.setEntity(dataEntity);
	            try {
					long st = new Date().getTime();
					response = httpclient.execute(post);
//					System.out.println(response.toString());
					System.out.println(new Date().getTime()-st);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}            
	    }

	public void ylcs (){
		timeto = 0;
		for (int i=0;i<10;i++){
			Thread tr = new Thread(new ThreaTest(i));
			tr.start();
		}
	}

	public static long timeto = 0;

	private class ThreaTest implements Runnable{
		ThreaTest(int a){
			this.a=a;
		}
		private int a = 0;
		public void run() {
			for (int i=0;i<5;i++){
				HttpPostFileUtils tt = new HttpPostFileUtils();
				tt.doLivenessAuthByVideoFiles("F://1582786150134.mp4");
			}
		}
	}



	public void doLivenessAuthByVideoFile( String videoPath, String ticketId) {
		String resJsonStr ="";
		HttpClient httpclient =  new DefaultHttpClient();
		HttpPost post = new HttpPost(httpurl);
		HttpEntity dataEntity = getMultiDefaultFileEntity(videoPath);// File文件格式上传
		post.setEntity(dataEntity);
		try {
			HttpResponse response = httpclient.execute(post);
			System.out.println(response);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param filePostUrl 请求接口URL
	 * @param params	  除文件外其它参数
	 * @param filesMap    Map<文件名,文件二进制byte[]>
	 * @return
	 */
	public static String postFiles(String filePostUrl,
								   Map<String, String> params, Map<String, byte[]> filesMap) {

		HttpPost httppost = new HttpPost(filePostUrl);
		HttpClient client = new DefaultHttpClient();
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		// 是否多个文件
		for (String key : filesMap.keySet()) {
			entityBuilder.addBinaryBody("file", filesMap.get(key),
					ContentType.DEFAULT_BINARY, key);
		}
		// 是否有表单参数
//		for (String key : params.keySet()) {
//			entityBuilder.addTextBody(key, params.get(key),ContentType.TEXT_PLAIN.withCharset("UTF-8"));
//		}
		httppost.setEntity(entityBuilder.build());
		try {
			HttpResponse response = client.execute(httppost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity(), "UTF-8");
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
		return null;
	}
}
