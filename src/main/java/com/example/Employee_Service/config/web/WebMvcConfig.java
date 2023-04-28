package com.example.Employee_Service.config.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.the.common.config.i18n.SmartLocaleResolver;
import com.the.common.validator.regex.RegexHelper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Web mvc config.
 */
@Configuration
@EnableTransactionManagement
public class WebMvcConfig implements WebMvcConfigurer {

  /**
   * Locale resolver locale resolver.
   *
   * @return the locale resolver
   */
  @Bean
  public LocaleResolver localeResolver() {
    return new SmartLocaleResolver();
  }

  /**
   * Gets message source.
   *
   * @return the message source
   */
  @Bean(name = "messageSource")
  public MessageSource getMessageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames(
        "file:i18n/common/common",
        "file:i18n/messages/messages",
        "file:i18n/validator/validator",
        "file:i18n/field/field");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  /**
   * Validator local validator factory bean.
   *
   * @return the local validator factory bean
   */
  @Bean
  public LocalValidatorFactoryBean validator() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(getMessageSource());
    return bean;
  }

  @Override
  public Validator getValidator() {
    return validator();
  }

  @Bean(name = "ModelMapper")
  public ModelMapper getModelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
        .setMatchingStrategy(MatchingStrategies.STRICT);
    return modelMapper;
  }

  @Bean(name = "ObjectMapper")
  public ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Bean(name = "RegexHelper")
  public RegexHelper getRegexHelper() {
    return new RegexHelper();
  }

  /**
   * Bean multithreading
   */
  @Bean(name = "threadPoolExecutor")
  public ThreadPoolTaskExecutor executorTask() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(5);
    executor.setThreadNamePrefix("executor-");
    executor.initialize();
    return executor;
  }
}

