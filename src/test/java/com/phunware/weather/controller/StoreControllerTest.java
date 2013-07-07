package com.phunware.weather.controller;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.google.gson.Gson;
import com.phunware.weather.ConfigApp;
import com.phunware.weather.db.domain.LowHightAvgTempByZipCode;
import com.phunware.weather.db.domain.WeatherByZipCode;
import com.phunware.weather.db.domain.ZipCode;
import com.phunware.weather.dto.Message;
import com.phunware.weather.dto.StoreStatus;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (value ="classpath:testspringapp-servlet.xml")
public class StoreControllerTest {
	@Inject
    private  ApplicationContext applicationContext;
    private  RequestMappingHandlerAdapter  handlerAdapter;
	@Inject
	private  StoreController  storeController;
	@Inject
	private Gson gson;
	
	@Inject
	MongoTemplate template;

	@Before
	public  void setup() throws Exception {
		
	    handlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter .class);
        storeController = applicationContext.getBean(StoreController.class);
        applicationContext.getBean(ConfigApp.class);
        template= applicationContext.getBean(MongoTemplate.class);
        
        
	}
	
	

	
	@Test
	public void testPopulateStoreOne() throws Exception {
		testDeleteStoreOne();
		long rows = template.count(new Query(), ZipCode.class);
		Assert.assertEquals(0, rows);
		testInitStoreOne();
		rows = template.count(new Query(), ZipCode.class);
		Assert.assertEquals(100, rows);
	}
	
	
	@Test
	public void testPopulateStoreTwo() throws Exception {
		
		long rows = template.count(new Query(), ZipCode.class);
		if(rows==0) {
			testInitStoreOne();
			rows = template.count(new Query(), ZipCode.class);
			Assert.assertEquals(100, rows);
		}
		
		
		testDeleteStoreTwo();
		rows = template.count(new Query(), WeatherByZipCode.class);
		Assert.assertEquals(0, rows);
		testInitStoreTwo();
		long expected = testStoreTwoStatus();
		rows = template.count(new Query(), WeatherByZipCode.class);
		Assert.assertEquals(expected, rows);
	}
	
	@Test
	public void testPopulateStoreThree() throws Exception {
		
		long rows = template.count(new Query(), WeatherByZipCode.class);
		if(rows==0) {
			testInitStoreTwo();
			long expected = testStoreTwoStatus();
			rows = template.count(new Query(), WeatherByZipCode.class);
			Assert.assertEquals(expected, rows);
		}
		testDeleteStoreThree();
		rows = template.count(new Query(), LowHightAvgTempByZipCode.class);
		Assert.assertEquals(0, rows);
		long expected = testInitStoreThree();
		rows = template.count(new Query(), LowHightAvgTempByZipCode.class);
		Assert.assertEquals(expected, rows);
		
	}
	
	/* Store Three Start */
		
	private void testDeleteStoreThree() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET","/delete/storethree");
		MockHttpServletResponse response = new MockHttpServletResponse();
        String result = handle(request, response);
        Message msg = gson.fromJson(result, Message.class);
        Assert.assertEquals("Store[3] truncated successfully!",msg.getMessage());
	}
	
	private long testInitStoreThree() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET","/init/storethree");
		MockHttpServletResponse response = new MockHttpServletResponse();
        String result = handle(request, response);
        StoreStatus msg = gson.fromJson(result, StoreStatus.class);
        Assert.assertEquals("Stroe[3] populated with low, high and avg temperature successfully![Using Map Red :)]",msg.getStatusMsg());
        return msg.getRows();
	}
	
	/* Store Three End */
	
	/* Store Two Start */
	private void testDeleteStoreTwo() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET","/delete/storetwo");
		MockHttpServletResponse response = new MockHttpServletResponse();
        String result = handle(request, response);
        Message msg = gson.fromJson(result, Message.class);
        Assert.assertEquals("Stroe[2] truncated successfully!",msg.getMessage());
	}
	
	
	private void testInitStoreTwo() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET","/init/storetwo");
		MockHttpServletResponse response = new MockHttpServletResponse();
        String result = handle(request, response);
        Message msg = gson.fromJson(result, Message.class);
        Assert.assertEquals("process started for Stroe[2] successfully! Please use [/init/storetwo/status] url for status.",msg.getMessage());

	}
	
	
	
	private long testStoreTwoStatus() throws Exception {
        
		MockHttpServletRequest request = new MockHttpServletRequest("GET","/init/storetwo/status");
		MockHttpServletResponse response = null;
		StoreStatus msg = new StoreStatus();
		msg.setProgress(true);
		while(msg.isProgress()){
			response = new MockHttpServletResponse();
         	msg = gson.fromJson(handle(request, response), StoreStatus.class);
         	Thread.sleep(1000);
		}
		
		return msg.getRows();
	}
	
	/* Store Two End */
	
	/* Store Two Start */
	private void testDeleteStoreOne() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET","/delete/storeone");
		MockHttpServletResponse response = new MockHttpServletResponse();
        String result = handle(request, response);
        Message msg = gson.fromJson(result, Message.class);
        Assert.assertEquals("Deleted successfully!",msg.getMessage());
	}
	
	
	private void testInitStoreOne() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET","/init/storeone");
		MockHttpServletResponse response = new MockHttpServletResponse();
        String result = handle(request, response);
        Message msg = gson.fromJson(result, Message.class);
        Assert.assertEquals("100 item(s) inserted successfully! ",msg.getMessage());
	}
	
	/* Store One End */
	
	
	protected String handle(MockHttpServletRequest request, MockHttpServletResponse response)
	            throws Exception {
	        final HandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
	        final HandlerExecutionChain handler = handlerMapping.getHandler(request);

	      //  assertNotNull("No handler found for request, check you request mapping", handler);

	        log.debug("handler: " + handler);
	        
	        final Object controller = handler.getHandler();
	        
	        log.debug("controller: " + controller);
	        // if you want to override any injected attributes do it here

	        final HandlerInterceptor[] interceptors =
	                handlerMapping.getHandler(request).getInterceptors();
	        
	        for (HandlerInterceptor interceptor : interceptors) {
	            log.debug("interceptor: " + interceptor);
	            final boolean carryOn = interceptor.preHandle(request, response, controller);
	            if (!carryOn) {
	                return null;
	            }
	        }

	        handlerAdapter.handle(request, response, controller);
	        return response.getContentAsString();
	    }
	

}
