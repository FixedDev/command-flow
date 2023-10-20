## Namespaces

*(If you use an already implemented platform and annotated commands, you don't need
to use Namespaces, yay!)*

A namespace is a mapping/set of the arguments that are injected into the execution.
For example, we can inject the user executor of a command and use it later.

Creating a namespace:
<!--@formatter:off-->
```java
// Create a namespace
Namespace namespace = Namespace.create();
```
<!--@formatter:on-->

Now we can set any object in the namespace, by type and name, for example, suppose we
have a `User` class:
<!--@formatter:off-->
```java
namespace.setObject(User.class, "USER", new User("Fixed", 16));
```
<!--@formatter:on-->

And now, we can retrieve the object from the namespace, using the exact same
type and name, for example:
<!--@formatter:off-->
```java
User user = namespace.getObject(User.class, "USER");

System.out.println(user.getName()); // Fixed
```
<!--@formatter:on-->

