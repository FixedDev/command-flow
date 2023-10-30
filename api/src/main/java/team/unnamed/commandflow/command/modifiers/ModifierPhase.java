package team.unnamed.commandflow.command.modifiers;

/**
 * This enum represents the different phases in which a modifier may be applied.
 */
public enum ModifierPhase {
    /**
     * The modifier is applied before the command is parsed, just after the context is created.
     * <p>
     * Only in this phase the stack is available, in the next phases the stack doesn't exists so it's set to null.
     */
    PRE_PARSE,
    /**
     * The modifier is applied after the command is parsed, just before the command is executed.
     * <p>
     * Another name for this could be POST_PARSE.
     */
    PRE_EXECUTE,
    /**
     * The modifier is applied after the command is executed.
     * <p>
     * This phase can't cancel anything, as it is the last phase, and the command is already executed, trying to cancel
     * something is useless, and only will prevent the next modifier from being called.
     */
    POST_EXECUTE
}
