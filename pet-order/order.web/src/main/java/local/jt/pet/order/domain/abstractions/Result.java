package local.jt.pet.order.domain.abstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Result {
    private final boolean isSuccess;
    private final Error error;
    private List<Error> problemDetails;

    public Result(boolean isSuccess, Error error) {
        if (isSuccess && !Objects.equals(error, Error.NONE)) {
            throw new IllegalArgumentException();
        }

        if (!isSuccess && Objects.equals(error, Error.NONE)) {
            throw new IllegalArgumentException();
        }

        this.isSuccess = isSuccess;
        this.error = error;
        this.problemDetails = new ArrayList<>();
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return !isSuccess;
    }

    public Error getError() {
        return error;
    }

    public static Result success() {
        return new Result(true, Error.NONE);
    }

    public static Result failure(Error error) {
        return new Result(false, error);
    }

    public static <T> Result.Generic<T> success(T value) {
        return new Result.Generic<>(value, true, Error.NONE);
    }

    public static <T> Result.Generic<T> failureOf(Error error) {
        return new Result.Generic<>(null, false, error);
    }

    public static <T> Result.Generic<T> create(T value) {
        return value != null ? success(value) : failureOf(Error.NULL_VALUE);
    }

    public static final class Generic<T> extends Result {
        private final T value;

        public Generic(T value, boolean isSuccess, Error error) {
            super(isSuccess, error);
            this.value = value;
        }

        public T getValue() {
            if (isSuccess()) {
                return value;
            }
            throw new IllegalStateException("The value of a failure result can not be accessed.");
        }

        public static <T> Generic<T> from(T value) {
            return create(value);
        }
    }
}