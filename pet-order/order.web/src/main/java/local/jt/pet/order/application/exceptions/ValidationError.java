package local.jt.pet.order.application.exceptions;

public record ValidationError(
        String field,
        String message
) { }
