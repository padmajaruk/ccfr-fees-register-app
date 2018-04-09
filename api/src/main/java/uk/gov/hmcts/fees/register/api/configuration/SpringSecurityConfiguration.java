package uk.gov.hmcts.fees.register.api.configuration;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import uk.gov.hmcts.auth.checker.spring.useronly.AuthCheckerUserOnlyFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthCheckerUserOnlyFilter authCheckerFilter;

    @Override
    @SuppressFBWarnings(value = "SPRING_CSRF_PROTECTION_DISABLED", justification = "It's safe to disable CSRF protection as application is not being hit directly from the browser")
    protected void configure(HttpSecurity http) throws Exception {
        authCheckerFilter.setAuthenticationManager(authenticationManager());

        http
            .addFilter(authCheckerFilter)
            .sessionManagement().sessionCreationPolicy(STATELESS).and()
            .csrf().disable()
            .formLogin().disable()
            .logout().disable()
            .authorizeRequests()
            .antMatchers("/swagger-ui.html", "/webjars/springfox-swagger-ui/**", "/swagger-resources/**", "/v2/**", "/health", "/info").permitAll()
            .antMatchers(HttpMethod.POST,"/fees-register/ranged-fees", "/fees-register/fixed-fees", "/fees-register/fees/**/versions").hasAnyAuthority("freg-fee-create")
            .antMatchers(HttpMethod.POST, "/fees-register/bulk-fixed-fees").hasAuthority("freg-fee-create")
            .antMatchers(HttpMethod.PATCH, "/fees-register/fees/**/versions/**").hasAuthority("freg-fee-approve")
            .antMatchers(HttpMethod.PUT, "/fees-register/ranged-fees/**", "/fees-register/fixed-fees/**").hasAuthority("freg-fee-edit")
            .antMatchers(HttpMethod.DELETE, "/fees-register/fees/**", "/fees-register/**/versions/**").hasAuthority("freg-fee-delete")
            .antMatchers(HttpMethod.GET, "/fees-register/fees/**").permitAll();
    }
}
