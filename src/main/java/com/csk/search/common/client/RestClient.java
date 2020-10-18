package com.csk.search.common.client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;

@Component
public class RestClient {

	private static final Log logger = LogFactory.getLog(RestClient.class);
	
	@Value("${restclient.connectTimeout}")
	private int connectTimeout;
	@Value("${restclient.socketTimeout}")
	private int socketTimeout;
	@Value("${restclient.connectionTTL}")
	private int connectionTTL;
	@Value("${restclient.concurrencyTotal}")
	private int concurrencyTotal;
	@Value("${restclient.concurrencyRoute}")
	private int concurrencyRoute;
	
	@PostConstruct
	public void init() {
		if( !Unirest.isRunning() ) {
			Unirest.config().setObjectMapper(new ObjectMapper() {
			    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			 
			    public String writeValue(Object value) {
			        try {
						return mapper.writeValueAsString(value);
					} catch (JsonProcessingException e) {
						logger.error("writeValueAsString fail.", e);
					}
					return null;
			    }
			 
			    public <T> T readValue(String value, Class<T> valueType) {
			        try {
						return mapper.readValue(value, valueType);
					} catch (JsonParseException e) {
						logger.error("readValue-parse fail.", e);
					} catch (JsonMappingException e) {
						logger.error("readValue-mapping fail.", e);
					} catch (IOException e) {
						logger.error("readValue-io fail.", e);
					}
			        
			        return null;
			    }
			})
			.connectTimeout(connectTimeout)
			.socketTimeout(socketTimeout)
			.connectionTTL(connectionTTL, TimeUnit.MINUTES)
			.concurrency(concurrencyTotal, concurrencyRoute);
			logger.info("Unirest initialized " + this.getClass().getName());
		}
	}
}
