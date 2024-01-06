package team.unnamed.commandflow.annotated.part;

/**
 * An aggregation of multiple bindings of {@linkplain PartModifier} or {@linkplain PartFactory}, allowing to automatically register
 * a set of modifiers or factories.
 */
@FunctionalInterface
public interface Module {

    /**
     * The configure method, where you add the bindings for {@linkplain PartModifier}s or a set of {@linkplain PartFactory}.
     */
    void configure(PartInjector injector);

}
