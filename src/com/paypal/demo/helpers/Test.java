package com.paypal.demo.helpers;
import java.io.IOException;

import org.json.JSONObject;

import okhttp3.ConnectionSpec;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			OkHttpClient client = new OkHttpClient();

			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\r\n    \"description\": \"Billing Agreement\",\r\n    \"shipping_address\":\r\n    {\r\n        \"line1\": \"1350 North First Street\",\r\n        \"city\": \"San Jose\",\r\n        \"state\": \"CA\",\r\n        \"postal_code\": \"95112\",\r\n        \"country_code\": \"US\",\r\n        \"recipient_name\": \"John Doe\"\r\n    },\r\n    \"payer\":\r\n    {\r\n        \"payment_method\": \"PAYPAL\"\r\n    },\r\n    \"plan\":\r\n    {\r\n        \"type\": \"MERCHANT_INITIATED_BILLING\",\r\n        \"merchant_preferences\":\r\n        {\r\n            \"return_url\": \"https://www.example.com/return\",\r\n            \"cancel_url\": \"https://www.example.com/cancel\",\r\n            \"notify_url\": \"https://www.example.com/notify\",\r\n            \"accepted_pymt_type\": \"INSTANT\",\r\n            \"skip_shipping_address\": false,\r\n            \"immutable_shipping_address\": true\r\n        }\r\n    }\r\n}");
			Request request = new Request.Builder()
			  .url("https://api.sandbox.paypal.com/v1/billing-agreements/agreement-tokens")
			  .post(body)
			  .addHeader("content-type", "application/json")
			  .addHeader("authorization", "Bearer123 A21AAFf-4eSl1_kIaiTk6eQ9WyeDYXAD9ym_CbKuFQnveJBNMEHqQrdvt1zMh-Y69HsmMbnZYuIk1PpbJCm_JaWDyoNVinODQ")
			  .addHeader("cache-control", "no-cache")
			  .addHeader("postman-token", "6c700a7a-3699-9abb-d7da-30386ed66425")
			  .build();

			Response response = client.newCall(request).execute();
			JSONObject myObject = new JSONObject(response);
			System.out.println(myObject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
