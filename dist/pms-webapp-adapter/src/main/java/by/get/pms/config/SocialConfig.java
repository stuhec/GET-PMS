package by.get.pms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.github.connect.GitHubConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;

import javax.sql.DataSource;

/**
 * Created by milos on 20-Nov-16.
 */
@Configuration
@PropertySource("classpath:/config/security.properties")
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	public ConnectionSignUp accountConnectionSignUpService;

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer,
			Environment environment) {
		FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(
				environment.getProperty("facebook.appId"), environment.getProperty("facebook.appSecret"));
		facebookConnectionFactory.setScope("public_profile, email");

		GitHubConnectionFactory gitHubConnectionFactory = new GitHubConnectionFactory(
				(environment.getProperty("github.appId")), environment.getProperty("github.appSecret"));

		connectionFactoryConfigurer.addConnectionFactory(facebookConnectionFactory);
		connectionFactoryConfigurer.addConnectionFactory(gitHubConnectionFactory);
	}

	// way to identify current user - from Spring Security Context
	@Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	}

	// UsersConnectionRepository - factory to create request-scoped (per-user) ConnectionRepository bean
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		// stores connections to UserConnection table
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator, Encryptors.noOpText());
		repository.setConnectionSignUp(accountConnectionSignUpService);
		return repository;
	}
}
