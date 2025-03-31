package task.schedule.config;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import task.schedule.filter.LoginFilter;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<Filter> loginFilter() {
        FilterRegistrationBean<Filter> filterBean = new FilterRegistrationBean<>();

        filterBean.setFilter(new LoginFilter());
        filterBean.setOrder(1);
        filterBean.addUrlPatterns("/*");

        return filterBean;
    }
}
