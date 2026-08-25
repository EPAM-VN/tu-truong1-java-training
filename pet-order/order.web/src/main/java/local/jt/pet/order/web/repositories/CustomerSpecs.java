package local.jt.pet.order.web.repositories;

import local.jt.pet.order.web.enums.Membership;
import local.jt.pet.order.web.models.Customer;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecs {
    public static Specification<Customer> hasIdentifier(String identifier) {
        return (identifier == null || identifier.isBlank())
            ?   Specification.unrestricted()
            :   (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("identifier"), identifier);
    }

    public static Specification<Customer> isMember(Membership membership) {
        return (membership == null)
        ?   (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("membership"), Membership.GUEST)
        :   (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.notEqual(root.get("membership"), Membership.GUEST),
                        criteriaBuilder.equal(root.get("membership"), membership)
                );
    }
}
