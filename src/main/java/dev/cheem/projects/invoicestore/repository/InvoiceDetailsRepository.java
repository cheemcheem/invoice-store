package dev.cheem.projects.invoicestore.repository;

import dev.cheem.projects.invoicestore.model.InvoiceDetails;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetails, Long>{

}
