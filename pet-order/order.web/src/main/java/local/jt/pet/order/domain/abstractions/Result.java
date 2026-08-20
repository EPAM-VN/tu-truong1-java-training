package local.jt.pet.order.domain.abstractions;

import java.util.Objects;

public class Result {
    private final boolean isSuccess;
    private final Error error;

    public Result(boolean isSuccess, Error error) {
        if (isSuccess && !Objects.equals(error, Error.NONE)) {
            throw new IllegalStateException();
        }

        if (!isSuccess && Objects.equals(error, Error.NONE)) {
            throw new IllegalStateException();
        }

        this.isSuccess = isSuccess;
        this.error = error;
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

    public static <T> Result.Generic<T> failure(Error error, T value) {
        return new Result.Generic<>(value, false, error);
    }

    public static <T> Result.Generic<T> create(T value) {
        return value != null ? success(value) : failure(Error.NULL_VALUE, value);
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