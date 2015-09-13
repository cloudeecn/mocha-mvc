package works.cirno.mocha.mvc.factory;

/**
 *
 */
class BasicName {
    private String name;
    private Class<?> type;

    public BasicName(Class<?> type) {
        this(null, type);
    }

    public BasicName(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicName basicName = (BasicName) o;

        if (!type.equals(basicName.type)) return false;
        if (name != null ? !name.equals(basicName.name) : basicName.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + type.hashCode();
        return result;
    }
}
