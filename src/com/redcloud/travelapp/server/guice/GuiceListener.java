package com.redcloud.travelapp.server.guice;

import javax.servlet.annotation.WebListener;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * GUICE JPA Database Initialization
 * Based on the persistence.xml configs
 * @author Sharadha
 *
 */
@WebListener
public class GuiceListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		Injector injector = Guice.createInjector(new JpaPersistModule("db_manager"));
		injector.getInstance(JpaInitializer.class);
		return injector;
	}
}

class JpaInitializer {
	@Inject
	public JpaInitializer(PersistService persistService) {}
}
