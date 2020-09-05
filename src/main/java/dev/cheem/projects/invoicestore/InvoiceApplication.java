package dev.cheem.projects.invoicestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:beans.xml")
public class InvoiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(InvoiceApplication.class, args);
  }


}

