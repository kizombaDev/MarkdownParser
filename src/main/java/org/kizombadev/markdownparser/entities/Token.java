package org.kizombadev.markdownparser.entities;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Token {
    public static final Token Star = create(Category.STAR);
    public static final Token DoubleStar = create(Category.DOUBLE_STAR);
    public static final Token NumberSign = create(Category.NUMBER_SIGN);
    public static final Token DoubleNumberSign = create(Category.DOUBLE_NUMBER_SIGN);
    public static final Token NewLine = create(Category.NEW_LINE);
    public static final Token GreaterThanSign = create(Category.GREATER_THAN_SIGN);
    private String value;
    private Category category;
    private Token() {
    }

    private static Token create(Category type) {
        checkArgument(type != Category.TEXT, "Please use the method createTextToken if you want create a text token");

        return create(type, null);
    }

    public static Token createTextToken(String value) {
        checkNotNull(value);
        return create(Category.TEXT, value);
    }

    private static Token create(Category type, String value) {
        Token result = new Token();
        result.value = value;
        result.category = type;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Token token = (Token) o;

        return Objects.equal(value, token.value) && Objects.equal(category, token.category);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value, category);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .add("category", category)
                .toString();
    }

    public String getValue() {
        return value;
    }

    public boolean isTextToken() {
        return category == Category.TEXT;
    }

    private Category getCategory() {
        return category;
    }

    private enum Category {
        STAR,
        TEXT,
        DOUBLE_NUMBER_SIGN,
        DOUBLE_STAR,
        NUMBER_SIGN,
        NEW_LINE,
        GREATER_THAN_SIGN
    }
}
