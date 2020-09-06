package me.fixeddev.commandflow.part;

/**
 * A part that has the option to use all the available primitive arguments
 * like {@linkplain Integer}, {@linkplain Double}, {@linkplain Float}, {@linkplain Boolean}.
 */
public abstract class PrimitivePart implements ArgumentPart {

    private String name;
    protected boolean consumeAll;

    /**
     * Creates a PrimitivePart instance with the given name and boolean that represents if
     * the arguments will be consumed.
     *
     * @param name       The name for this part.
     * @param consumeAll If this part should consume all the available arguments.
     */
    public PrimitivePart(String name, boolean consumeAll) {
        this.name = name;
        this.consumeAll = consumeAll;
    }

    /**
     * Creates a PrimitivePart instance with the given name and consumeAll parameter as disabled.
     *
     * @param name The name for this part.
     */
    public PrimitivePart(String name) {
        this(name, false);
    }

    @Override
    public String getName() {
        return name;
    }

}