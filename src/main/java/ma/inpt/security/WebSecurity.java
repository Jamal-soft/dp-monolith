package ma.inpt.security;


import ma.inpt.organisationService.organisation.OrganisationService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final OrganisationService organisationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public WebSecurity(OrganisationService organisationService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.organisationService = organisationService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, "/signup/image")
                .permitAll()

                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_IN_URL)
                .permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_REQUEST_URL)
                .permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_URL)
                .permitAll()

                .antMatchers(HttpMethod.POST, "/payment")
                .permitAll()


                .antMatchers(HttpMethod.GET, "/donors/*/sum-donations")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/donors")
                .permitAll()

                .antMatchers(HttpMethod.GET,"/organisations/verified")
                .permitAll()
                .antMatchers(HttpMethod.GET,"/organisations/projects")
                .permitAll()
                .antMatchers(HttpMethod.GET,"/organisations/projects/*")
                .permitAll()
                .antMatchers(SecurityConstants.UPDATECURRENTBALANCE)
                .permitAll()

                .anyRequest().authenticated().and()
                //.addFilter(getAuthenticationFilter())
                .addFilter(new AuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;

        http.headers().frameOptions().disable();


    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        final CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","OPTIONS"));
        configuration.setAllowCredentials(false);
        configuration.setAllowedHeaders(Arrays.asList("*"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
