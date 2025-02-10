package vttp2023.batch4.paf.assessment.services;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class ForexService {

	// TODO: Task 5 
	public float convert(String from, String to, float amount) {

		String upperCaseFrom = from.toUpperCase();
		String upperCaseTo = to.toUpperCase();

		String baseUrl = "https://api.frankfurter.dev/v1/latest";

		String url = UriComponentsBuilder
			.fromUriString(baseUrl)
			.queryParam("base", upperCaseFrom)
			.queryParam("symbols", upperCaseTo)
			.toUriString();

		RequestEntity<Void> req = RequestEntity
			.get(url)
			.accept(MediaType.APPLICATION_JSON)
			.build();
		
		RestTemplate restTemplate = new RestTemplate();

		try {
			ResponseEntity<String> resp = restTemplate.exchange(req, String.class);

			String payload = resp.getBody();

			JsonReader reader = Json.createReader(new StringReader(payload));

			JsonObject payloadObj = reader.readObject();

			JsonObject ratesObj = payloadObj.getJsonObject("rates");

			float exchangeRate = (float) ratesObj.getJsonNumber(upperCaseTo).doubleValue();

			return amount * exchangeRate;
		}

		catch (Exception e) {
			e.printStackTrace();

			return -1000f;
		}
	}
}
