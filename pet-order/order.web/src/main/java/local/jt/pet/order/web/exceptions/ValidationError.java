package local.jt.pet.order.web.exceptions;

public record ValidationError(
        String field,
        String message
) { }
