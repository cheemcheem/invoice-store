package dev.cheem.projects.invoicestore.repository;

import dev.cheem.projects.invoicestore.model.InvoiceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceFileRepository extends JpaRepository<InvoiceFile, Long> {

}
