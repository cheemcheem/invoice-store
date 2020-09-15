package dev.cheem.projects.invoicestore.repository;

import dev.cheem.projects.invoicestore.model.InvoiceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetails, Long> {

}
