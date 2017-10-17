package com.redcloud.travelapp.server.guice;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Guice Persistence Module
 * @author Sharadha
 *
 */
public class GuiceDBModule extends AbstractModule {

	  private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE
	      = new ThreadLocal<EntityManager>();

	  public void configure() {
	  }
	  
	  /**
	   * Entity Manager Settings
	   * @return
	   */
	  @Provides @Singleton
	  public EntityManagerFactory provideEntityManagerFactory() {
	    Map<String, String> properties = new HashMap<String, String>();
	    properties.put("hibernate.connection.driver_class","org.postgresql.Driver");
	    properties.put("hibernate.connection.url","jdbc:postgresql://localhost:5432/postgres");
	    properties.put("hibernate.connection.username","postgres");
	    properties.put("hibernate.connection.password","postpass");
	    properties.put("hibernate.connection.pool_size","10");
	    properties.put("hibernate.dialect","org.hibernate.dialect.PostgreSQLDialect");
	    properties.put("hibernate.hbm2ddl.auto","update");
	    properties.put("hibernate.id.new_generator_mappings","true");
	    properties.put("hibernate.listeners.envers.autoRegister","false");
	    properties.put("hibernate.show_sql","true");
	    properties.put("hibernate.default_batch_fetch_size","8");
	    properties.put("hibernate.max_fetch_depth","3");
	    properties.put("hibernate.cache.provider_class","org.hibernate.cache.NoCacheProvider");
	    //return Persistence.createEntityManagerFactory("db_manager", properties);
	    PersistenceUnitInfo info = archiverPersistenceUnitInfo();
	    return new HibernatePersistenceProvider()
				.createContainerEntityManagerFactory(info,properties);
	  }
	  
	  /**
	   * Entity Manager Instance
	   * @param entityManagerFactory
	   * @return
	   */
	  @Provides
	  public EntityManager provideEntityManager(EntityManagerFactory entityManagerFactory) {
	    EntityManager entityManager = ENTITY_MANAGER_CACHE.get();
	    if (entityManager == null) {
	      ENTITY_MANAGER_CACHE.set(entityManager = entityManagerFactory.createEntityManager());
	    }
	    return entityManager;
	  }
	  
	  /**
	   * Persistence Unit Info
	   * @return
	   */
	  private static PersistenceUnitInfo archiverPersistenceUnitInfo() {
		    return new PersistenceUnitInfo() {
		        @Override
		        public String getPersistenceUnitName() {
		            return "ApplicationPersistenceUnit";
		        }

		        @Override
		        public String getPersistenceProviderClassName() {
		            return "org.hibernate.jpa.HibernatePersistenceProvider";
		        }

		        @Override
		        public PersistenceUnitTransactionType getTransactionType() {
		            return PersistenceUnitTransactionType.RESOURCE_LOCAL;
		        }

		        @Override
		        public DataSource getJtaDataSource() {
		            return null;
		        }

		        @Override
		        public DataSource getNonJtaDataSource() {
		            return null;
		        }

		        @Override
		        public List<String> getMappingFileNames() {
		            return Collections.emptyList();
		        }

		        @Override
		        public List<URL> getJarFileUrls() {
		            try {
		                return Collections.list(this.getClass()
		                                            .getClassLoader()
		                                            .getResources(""));
		            } catch (IOException e) {
		                throw new UncheckedIOException(e);
		            }
		        }

		        @Override
		        public URL getPersistenceUnitRootUrl() {
		            return null;
		        }

		        @Override
		        public List<String> getManagedClassNames() {
		            //return Collections.emptyList();
		        	List<String> classNames = new ArrayList<String>();
		        	classNames.add("com.redcloud.travelapp.server.db.PlacesDB");
		        	return classNames;
		        }

		        @Override
		        public boolean excludeUnlistedClasses() {
		            return false;
		        }

		        @Override
		        public SharedCacheMode getSharedCacheMode() {
		            return null;
		        }

		        @Override
		        public ValidationMode getValidationMode() {
		            return null;
		        }

		        @Override
		        public Properties getProperties() {
		            return new Properties();
		        }

		        @Override
		        public String getPersistenceXMLSchemaVersion() {
		            return null;
		        }

		        @Override
		        public ClassLoader getClassLoader() {
		            return null;
		        }

		        @Override
		        public void addTransformer(ClassTransformer transformer) {

		        }

		        @Override
		        public ClassLoader getNewTempClassLoader() {
		            return null;
		        }
		    };
		}

	}