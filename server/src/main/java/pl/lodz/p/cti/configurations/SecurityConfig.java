package pl.lodz.p.cti.configurations;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.validation.constraints.NotNull;

/**
 * @author Mateusz Wieczorek
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccessDeniedHandler accessDeniedHandler;
    private static final String ADMIN_ROLE = "ADMIN";

    @NotNull
    @Value("${app.security.user}")
    private String user;

    @NotNull
    @Value("${app.security.password}")
    private String password;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable().and()
                .authorizeRequests()
                    .antMatchers("/", "/index", "/getObject/**", "/tv/**", "/cti-websocket/**").permitAll()
                    .antMatchers("/configuration/**").hasAnyRole(ADMIN_ROLE)
                    .antMatchers("/removeCollection/**").hasAnyRole(ADMIN_ROLE)
                    .antMatchers("/addCollection/**").hasAnyRole(ADMIN_ROLE)
                    .antMatchers("/modifyCollection/**").hasAnyRole(ADMIN_ROLE)
                    .antMatchers("/removePresentation/**").hasAnyRole(ADMIN_ROLE)
                    .antMatchers("/addPresentation/**").hasAnyRole(ADMIN_ROLE)
                    .antMatchers("/modifyPresentation/**").hasAnyRole(ADMIN_ROLE)
                    .antMatchers("/addObject/**").hasAnyRole(ADMIN_ROLE)
                    .antMatchers("/modifyObject/**").hasAnyRole(ADMIN_ROLE)
                    .antMatchers("/removeObject/**").hasAnyRole(ADMIN_ROLE)
                    .antMatchers("/modifySchedule/**").hasAnyRole(ADMIN_ROLE)
                    .antMatchers("/objects/**").hasAnyRole(ADMIN_ROLE)
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/webjars/**", "/css/**", "/fonts/**", "/libs/**")
                .antMatchers("/resources/**", "/static/**", "/js/**", "/images/**")
                .antMatchers("/Widget/**", "/widgetlist.xml");
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(user).password(password).roles(ADMIN_ROLE);
    }

}