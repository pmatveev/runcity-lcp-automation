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
	public static final String DATE_FORMAT = "yyyy MM dd";
	public static final String DATE_FORMAT_JS = "yyyy mm dd";
	public static final String DATE_TIME_FORMAT = "yyyy MM dd HH mm";
	public static final String DATE_TIME_FORMAT_JS = "yyyy mm dd hh ii";
	public static final String DATE_TIMESTAMP_FORMAT = "yyyy MM dd HH mm ss";
	public static final String TIMESTAMP_FORMAT = "HH mm ss";

	@Resource
	private Environment env;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(env.getRequiredProperty("db.driver"));
		dataSource.setUrl(env.getRequiredProperty("db.url"));
		dataSource.setUsername(env.getRequiredProperty("db.username"));

		String password = env.getRequiredProperty("db.password");
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

		String[] locales = env.getRequiredProperty("runcity.langlist").split(",");

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
		properties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
		properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
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