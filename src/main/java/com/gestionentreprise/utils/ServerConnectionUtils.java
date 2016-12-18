package com.gestionentreprise.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class ServerConnectionUtils {
	private static final String COMPTABILITE_SUFFIXE = "comptabilite/mobile/";

	private static final String PARAMETRAGE_SUFFIXE = "parametrage/mobile/";

	private static Properties properties;

	public static void setProperties(Properties properties) {
		ServerConnectionUtils.properties = properties;
	}

	private static String getServerUrl() {
		return properties.getProperty("server.url");
	}

	public static String getComptabiliteServerUrl() {
		return getServerUrl() + COMPTABILITE_SUFFIXE;
	}

	public static String getParametrageServerUrl() {
		return getServerUrl() + PARAMETRAGE_SUFFIXE;
	}

	private InputStream executeGetRequest(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		System.out.println(url.toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		if (connection.getResponseCode() / 100 != 2) {
			throw new IOException("Response not OK Version"
					+ connection.getResponseCode());
		}
		return connection.getInputStream();
	}

	private static InputStreamReader executePostRequest(String urlStr,
			Map<String, String> parametres) throws Exception {
		
		// Initialisation
		HttpPost httpPost = new HttpPost(urlStr);
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		
		String username = "mimie";
		String password = "zoe";
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);

		httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost));
				
		// Préparation paramètres
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : parametres.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse response = httpClient.execute(httpPost);
		return new InputStreamReader(response.getEntity().getContent());
	}

	public static StringBuilder executeRequest(String url,
			Map<String, String> parametres) throws Exception {
		InputStreamReader in = executePostRequest(url, parametres);
		BufferedReader reader = new BufferedReader(in);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb;
	}
}
