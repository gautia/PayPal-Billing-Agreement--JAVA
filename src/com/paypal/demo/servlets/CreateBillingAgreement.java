package com.paypal.demo.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.paypal.demo.dto.ApplicationConfiguration;
import com.paypal.demo.dto.TransactionAmountDto;
import com.paypal.demo.dto.TransactionDetailsDto;
import com.paypal.demo.helpers.RestClient;

/**
 * This class handles the Create Billing Agreement with Token api call to paypal serve
 * Function is invoked once the user authorize the Billing Agreement 
 */
@WebServlet("/CreateBillingAgreement")
public class CreateBillingAgreement extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateBillingAgreement() {
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
			
			String jsonFromHtml = readInputStreamForData(request); // holding the json from request
			JSONObject dataToSend = getExecutePaymentObject(jsonFromHtml); // helper to get execute payment API payload
			RestClient restClient = new RestClient();
		    JSONObject accessTokenObjectFromPayPalServer = restClient.getAccessToken(ac.getClientId(), ac.getSecret(), ac.getAccessTokenUrl(), ac.getBnCode());
			String accessToken = accessTokenObjectFromPayPalServer.getString("access_token");
			
			JSONObject dataFromcreateBillingAgreementtAPI = restClient.createBillingAgreement(accessToken, ac.getCreateBillingAgreementUrl(), dataToSend, ac.getBnCode());
			String billingAgreementID= dataFromcreateBillingAgreementtAPI.getString("id");
			HttpSession session = request.getSession();
			session.setAttribute("billingAgreementID", billingAgreementID);
			
			response.setContentType("application/json");
			response.setStatus(200);
			PrintWriter out = response.getWriter();
			out.print(dataFromcreateBillingAgreementtAPI);
			
		}catch(Exception e) {
			response.setStatus(500);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Map<String, String> error = new HashMap<String, String>();
			error.put("error", e.toString());
			out.print(error);
			
		}
	}
	
	private String readInputStreamForData(HttpServletRequest request) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        if(br != null) {
            json = br.readLine();
        }
        return json;
	}
	
	private JSONObject getExecutePaymentObject(String jsonFromHtml) throws IOException {
	    JSONObject json = new JSONObject(jsonFromHtml);
	    JSONObject dataToSend = new JSONObject();
		dataToSend.put("token_id", json.getString("billingToken"));
		return dataToSend;
	}
}
