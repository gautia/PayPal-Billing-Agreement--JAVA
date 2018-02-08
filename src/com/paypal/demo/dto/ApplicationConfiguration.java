package com.paypal.demo.dto;

public class ApplicationConfiguration {
	
	private String secret;
	private String accessTokenUrl;
	private String createBillingAgreementTokenUrl;
	private String createBillingAgreementUrl;
	private String getPaymentsDetailsUrl;
	private String expressCheckoutUrl;
	private String bnCode;
	private String cancelUrl;
	private String returnUrl;
	private String doPayment;
	
	private String clientId;
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}
	public void setAccessTokenUrl(String accessTokenUrl) {
		this.accessTokenUrl = accessTokenUrl;
	}
	public String getCreateBillingAgreementTokenUrl() {
		return createBillingAgreementTokenUrl;
	}
	public void setCreateBillingAgreementTokenUrl(String createBillingAgreementTokenUrl) {
		this.createBillingAgreementTokenUrl = createBillingAgreementTokenUrl;
	}
	public String getCreateBillingAgreementUrl() {
		return createBillingAgreementUrl;
	}
	public void setCreateBillingAgreementUrl(String createBillingAgreementUrl) {
		this.createBillingAgreementUrl = createBillingAgreementUrl;
	}
	public String getGetPaymentsDetailsUrl() {
		return getPaymentsDetailsUrl;
	}
	public void setGetPaymentsDetailsUrl(String getPaymentsDetailsUrl) {
		this.getPaymentsDetailsUrl = getPaymentsDetailsUrl;
	}
	public String getExpressCheckoutUrl() {
		return expressCheckoutUrl;
	}
	public void setExpressCheckoutUrl(String expressCheckoutUrl) {
		this.expressCheckoutUrl = expressCheckoutUrl;
	}
	public String getBnCode() {
		return bnCode;
	}
	public void setBnCode(String bnCode) {
		this.bnCode = bnCode;
	}
	public String getCancelUrl() {
		return cancelUrl;
	}
	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getDoPayment() {
		return doPayment;
	}
	public void setDoPayment(String doPayment) {
		this.doPayment = doPayment;
	}
	
}
