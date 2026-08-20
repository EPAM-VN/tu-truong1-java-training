package local.jt.pet.order.domain.abstractions;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class Entity
{
    private UUID id;
    private final List<DomainEvent> domainEvents = List.of();

    protected Entity(UUID id)
    {
        this.id = id;
    }

    protected Entity()
    {
    }

    public UUID getId() { return this.id; }

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
