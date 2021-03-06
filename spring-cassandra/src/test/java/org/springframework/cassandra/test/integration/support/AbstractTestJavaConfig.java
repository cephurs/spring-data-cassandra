package org.springframework.cassandra.test.integration.support;

import org.springframework.cassandra.config.java.AbstractSessionConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class AbstractTestJavaConfig extends AbstractSessionConfiguration {

	public static BuildProperties PROPS = new BuildProperties();
	public static final int PORT = PROPS.getCassandraPort();

	@Override
	protected int getPort() {
		return PORT;
	}
}
