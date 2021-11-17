package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
public class TcpServerApplication {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(TcpServerApplication.class);
		System.in.read();
		ctx.close();
	}

	@Bean
	TcpNetServerConnectionFactory cf () {
		return new TcpNetServerConnectionFactory(9876);
	}

	@Bean
	TcpInboundGateway tcpGate() {
		TcpInboundGateway gateway = new TcpInboundGateway();
		gateway.setConnectionFactory(cf());
		gateway.setRequestChannel(requestChannel());
		return gateway;
	}

	@Bean
	public MessageChannel requestChannel() {
		return new DirectChannel();
	}

	@MessageEndpoint
	public static class Echo {
		@ServiceActivator(inputChannel = "requestChannel")
		public String echo(byte [] in) {

			System.out.println(new String(in));

			return "echo: " + new String(in);
		}
	}

	@Autowired
	private Environment env;
}
