package ru.ifmo.soa.uddi.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import ru.ifmo.soa.uddi.exception.UddiFaultResolver;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(context);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, "/uddi/*");
  }

  @Bean(name = "uddiapi")
  public DefaultWsdl11Definition uddiApiWsdl(XsdSchema uddiSchema) {
    DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();
    wsdl.setPortTypeName("UDDI_APIService");
    wsdl.setLocationUri("/uddi");
    wsdl.setTargetNamespace("urn:uddi-org:api_v3");
    wsdl.setSchema(uddiSchema);
    return wsdl;
  }

  @Bean
  public XsdSchema uddiSchema() {
    return new SimpleXsdSchema(new ClassPathResource("schema/uddi_v3.xsd"));
  }

  @Bean
  public UddiFaultResolver uddiFaultResolver() {
    return new UddiFaultResolver();
  }
}
