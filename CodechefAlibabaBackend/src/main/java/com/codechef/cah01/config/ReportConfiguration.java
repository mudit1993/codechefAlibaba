package com.codechef.cah01.config;

import static org.eclipse.persistence.config.PersistenceUnitProperties.CACHE_SHARED_DEFAULT;
import static org.eclipse.persistence.config.PersistenceUnitProperties.CLASSLOADER;
import static org.eclipse.persistence.config.PersistenceUnitProperties.CREATE_OR_EXTEND;
import static org.eclipse.persistence.config.PersistenceUnitProperties.DDL_DATABASE_GENERATION;
import static org.eclipse.persistence.config.PersistenceUnitProperties.DDL_GENERATION;
import static org.eclipse.persistence.config.PersistenceUnitProperties.DDL_GENERATION_MODE;
import static org.eclipse.persistence.config.PersistenceUnitProperties.LOGGING_LEVEL;
import static org.eclipse.persistence.config.PersistenceUnitProperties.NONE;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.instrument.classloading.SimpleLoadTimeWeaver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableTransactionManagement
public class ReportConfiguration {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
    @Autowired
    private Environment env;

    /**
    * Based on a {@link DataSource} (provided using the method above), provides
    * a factory to create {@link javax.persistence.EntityManager} instances
    * 
     * @param dataSource
    *            dataSource for accessing the database
    * @return LocalContainerEntityManagerFactoryBean entityManagerFactory
    *         object
    */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
          LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
          entityManagerFactoryBean.setPackagesToScan("com.codechef.cah01");
          entityManagerFactoryBean.setPersistenceUnitName("name");
          entityManagerFactoryBean.setPersistenceProvider(new PersistenceProvider());
          entityManagerFactoryBean.setDataSource(dataSource);

          entityManagerFactoryBean.setJpaPropertyMap(getJPAProperties(dataSource.getClass().getClassLoader()));
          entityManagerFactoryBean.setLoadTimeWeaver(new SimpleLoadTimeWeaver());
          entityManagerFactoryBean.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());

          entityManagerFactoryBean.afterPropertiesSet();

          return entityManagerFactoryBean;
    }

    /**
    * Based on an {@link EntityManagerFactory} (provided using the method
    * above), provides a {@link JpaTransactionManager}
    * 
     * @param entityManagerFactory
    *            entityManagerFactory object to create JpaTransactionManager
    * @return JpaTransactionManager transactionManager instance
    */
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
          return new JpaTransactionManager(entityManagerFactory);
    }

    /**
    * Based on an {@link EntityManagerFactory} (provided using the method
    * above), provides a {@link EntityManager}
    * 
     * @param entityManagerFactory
    *            entityManagerFactory object to create entityManager
    * @return EntityManager entityManager instance
    */
    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
          return entityManagerFactory.createEntityManager();
    }
    /**
     * Sets required JPA properties in a HashMap to be used by
     * EntityManagerFactory
     * 
      * @param classLoader
     *            ClassLoader for the DataSource
     * @return HashMap map of String and Object containing JPA properties
     */
     private Map<String, Object> getJPAProperties(ClassLoader classLoader) {
           Map<String, Object> properties = new HashMap<>();

           String[] profiles = env.getActiveProfiles();
           // if we have spring.profiles.active already set
           if (ArrayUtils.isNotEmpty(profiles)) {
                  // if cloud profile is set
                  if (Arrays.asList(profiles).contains("CLOUD")) {
                         // if any other profile is set ex. Cloud
                         properties.put(DDL_GENERATION, NONE);
                  } else {
                         // any other profile set that cloud
                         properties.put(DDL_GENERATION, CREATE_OR_EXTEND);
                         properties.put(DDL_GENERATION_MODE, DDL_DATABASE_GENERATION);
                  }
           } else {
                  // if no spring.profiles.active is set and default profile active
                  properties.put(DDL_GENERATION, CREATE_OR_EXTEND);
                  properties.put(DDL_GENERATION_MODE, DDL_DATABASE_GENERATION);
           }
           properties.put(CLASSLOADER, classLoader);
           properties.put(LOGGING_LEVEL, "INFO");
           properties.put(CACHE_SHARED_DEFAULT, "false");

           return properties;
     }
	/**
     * This will generate JdbcTemplate Bean.
     * 
      * @param dataSource
     *            data source object for database operation.
     * @return JdbcTemplate jdbcTemplate instance
     */
     @Bean
     public JdbcTemplate jdbcTemplate(DataSource dataSource) {
           JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
           jdbcTemplate.setResultsMapCaseInsensitive(true);
           return jdbcTemplate;
     }
     
     @Bean
     public WebMvcConfigurer corsConfigurer() {
         return new WebMvcConfigurerAdapter() {
             @Override
             public void addCorsMappings(CorsRegistry registry) {
                 registry.addMapping("/**");
             }
         };
     }
	
}
