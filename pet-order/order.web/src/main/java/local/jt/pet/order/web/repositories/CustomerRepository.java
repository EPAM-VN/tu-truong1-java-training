package local.jt.pet.order.web.repositories;

import local.jt.pet.order.web.enums.Membership;
import local.jt.pet.order.web.models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByEmail(String email);

    @Query(
            value = """
                SELECT c
                FROM Customer AS c
                WHERE c.membership != 'GUEST'
                    AND c.email ilike :email
            """
    )
    List<Customer> getMemberByEmail(@Param("email") String email);

    @Query(
            value = """
                SELECT DISTINCT c.*
                FROM customers AS c
                JOIN addresses AS a
                    ON c.id = a.customer_id
                WHERE c.membership = :membership
                    AND c.email ILIKE CONCAT('%', :email, '%')
            """,
            countQuery = """
                SELECT COUNT(DISTINCT *)
                FROM customers AS c
                JOIN addresses AS a
                    ON c.id = a.customer_id
                WHERE c.membership = :membership
                    AND c.email ILIKE '%' || :email || '%'
            """,
            nativeQuery = true
    )
    Page<Customer> search(@Param("membership") String pMembership, @Param("email") String pEmail, Pageable pPageable);
}
