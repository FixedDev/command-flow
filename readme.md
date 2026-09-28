# command-flow

[![Build Status](https://img.shields.io/github/actions/workflow/status/unnamed/command-flow/build.yml?branch=master)](https://github.com/unnamed/command-flow/actions/workflows/build.yml)
[![MIT License](https://img.shields.io/badge/license-MIT-blue)](license.txt)

A Java 8+ command framework that reduces repetitive code when creating commands for multiple platforms. The project separates its shared API from platform integrations while keeping a consistent development model across Bukkit, BungeeCord, Velocity, Discord, and Brigadier.

## What it demonstrates

- Design of a modular, reusable Java library.
- Cross-platform integrations through independent modules.
- Imperative and declarative APIs for building commands.
- Automated tests and concept-based technical documentation.
- Handling of arguments, permissions, subcommands, and execution context.

## Project structure

| Module | Purpose |
| --- | --- |
| `api` | Shared API and tests. |
| `bukkit`, `bungee`, `velocity` | Server-platform integrations. |
| `discord` | Discord JDA integration. |
| `brigadier` | Brigadier adapter. |

## Local development

Requirements: Java 8 and Maven.

```bash
mvn clean verify
```

Extended documentation is available in [`docs/`](docs), and the license is in [`license.txt`](license.txt).
