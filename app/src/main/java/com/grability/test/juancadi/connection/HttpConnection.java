//class-name:     	HttpConnection
//class-overview: 	Implementa algunos métodos útiles para realizar transacciones HTTP. En Android
//					Studio se debe adicionar la dependencia "useLibrary 'org.apache.http.legacy'" en
// 					el script de configuración app/build.gradle
//class-autor:    	Juancadi
//class-date:     	2015-11-12

package com.grability.test.juancadi.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpConnection {

	public HttpConnection() {
		
	}

	public String buildUrlGET(String url, String[] keys, String[] values){
		
		StringBuffer urlGET = new StringBuffer(url);
		urlGET.append("?");

		for (int i = 0; i < keys.length; i++) {

			urlGET.append(keys[i]+"="+values[i]);
			if(i != keys.length-1) urlGET.append("&");
		}
		
		
		return urlGET.toString();
		
	}
	
	

	public String sendWithGET(String url) throws Exception {

		
		URL urlGET = new URL(url);
		URI uriGET = new URI(urlGET.getProtocol(), urlGET.getUserInfo(), urlGET.getHost(), urlGET.getPort(), urlGET.getPath(), urlGET.getQuery(), urlGET.getRef());
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet();
		request.setURI(uriGET);
		HttpResponse response = client.execute(request);
		return processServerResponse(response);
	}

	
	public String sendWithPOST(String url, String[][] parameters) throws Exception {
		
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		for (int i = 0; i < parameters.length; i++) {
			postParameters.add(new BasicNameValuePair(parameters[i][0],
					parameters[i][1]));
		}

		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
		request.setEntity(formEntity);
		HttpResponse response = client.execute(request);
		return processServerResponse(response);
	}

	
	public String processServerResponse(HttpResponse response)
			throws IOException {
		InputStreamReader in = null;
		in = new InputStreamReader(response.getEntity().getContent());
		int length = (int) response.getEntity().getContentLength();

		String str;
		if (length != -1) {
			char servletData[] = new char[length];
			in.read(servletData);
			str = new String(servletData);
		} else {
			ByteArrayOutputStream bStrm = new ByteArrayOutputStream();
			int ch;
			while ((ch = in.read()) != -1)
				bStrm.write(ch);
			str = new String(bStrm.toByteArray());
			bStrm.close();
		}
		return str;
	}
	
	
	public Bitmap downloadImage(String imageHttpAddress) throws IOException{
        URL imageUrl = null;
        imageUrl = new URL(imageHttpAddress);
        HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
        conn.connect();
        return BitmapFactory.decodeStream(conn.getInputStream());
        
    }

	public JSONObject getJsonObjectFromUrl(String url) throws JSONException, IOException{


		return new JSONObject(EntityUtils.toString(
				new DefaultHttpClient().execute(
						new HttpGet(url)).getEntity()));
	}
	
}
