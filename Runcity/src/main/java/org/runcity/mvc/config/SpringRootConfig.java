package org.runcity.mvc.config;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.ejb.HibernatePersistence;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.PasswordCipher;
import org.runcity.util.Version;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("/WEB-INF/conf/app.properties")
@EnableJpaRepositories("org.runcity.db.repository")
@ComponentScan({ "org.runcity.db.entity" })
public class SpringRootConfig {
	public static final String DATE_FORMAT = "yyyy mm dd";
	private static final String PROP_DATABASE_DRIVER = "db.driver";
	private static final String PROP_DATABASE_PASSWORD = "db.password";
	private static final String PROP_DATABASE_URL = "db.url";
	private static final String PROP_DATABASE_USERNAME = "db.username";
	private static final String PROP_LANGLIST = "runcity.langlist";
	private static final String PROP_HIBERNATE_DIALECT = "hibernate.dialect";
	private static final String PROP_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	private static final String PROP_HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

	@Resource
	private Environment env;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(env.getRequiredProperty(PROP_DATABASE_DRIVER));
		dataSource.setUrl(env.getRequiredProperty(PROP_DATABASE_URL));
		dataSource.setUsername(env.getRequiredProperty(PROP_DATABASE_USERNAME));

		String password = env.getRequiredProperty(PROP_DATABASE_PASSWORD);
		if (password.startsWith("clear:")) {
			password = password.substring(6);
		} else {
			try {
				password = new PasswordCipher("dbpasswd").decrypt(password);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		dataSource.setPassword(password);

		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistence.class);
		entityManagerFactoryBean.setPackagesToScan("org.runcity.db.entity");

		entityManagerFactoryBean.setJpaProperties(getHibernateProperties());

		return entityManagerFactoryBean;
	}

	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

		return transactionManager;
	}

	@Bean
	public DynamicLocaleList getLocaleList() {
		DynamicLocaleList localeList = new DynamicLocaleList();

		String[] locales = env.getRequiredProperty(PROP_LANGLIST).split(",");

		for (int i = 0; i < locales.length; i++) {
			localeList.add(locales[i]);
		}

		return localeList;
	}

	@Bean
	public Version getVersion() {
		return new Version(this.getClass().getPackage().getImplementationVersion());
	}

	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put(PROP_HIBERNATE_DIALECT, env.getRequiredProperty(PROP_HIBERNATE_DIALECT));
		properties.put(PROP_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROP_HIBERNATE_SHOW_SQL));
		properties.put(PROP_HIBERNATE_HBM2DDL_AUTO, env.getRequiredProperty(PROP_HIBERNATE_HBM2DDL_AUTO));
		return properties;
	}

	@Bean
	public JavaMailSender getMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(env.getRequiredProperty("runcity.mail.host"));
		mailSender.setPort(new Integer(env.getRequiredProperty("runcity.mail.port")));
		mailSender.setUsername(env.getRequiredProperty("runcity.mail.username"));
		
		String password = env.getRequiredProperty("runcity.mail.password");
		if (password.startsWith("clear:")) {
			password = password.substring(6);
		} else {
			try {
				password = new PasswordCipher("mailpass").decrypt(password);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		mailSender.setPassword(password);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", env.getRequiredProperty("runcity.mail.tlsEnabled"));

		return mailSender;
	}
}