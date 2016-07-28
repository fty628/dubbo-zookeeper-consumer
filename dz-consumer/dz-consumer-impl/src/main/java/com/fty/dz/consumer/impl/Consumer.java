package com.fty.dz.consumer.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.fty.dz.consumer.api.IConsumerService;
import com.google.common.util.concurrent.AbstractIdleService;


public class Consumer extends AbstractIdleService{

	protected static final Logger logger = LoggerFactory.getLogger(Consumer.class);

	private static final String[] CONFIG_LOCATIONS = new String[] { "classpath*:spring-config.xml"};

	private GenericXmlApplicationContext context;
	private String activeProfile = "dev";
	
	public Consumer(String activeProfile) {
		super();
		this.activeProfile = activeProfile;
	}

	public void test(){
		if(context != null){
			IConsumerService consumerService = (IConsumerService)context.getBean("consumerService");
			System.out.println("-----------------------------");
			for (int i = 0; i < 10; i++) {
				System.out.println(consumerService.say("Admin"));
			}
			System.out.println("-----------------------------");
		}
	}
	
	public static void main(String[] args) throws Exception {
		String activeProfile = "dev";
		if (args != null && args.length > 0) {
			activeProfile = args[0];
		}
		Consumer consumer = new Consumer(activeProfile);
		consumer.startAsync();
        try {
            Object lock = new Object();
            synchronized (lock) {
                while (true) {
                    lock.wait();
                }
            }
        } catch (InterruptedException ex) {
            System.err.println("ignoreinterruption");
        }
	}

	@Override
	protected void startUp() throws Exception {
		long beginTime = System.currentTimeMillis();
		try {
			System.out.println("INFO: Dubbo + Zookeeper Consumer starting ...");
			context = new GenericXmlApplicationContext();
			context.getEnvironment().setActiveProfiles(activeProfile);
			context.load(CONFIG_LOCATIONS);
			System.out.println("INFO: Dubbo + Zookeeper Consumer Environment -- " + activeProfile);
			context.refresh();
			System.out.println(context);
			long endTime = System.currentTimeMillis();
			System.out.println("INFO: Dubbo + Zookeeper Consumer startup in " + (endTime - beginTime) + " ms");
			context.registerShutdownHook();
			test();
		} catch (Exception e) {
			System.err.println("INFO: Dubbo + Zookeeper Consumer startup failure.");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	protected void shutDown() throws Exception {
		if(context != null){
			context.stop();
		}
	    System.out.println("-------------service stoppedsuccessfully-------------");
	}
}
