package org.kizombadev.markdownparser;


public class Token {
    private String value;
    private Category category;

    private Token() {
    }

    public static Token create(Category type) {

        if (type == Category.TEXT) {
            //todo better exception
            throw new IllegalStateException();
        }

        return create(type, null);
    }

    public static Token createText(String value) {
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

        if (value != null ? !value.equals(token.value) : token.value != null) {
            return false;
        }
        return category == token.category;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", category=" + category +
                '}';
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
