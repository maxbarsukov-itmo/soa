package ru.ifmo.soa.uddi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for UDDI v3.0.2-compliant registry.
 * Exposes SOAP endpoints for:
 * - Inquiry API (find_business, get_businessDetail, etc.)
 * - Publish API (save_business, delete_business, etc.)
 * - Security (get_authToken)
 */
@SpringBootApplication
public class UddiRegistryApplication {

  public static void main(String[] args) {
    SpringApplication.run(UddiRegistryApplication.class, args);
  }
}
