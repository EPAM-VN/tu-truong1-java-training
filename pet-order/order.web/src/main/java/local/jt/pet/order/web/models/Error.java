package local.jt.pet.order.web.models;

import java.util.Objects;

public record Error(String Code, String Name) {
    public final static Error NONE = new Error("", "");
    public final static Error NULL_VALUE = new Error("Error.NullValue", "Null value was provided");
    public final static Error VALIDATION = new Error("Error.Validation", "One or more validation errors occurred");

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Error error = (Error) o;
        return Objects.equals(Code, error.Code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Code);
    }
}
