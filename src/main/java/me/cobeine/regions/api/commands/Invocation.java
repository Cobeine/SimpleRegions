package me.cobeine.regions.api.commands;

import lombok.NonNull;

public class Invocation<T> {
    private final T commandSender;
    private final String subcommand;
    private final String[] arguments;
    private final String[] allArgs;

    public Invocation(T t, String a1, String[] a2, String[] a3) {
        this.commandSender = t;
        this.allArgs = a3;
        this.arguments = a2;
        this.subcommand = a1;
    }

    public String getSubcommand() {
        return subcommand;
    }

    public String[] getAllArgs() {
        return allArgs;
    }

    public String[] getArguments() {
        return arguments;
    }

    public T getCommandSender() {
        return commandSender;
    }

    public @NonNull String getSafeArgument(int i) {
        var value = getArguments()[i];
        return value == null ? "" : value;
    }
}
