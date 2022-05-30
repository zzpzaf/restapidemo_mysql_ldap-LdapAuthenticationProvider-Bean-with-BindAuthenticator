package com.zzpzaf.restapidemo.Configuration;

import javax.servlet.http.HttpServletResponse;

import com.zzpzaf.restapidemo.Services.LdapUserAuthoritiesPopulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;


@Configuration
@EnableWebSecurity
public class CustomSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;
    
    @Autowired
    private LdapUserAuthoritiesPopulator ldapUserAuthoritiesPopulator;
 
       @Override
       protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().antMatchers("/api/items").hasRole("ADMIN")
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().httpBasic()
            .authenticationEntryPoint(
                    (request, response, authException) -> {
                        response.setHeader("WWW-Authenticate", "Basic realm=" + env.getProperty("app.realm") + "");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No Authentication !!!");
                    }
            );
       }
        
    @Bean
    LdapAuthenticationProvider ldapAuthenticationProvider() {
        return new LdapAuthenticationProvider(authenticator(), ldapUserAuthoritiesPopulator);
    }

    @Bean
    BindAuthenticator authenticator() {
        String searchBase = "";
        String filter = env.getProperty("ldap.search.filter");
        assert filter != null;

        FilterBasedLdapUserSearch search = new FilterBasedLdapUserSearch(searchBase, filter, contextSource());

        BindAuthenticator authenticator = new BindAuthenticator(contextSource());
        authenticator.setUserSearch(search);

        return authenticator;
    }

    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        DefaultSpringSecurityContextSource defSecCntx = new DefaultSpringSecurityContextSource(env.getProperty("spring.ldap.urls") + "/" + env.getProperty("spring.ldap.base"));
        defSecCntx.setUserDn(env.getProperty("ldap.managerDn") + "," + env.getProperty("spring.ldap.base"));
        defSecCntx.setPassword(env.getProperty("spring.ldap.password"));
        return defSecCntx;
    }


}

