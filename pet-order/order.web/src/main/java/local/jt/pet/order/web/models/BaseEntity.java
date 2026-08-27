package local.jt.pet.order.web.models;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEntity
{
    @Setter
    @Getter
    @Id
    @Column(name = "id")
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @EqualsAndHashCode.Include
    private UUID id;

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public List<DomainEvent> GetDomainEvents()
    {
        return Collections.unmodifiableList(domainEvents);
    }

    public void ClearDomainEvents()
    {
        domainEvents.clear();
    }

    protected void RaiseDomainEvent(DomainEvent domainEvent)
    {
        domainEvents.add(domainEvent);
    }
}
