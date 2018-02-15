package com.paypal.demo.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import okhttp3.ConnectionSpec;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;


public class RestClient {

	public JSONObject getAccessToken(String clientId, String secret, String url, String bnCode) throws IOException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
	    headers.put("Authorization", getBasicBearerToken(clientId, secret));
	    headers.put("PayPal-Partner-Attribution-Id", bnCode);
	    String data = "grant_type=client_credentials";
	    JSONObject dataFromServer = post(data, headers, url);
		return dataFromServer;
	}
	
	public JSONObject createBillingAgreementToken(String accessToken, String url, String dataToSend, String bnCode) throws IOException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bearer "+accessToken);
		//headers.put("PayPal-Partner-Attribution-Id", bnCode);
		//JSONObject jsonString = new JSONObject(dataToSend);
		JSONObject dataFromCreateApi = post(dataToSend, headers, url);
		return dataFromCreateApi;
	}
	public JSONObject makePayment(String accessToken, String url, String dataToSend, String bnCode) throws IOException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bearer "+accessToken);
		//headers.put("PayPal-Partner-Attribution-Id", bnCode);
		//JSONObject jsonString = new JSONObject(dataToSend);
		JSONObject dataFromCreateApi = post(dataToSend, headers, url);
		return dataFromCreateApi;
	}
	public JSONObject createPayment(String accessToken)throws IOException {
		JSONObject myObject=null;
		try {
			OkHttpClient client = new OkHttpClient();

			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\r\n    \"description\": \"Billing Agreement\",\r\n    \"shipping_address\":\r\n    {\r\n        \"line1\": \"1350 North First Street\",\r\n        \"city\": \"San Jose\",\r\n        \"state\": \"CA\",\r\n        \"postal_code\": \"95112\",\r\n        \"country_code\": \"US\",\r\n        \"recipient_name\": \"John Doe\"\r\n    },\r\n    \"payer\":\r\n    {\r\n        \"payment_method\": \"PAYPAL\"\r\n    },\r\n    \"plan\":\r\n    {\r\n        \"type\": \"MERCHANT_INITIATED_BILLING\",\r\n        \"merchant_preferences\":\r\n        {\r\n            \"return_url\": \"https://www.example.com/return\",\r\n            \"cancel_url\": \"https://www.example.com/cancel\",\r\n            \"notify_url\": \"https://www.example.com/notify\",\r\n            \"accepted_pymt_type\": \"INSTANT\",\r\n            \"skip_shipping_address\": false,\r\n            \"immutable_shipping_address\": true\r\n        }\r\n    }\r\n}");
			Request request = new Request.Builder()
			  .url("https://api.sandbox.paypal.com/v1/billing-agreements/agreement-tokens")
			  .post(body)
			  .addHeader("content-type", "application/json")
			  .addHeader("authorization", "Bearer "+accessToken)
			  .addHeader("cache-control", "no-cache")
			  .addHeader("postman-token", "6c700a7a-3699-9abb-d7da-30386ed66425")
			  .build();

			Response response = client.newCall(request).execute();
			myObject = new JSONObject(response);
			System.out.println(myObject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myObject;
	}
	public JSONObject createBillingAgreement(String accessToken,  String url, Object dataToSend, String bnCode) throws IOException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bearer "+accessToken);
		headers.put("PayPal-Partner-Attribution-Id", bnCode);
		JSONObject dataFromExecuteApi = post(dataToSend.toString(), headers, url);
		return dataFromExecuteApi;
		
	}
	
	public JSONObject getPaymentDetails(String accessToken, String url, String bnCode) throws IOException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bearer "+accessToken);
		headers.put("PayPal-Partner-Attribution-Id", bnCode);
		JSONObject dataFromPayPal = get(headers, url);
		return dataFromPayPal;
	}

	public String getBasicBearerToken(String clientId, String secret) {
		String token = clientId.toString().trim() +":"+secret.toString().trim();
		token = token.replace("\"", "");
		Base64 b = new Base64();
		String accessToken = b.encodeAsString(new String(token).getBytes());
		return "Basic "+accessToken;
	}
	
	public JSONObject post(String data, Map<String, String> headers, String url) throws IOException {
		ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS) // Set TLS VERSION 1.2 
                .tlsVersions(TlsVersion.TLS_1_2)
                .build();
        List<ConnectionSpec> specs = new ArrayList<>();
	        specs.add(cs);
	        specs.add(ConnectionSpec.COMPATIBLE_TLS);
	        specs.add(ConnectionSpec.CLEARTEXT);
	        
		OkHttpClient client = new OkHttpClient.Builder()
		        .connectTimeout(36000, TimeUnit.SECONDS)
		        .writeTimeout(36000, TimeUnit.SECONDS)
		        .readTimeout(36000, TimeUnit.SECONDS)
		        .connectionSpecs(specs)
		        .build();
		MediaType mediaType = MediaType.parse(""); // set the media type to empty since the headers will have the needed the media type.
		Headers headerbuild = Headers.of(headers);
	    RequestBody body = RequestBody.create(mediaType, data);
	    Request request = new Request.Builder().url(url).headers(headerbuild).post(body).build();
	    Response response = client.newCall(request).execute();
	    String responseBody = response.body().string();
	    JSONObject jsonObj = new JSONObject(responseBody);
	    return jsonObj;
	}
	
	public JSONObject get(Map<String, String> headers,  String url) throws IOException {
		ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS) // Set TLS VERSION 1.2 
                .tlsVersions(TlsVersion.TLS_1_2)
                .build();
        List<ConnectionSpec> specs = new ArrayList<>();
	        specs.add(cs);
	        specs.add(ConnectionSpec.COMPATIBLE_TLS);
	        specs.add(ConnectionSpec.CLEARTEXT);
	        
		OkHttpClient client = new OkHttpClient.Builder()
		        .connectTimeout(36000, TimeUnit.SECONDS)
		        .writeTimeout(36000, TimeUnit.SECONDS)
		        .readTimeout(36000, TimeUnit.SECONDS)
		        .connectionSpecs(specs)
		        .build();
		Headers headerbuild = Headers.of(headers);
		Request request = new Request.Builder().url(url).headers(headerbuild).build();
		Response response = client.newCall(request).execute();	     
		String responseBody = response.body().string();
		JSONObject jsonObj = new JSONObject(responseBody);
		return jsonObj;
	}
	
	

}