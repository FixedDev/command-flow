## Command Part

This is the second most fundamental component, it can be understood as every argument
of a [Command](./command.md), including things like subcommands, flags, non positional
arguments, etc.

It can use arguments from the argument list, or provide them using any other means. They
can also forward the parsing responsibility to another part and just act as a modifier.

Most of the default parts can be found at the `Parts` class.