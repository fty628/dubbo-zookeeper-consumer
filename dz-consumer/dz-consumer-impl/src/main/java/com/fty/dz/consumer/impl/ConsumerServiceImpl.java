package com.fty.dz.consumer.impl;

import javax.annotation.Resource;

import org.dz.server.api.IProviderService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fty.dz.consumer.api.IConsumerService;

/**
 * Hello world!
 *
 */
@Service("consumerService")
@Component
public class ConsumerServiceImpl implements IConsumerService {

	@Resource
	private IProviderService providerService;
	
	@Override
	public String say(String name) {
		return providerService.saySomething(name);
	}
}
