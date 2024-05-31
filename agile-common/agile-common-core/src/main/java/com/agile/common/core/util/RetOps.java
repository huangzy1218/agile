package com.agile.common.core.util;

import cn.hutool.core.util.ObjectUtil;
import com.agile.common.core.constant.CommonConstants;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Simplify R<T> access operations, example:
 * <pre>
 * // 1. Chain operation: Assert and then consume
 *  RetOps.of(result)
 *   		.assertCode(-1,r -> new RuntimeException("error "+r.getCode()))
 *   		.assertDataNotEmpty(r -> new IllegalStateException("oops!"))
 *   		.useData(System.out::println);
 *
 * // 2. Read raw value.
 * 	 R  s = RetOps.of(result)
 *          .assertDataNotNull(r -> new IllegalStateException("oops!"))
 *          .map(i -> Integer.toHexString(i))
 *          .peek();
 * </pre>
 *
 * @author Huang Z.Y.
 */
public class RetOps<T> {

    /**
     * Status code is success.
     */
    public static final Predicate<R<?>> CODE_SUCCESS = r -> CommonConstants.SUCCESS == r.getCode();

    /**
     * Data has value.
     */
    public static final Predicate<R<?>> HAS_DATA = r -> ObjectUtil.isNotEmpty(r.getData());

    /**
     * Data has value and contains element(s).
     */
    public static final Predicate<R<?>> HAS_ELEMENT = r -> ObjectUtil.isNotEmpty(r.getData());

    /**
     * Status is success and has value.
     */
    public static final Predicate<R<?>> DATA_AVAILABLE = CODE_SUCCESS.and(HAS_DATA);

    private final R<T> original;

    // ~ Initialize.
    // ===================================================================================================

    RetOps(R<T> original) {
        this.original = original;
    }

    public static <T> RetOps<T> of(R<T> original) {
        return new RetOps<>(Objects.requireNonNull(original));
    }

    // ~ Miscellaneous method.
    // ===================================================================================================

    /**
     * Get original value.
     *
     * @return R
     */
    public R<T> peek() {
        return original;
    }

    /**
     * Get {@code code}.
     *
     * @return {@code code}
     */
    public int getCode() {
        return original.getCode();
    }

    /**
     * Get {@code data}.
     *
     * @return {@code data}
     */
    public Optional<T> getData() {
        return Optional.ofNullable(original.getData());
    }

    /**
     * Read the value of {@code data} conditionally.
     *
     * @param predicate Assertion function
     * @return Returns Optional wrapped data or empty if the assertion fails
     */
    public Optional<T> getDataIf(Predicate<? super R<?>> predicate) {
        return predicate.test(original) ? getData() : Optional.empty();
    }

    /**
     * Get {@code msg}.
     *
     * @return {@code msg}
     */
    public Optional<String> getMsg() {
        return Optional.of(original.getMsg());
    }

    /**
     * Test the value of {@code value} for equality.
     *
     * @param value Reference value
     * @return Return {@code ture} for equality
     */
    public boolean codeEquals(int value) {
        return original.getCode() == value;
    }

    /**
     * Test the value of {@code value} for equality.
     *
     * @param value Reference value
     * @return Return {@code ture} for not equality
     */
    public boolean codeNotEquals(int value) {
        return !codeEquals(value);
    }

    /**
     * Is success.
     *
     * @return {@code true} for success
     * @see CommonConstants#SUCCESS
     */
    public boolean isSuccess() {
        return codeEquals(CommonConstants.SUCCESS);
    }

    /**
     * Is failed.
     *
     * @return {@code true} for not success
     */
    public boolean notSuccess() {
        return !isSuccess();
    }

    // ~ Chain operation.
    // ===================================================================================================

    /**
     * Asserts the value of {@code code}.
     *
     * @param expect Expect value
     * @param func   A user function that creates exception objects
     * @param <Ex>   Exception type
     * @return Returns the instance so that the chain operation can continue
     * @throws Ex Thrown when the assertion fails
     */
    public <Ex extends Exception> RetOps<T> assertCode(int expect, Function<? super R<T>, ? extends Ex> func)
            throws Ex {
        if (codeNotEquals(expect)) {
            throw func.apply(original);
        }
        return this;
    }

