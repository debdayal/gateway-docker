package com.example;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.dto.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/gateway/api/users")
public class UserController {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.service.url.user-service}")
	private String userServiceUrl;
	
	@HystrixCommand(fallbackMethod="getUsersFallback")
	@RequestMapping(method = RequestMethod.GET)
    public Collection<User> findAll() {
		ParameterizedTypeReference<Collection<User>> ptr = new ParameterizedTypeReference<Collection<User>>(){};
		
		ResponseEntity<Collection<User>> responseEntity = 
                restTemplate.exchange(userServiceUrl, HttpMethod.GET, null, ptr);

		return responseEntity.getBody();
	}
	
	@RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
		ParameterizedTypeReference<User> ptr = new ParameterizedTypeReference<User>(){};
		HttpEntity<User> userEntity = new HttpEntity<User>(user);
		
		ResponseEntity<User> responseEntity = 
                restTemplate.exchange(userServiceUrl, HttpMethod.POST, userEntity, ptr);
        return responseEntity.getBody();
    }
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") String id) {
		ParameterizedTypeReference<User> ptr = new ParameterizedTypeReference<User>(){};
		restTemplate.exchange(userServiceUrl + "{id}", HttpMethod.DELETE, null, ptr, id);
    }
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public User findById(@PathVariable("id") String id) {
		
		ParameterizedTypeReference<User> ptr = new ParameterizedTypeReference<User>(){};
		
		ResponseEntity<User> responseEntity = 
                restTemplate.exchange(userServiceUrl + "{id}", HttpMethod.GET, null, ptr,id);

		return responseEntity.getBody();
    }
 
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public User update(@RequestBody User user, @PathVariable("id") String id) {
    	ParameterizedTypeReference<User> ptr = new ParameterizedTypeReference<User>(){};
		HttpEntity<User> userEntity = new HttpEntity<User>(user);
		
		ResponseEntity<User> responseEntity = 
                restTemplate.exchange(userServiceUrl + "{id}", HttpMethod.PUT, userEntity, ptr, id);
        return responseEntity.getBody();
    }
    
    public Collection<User> getUsersFallback(){
    	return Collections.EMPTY_LIST;
    }
	
}
