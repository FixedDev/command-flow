package me.fixeddev.commandflow.examples.basic;

import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.examples.user.User;

public class NamespaceUsage {

    public static void main(String[] args) {
        // The namespace is a mapping/set of the arguments that are injected into the execution.
        // For example, we can inject the user executor of a command and use it later.

        // Create a namespace, obviously
        Namespace namespace = Namespace.create();

        // Ok, here we actually inject the user instance, there are 3 arguments
        // The first one is the type of the instance.
        // The second one the name to identify from other injected instances with the same type.
        // The actual user instance
        namespace.setObject(User.class, "USER", new User("Fixed", 16));

        // To retrieve an object from the namespace is done in the next form
        // The arguments are the same as the setObject, the type of the instance and the differentiator name
        User user = namespace.getObject(User.class, "USER");

        System.out.println(user.getName()); // Fixed
    }

}
