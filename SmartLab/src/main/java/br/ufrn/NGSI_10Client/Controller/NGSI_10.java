package br.ufrn.NGSI_10Client.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

import br.ufrn.NGSI_10Client.entities.UpdateContext;

public class NGSI_10 {

	private static String OrionBrokerAddress;
	private static String token;

	public NGSI_10(String orionBrokerAddress, String token) {
		super();
		NGSI_10.OrionBrokerAddress = orionBrokerAddress;
		this.token = token;
	}

	public void updateContext(UpdateContext updateContext) throws ClientProtocolException, IOException {


		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(OrionBrokerAddress+"/v1/updateContext");

		// add header
		post.setHeader("X-Auth-Token", token);

		StringEntity requestEntity = new StringEntity(
				new Gson().toJson(updateContext), ContentType.APPLICATION_JSON);
		
		System.out.println(new Gson().toJson(updateContext));
		
		post.setEntity(requestEntity);

		HttpResponse response = client.execute(post);
		
		BufferedReader rd = new BufferedReader(
		        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		
		System.out.println(result);
	}

}
