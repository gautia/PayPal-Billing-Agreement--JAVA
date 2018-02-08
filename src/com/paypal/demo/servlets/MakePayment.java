package com.paypal.demo.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.paypal.demo.dto.ApplicationConfiguration;
import com.paypal.demo.dto.CreatePaymentDto;
import com.paypal.demo.helpers.Helper;
import com.paypal.demo.helpers.RestClient;

/**
 * This class handles CreatePayment API calls to paypal
 * Gets the payload from html and add access Token and sends the request as Post to paypal server
 * 
 */
@WebServlet("/MakePayment")
public class MakePayment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MakePayment() {
        super();
        // TODO Auto-generated constructor stub
    }
    

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			//load the config 
			ApplicationConfiguration ac =  (ApplicationConfiguration) getServletContext().getAttribute("config");
			HttpSession session = request.getSession();
			String billingAgreementID=(String)session.getAttribute("billingAgreementID");
			//billingAgreementID="B-9EX82222N7562991A";
			Random random = new Random();
			int  rand = random.nextInt(50) + 1;
			String randomString=rand+"ABCDEGFGh"+123;
			//CreatePaymentDto createPaymentDto = getCreatePaymentObject(request); // Helper method to get the payload data
	        //String json="{\\r\\n    \\\"intent\\\": \\\"sale\\\",\\r\\n    \\\"payer\\\":\\r\\n    {\\r\\n        \\\"payment_method\\\": \\\"PAYPAL\\\",\\r\\n        \\\"funding_instruments\\\": [\\r\\n        {\\r\\n            \\\"billing\\\":\\r\\n            {\\r\\n                \\\"billing_agreement_id\\\": \\\""+billingAgreementID+"\\\"\\r\\n            }\\r\\n        }]\\r\\n    },\\r\\n    \\\"transactions\\\": [\\r\\n    {\\r\\n        \\\"amount\\\":\\r\\n        {\\r\\n            \\\"currency\\\": \\\"USD\\\",\\r\\n            \\\"total\\\": \\\"1.00\\\"\\r\\n        },\\r\\n        \\\"description\\\": \\\"Payment transaction.\\\",\\r\\n        \\\"custom\\\": \\\"Payment custom field.\\\",\\r\\n        \\\"note_to_payee\\\": \\\"Note to payee field.\\\",\\r\\n        \\\"invoice_number\\\": \\\"GDAGDS5754YEK123\\\",\\r\\n        \\\"item_list\\\":\\r\\n        {\\r\\n            \\\"items\\\": [\\r\\n            {\\r\\n                \\\"sku\\\": \\\"skuitemNo1\\\",\\r\\n                \\\"name\\\": \\\"ItemNo1\\\",\\r\\n                \\\"description\\\": \\\"The item description.\\\",\\r\\n                \\\"quantity\\\": \\\"1\\\",\\r\\n                \\\"price\\\": \\\"1.00\\\",\\r\\n                \\\"currency\\\": \\\"USD\\\",\\r\\n                \\\"tax\\\": \\\"0\\\",\\r\\n                \\\"url\\\": \\\"https://www.example.com/\\\"\\r\\n            }]\\r\\n        }\\r\\n    }],\\r\\n    \\\"redirect_urls\\\":\\r\\n    {\\r\\n        \\\"return_url\\\": \\\"https://www.example.com/return\\\",\\r\\n        \\\"cancel_url\\\": \\\"https://www.example.com/cancel\\\"\\r\\n    }\\r\\n}";
			String JSON="{\n" + 
					"    \"intent\": \"sale\",\n" + 
					"    \"payer\":\n" + 
					"    {\n" + 
					"        \"payment_method\": \"PAYPAL\",\n" + 
					"        \"funding_instruments\": [\n" + 
					"        {\n" + 
					"            \"billing\":\n" + 
					"            {\n" + 
					"                \"billing_agreement_id\": \""+billingAgreementID+"\"\n" + 
					"            }\n" + 
					"        }]\n" + 
					"    },\n" + 
					"    \"transactions\": [\n" + 
					"    {\n" + 
					"        \"amount\":\n" + 
					"        {\n" + 
					"            \"currency\": \"USD\",\n" + 
					"            \"total\": \"1.00\"\n" + 
					"        }\n" + 
					"    }]\n" + 
					"}";
	        RestClient restClient = new RestClient();
	        JSONObject accessTokenObjectFromPayPalServer = restClient.getAccessToken(ac.getClientId(), ac.getSecret(), ac.getAccessTokenUrl(), ac.getBnCode());
			String accessToken = accessTokenObjectFromPayPalServer.getString("access_token");
			JSONObject dataFromCreatePaymentsAPI = restClient.makePayment(accessToken, ac.getDoPayment(), JSON, ac.getBnCode());
		
			response.setContentType("application/json");
			response.setStatus(200);
			PrintWriter out = response.getWriter();
			out.print(dataFromCreatePaymentsAPI); // post back the object to invoker
			
		}catch(Exception e) {
			response.setStatus(500);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Map<String, String> error = new HashMap<String, String>();
			error.put("error", e.toString());
			out.print(error);
		}
        
		
	}
	
	public void loadApplicationProperties(HttpServletRequest request) {
		// load properties to get url and other credentials
		try {
			InputStream input = getServletContext().getResourceAsStream("/WEB-INF/application.properties");
			Properties properties = new Properties();
			properties.load(input);
			
			ApplicationConfiguration ac = new ApplicationConfiguration();
			
			// check the application property wheather the app is in live or sandbox and load config accordingly
			if(properties.getProperty("IS_APPLICATION_IN_SANDBOX").toString().equals("true")) {
				// load all properties for sandbox
				
				ac.setAccessTokenUrl(properties.getProperty("ACCESS_TOKEN_URL").toString());
				ac.setClientId(properties.getProperty("CLIENT_ID").toString());
				ac.setCreateBillingAgreementTokenUrl(properties.getProperty("CREATE_PAYMENT_URL").toString());
				ac.setCreateBillingAgreementUrl(properties.getProperty("EXECUTE_PAYMENT_URL").toString());
				ac.setExpressCheckoutUrl(properties.getProperty("EXPRESS_CHECKOUT_SANDBOX_URL").toString());
				ac.setGetPaymentsDetailsUrl(properties.getProperty("GET_PAYMENT_DETAILS").toString());
				ac.setSecret(properties.getProperty("SECRET").toString());
				ac.setDoPayment(properties.getProperty("DO_PAYMENTS_URL").toString());
				
			}else {
				
				// load all properties for live
				ac.setAccessTokenUrl(properties.getProperty("ACCESS_TOKEN_URL_LIVE").toString());
				ac.setClientId(properties.getProperty("CLIENT_ID_LIVE").toString());
				ac.setCreateBillingAgreementTokenUrl(properties.getProperty("CREATE_PAYMENT_URL_LIVE").toString());
				ac.setCreateBillingAgreementUrl(properties.getProperty("EXECUTE_PAYMENT_URL_LIVE").toString());
				ac.setExpressCheckoutUrl(properties.getProperty("EXPRESS_CHECKOUT_LIVE_URL").toString());
				ac.setGetPaymentsDetailsUrl(properties.getProperty("GET_PAYMENT_DETAILS_LIVE").toString());
				ac.setSecret(properties.getProperty("SECRET_LIVE").toString());
			}
			
			 HttpSession session = request.getSession();  
		     session.setAttribute("config", ac);  
			
		}catch(Exception e) {
			System.out.println("Failed to load Application properties :" + e);
		}
        
	}
	
	private CreatePaymentDto getCreatePaymentObject(HttpServletRequest request) throws IOException {
		String BASEURL = request.getScheme() + "://"+ request.getServerName() + ((request.getServerPort() == 80) ? "" : ":" + request.getServerPort()) + request.getContextPath();
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        if(br != null) {
            json = br.readLine();
        }
       
        JSONObject dataFromHtml = new JSONObject(json);
        Helper helper = new Helper();
        ApplicationConfiguration ac =  (ApplicationConfiguration) getServletContext().getAttribute("config");
        CreatePaymentDto createPaymentDto = helper.getCreatePaymentData(BASEURL, dataFromHtml,ac.getReturnUrl(),ac.getCancelUrl());
        return createPaymentDto;
                
	}

}
