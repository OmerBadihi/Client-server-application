package com.srccodes.examples;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Currency {
	//https://free.currconv.com/api/v7/convert?q=USD_PHP&compact=ultra&apiKey=17e0439a3227da4193bf
	private Double Salary_in_Local_Currency;
	
	@SuppressWarnings("unchecked")
	public Currency(String currency, Employee employee) throws IOException {
		String apiKey = "17e0439a3227da4193bf";
		HttpClient httpClient = HttpClient.newHttpClient();
		ObjectMapper objectMapper = new ObjectMapper()
				.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		String httpResponse = null;
		currency = "USD_" + currency;//"USD_ILS"
		
		try {
			HttpRequest convertRequest = null;
			convertRequest = HttpRequest.newBuilder().
					  uri(new URI(String.format("https://free.currconv.com/api/v7/convert?q=%s&compact=ultra&apiKey=%s", currency, apiKey)))
					  .GET()
					  .build();
			httpResponse = httpClient.send(convertRequest, BodyHandlers.ofString()).body();
			Map<String, Double> map = objectMapper.readValue(httpResponse, Map.class);
		
			Salary_in_Local_Currency = map.get(currency)
									* employee.Salary_in_USD();
			
			
		} catch (URISyntaxException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	public Double Salary_in_Local_Currency() {
		return Salary_in_Local_Currency;
	}
//
//	public void setSalary_in_Local_Currency(Double salary_in_Local_Currency) {
//		Salary_in_Local_Currency = salary_in_Local_Currency;
//	}
	
}
