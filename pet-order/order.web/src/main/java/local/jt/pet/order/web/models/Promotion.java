package local.jt.pet.order.web.models;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import local.jt.pet.order.web.enums.PromotionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(
        name = "promotions",
        indexes = {
                @Index(name = "idx_promotions_code", columnList = "code", unique = true),
        })
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Promotion extends BaseEntity {
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",  nullable = false)
    private PromotionStatus status = PromotionStatus.INACTIVE;

    @Column(name = "effective_from",  nullable = false, columnDefinition = "BIGINT DEFAULT (EXTRACT(EPOCH FROM CURRENT_TIMESTAMP) * 1000)")
    private Long effectiveFromDate;

    @Column(name = "effective_to")
    private Long effectiveToDate;

    @ManyToMany(mappedBy = "promotions", cascade = CascadeType.ALL)
    private Set<Order> orders;

    @PrePersist
    public void prePersist() {
        if (effectiveFromDate == null) {
            effectiveFromDate = Instant.now().toEpochMilli(); // UTC epoch millis
        }
    }
}
