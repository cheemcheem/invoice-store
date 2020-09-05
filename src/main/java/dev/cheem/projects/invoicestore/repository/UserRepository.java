package dev.cheem.projects.invoicestore.repository;

import dev.cheem.projects.invoicestore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
