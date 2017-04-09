package org.kizombadev.markdownparser;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Token {
    private String value;
    private Category category;

    private Token() {
    }

    public static Token create(Category type) {
        checkArgument(type != Category.TEXT, "Please use the method createText if you want create a text token");

        return create(type, null);
    }

    public static Token createText(String value) {
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

    public Category getCategory() {
        return category;
    }

    public enum Category {
        STAR,
        TEXT,
        DOUBLE_NUMBER_SIGN,
        DOUBLE_STAR,
        NUMBER_SIGN,
        NEW_LINE,
        GREATER_THAN_SIGN
    }
}