    /**
     * Asserts success.
     *
     * @param func A user function that creates exception objects
     * @param <Ex> Exception type
     * @return Returns the instance so that the chain operation can continue
     * @throws Ex Thrown when the assertion fails
     */
    public <Ex extends Exception> RetOps<T> assertSuccess(Function<? super R<T>, ? extends Ex> func) throws Ex {
        return assertCode(CommonConstants.SUCCESS, func);
    }

    /**
     * Asserts that business data has a value.
     *
     * @param func A user function that creates exception objects
     * @param <Ex> Exception type
     * @return Returns the instance so that the chain operation can continue
     * @throws Ex Thrown when the assertion fails
     */
    public <Ex extends Exception> RetOps<T> assertDataNotNull(Function<? super R<T>, ? extends Ex> func) throws Ex {
        if (Objects.isNull(original.getData())) {
            throw func.apply(original);
        }
        return this;
    }

    /**
     * Asserts that business data has values and contains elements.
     *
     * @param func A user function that creates exception objects
     * @param <Ex> Exception type
     * @return Returns the instance so that the chain operation can continue
     * @throws Ex Thrown when the assertion fails
     */
    public <Ex extends Exception> RetOps<T> assertDataNotEmpty(Function<? super R<T>, ? extends Ex> func) throws Ex {
        if (ObjectUtil.isNotEmpty(original.getData())) {
            throw func.apply(original);
        }
        return this;
    }

    /**
     * Transform business data.
     *
     * @param mapper Business data conversion function
     * @param <U>    Data type
     * @return Returns the instance so that the chain operation can continue
     */
    public <U> RetOps<U> map(Function<? super T, ? extends U> mapper) {
        R<U> result = R.restResult(mapper.apply(original.getData()), original.getCode(), original.getMsg());
        return of(result);
    }

    /**
     * Transform business data with checking whether it is empty.
     *
     * @param predicate Assertion function
     * @param mapper    Business data conversion function
     * @param <U>       Data type
     * @return Returns the instance so that the chain operation can continue
     * @see RetOps#CODE_SUCCESS
     * @see RetOps#HAS_DATA
     * @see RetOps#HAS_ELEMENT
     * @see RetOps#DATA_AVAILABLE
     */
    public <U> RetOps<U> mapIf(Predicate<? super R<T>> predicate, Function<? super T, ? extends U> mapper) {
        R<U> result = R.restResult(mapper.apply(original.getData()), original.getCode(), original.getMsg());
        return of(result);
    }

    // ~ Data consumer.
    // ===================================================================================================

    /**
     * Consume data if data is accessible.
     *
     * @param consumer Consumer function
     */
    public void useData(Consumer<? super T> consumer) {
        consumer.accept(original.getData());
    }

    /**
     * Conditional consume data.
     *
     * @param consumer Consumer function
     * @param codes    A collection of error codes that match any one and call the consumption function
     */
    public void useDataOnCode(Consumer<? super T> consumer, int... codes) {
        useDataIf(o -> Arrays.stream(codes).filter(c -> original.getCode() == c).findFirst().isPresent(), consumer);
    }

    /**
     * Conditional consumption (success code indicates success).
     *
     * @param consumer Consumer function
     */
    public void useDataIfSuccess(Consumer<? super T> consumer) {
        useDataIf(CODE_SUCCESS, consumer);
    }

    /**
     * Conditional consumption.
     *
     * @param predicate Assertion function
     * @param consumer  Consumer function, which is called when assertion is {@code true}
     * @see RetOps#CODE_SUCCESS
     * @see RetOps#HAS_DATA
     * @see RetOps#HAS_ELEMENT
     * @see RetOps#DATA_AVAILABLE
     */
    public void useDataIf(Predicate<? super R<T>> predicate, Consumer<? super T> consumer) {
        if (predicate.test(original)) {
            consumer.accept(original.getData());
        }
    }

}
    