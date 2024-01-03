package team.unnamed.commandflow;

public interface Namespace {

    String DEFAULT_NAMESPACE_NAME = "__";

    /**
     * Gets an injected object from the backing Map
     *
     * @param clazz The class type of the Object to get
     * @param name  The name of the Object to get
     * @param <T>   The type of the Object to get
     * @return A nullable instance of T contained in the backing map with the specified name
     */
    <T> T getObject(Class<T> clazz, String name);

    /**
     * Gets an injected object from the backing Map, with the default namespace name.
     *
     * @param clazz The class type of the Object to get
     * @param <T>   The type of the Object to get
     * @return A nullable instance of T contained in the backing map with the default namespace name
     */
    default <T> T getObject(Class<T> clazz) {
        return getObject(clazz, DEFAULT_NAMESPACE_NAME);
    }

    /**
     * Sets an Object of Type T with a specified name into the backing Map
     * If an object with the same name and type is already on the map, it will be overridden
     *
     * @param clazz  The class type of the Object to set
     * @param name   The name of the object to set
     * @param object The Object to set into the backing map
     * @param <T>    The Type of the object to set
     */
    <T> void setObject(Class<T> clazz, String name, T object);

    /**
     * Sets an Object of Type T with the default name into the backing Map
     * If an object with the same type and without a specified name is already on the map, it will be overridden
     *
     * @param clazz  The class type of the Object to set
     * @param object The Object to set into the backing map
     * @param <T>    The Type of the object to set
     */
    default <T> void setObject(Class<T> clazz, T object) {
        setObject(clazz, DEFAULT_NAMESPACE_NAME, object);
    }

    static Namespace create() {
        return new NamespaceImpl();
    }

}
