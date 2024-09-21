package me.cobeine.regions.api.commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public abstract class CommandModule<T extends CommandSender> {
    ConcurrentHashMap<String, SubCommand<T>> subCommands;
    ExecutorService pool = Executors.newFixedThreadPool(10);
    private final String name;
    private String permission = null;

    public CommandModule(String name, JavaPlugin provider) {
        this(name, provider, false);
    }
    public CommandModule(String name,String permission, JavaPlugin provider) {
        this(name, null, provider, false);
    }
    public CommandModule(String name, JavaPlugin provider, boolean async) {
        this(name, null, provider, async);
    }
    public CommandModule(String name, @Nullable String permission, JavaPlugin provider, boolean async) {
        this.permission = permission;
        subCommands = new ConcurrentHashMap<>();
        PluginCommand cmd = provider.getCommand(name);
        this.name = name;
        if (cmd == null) {
            Logger.getLogger("commands").severe("Failed to register command '" + name + "' please check your plugin.yml file");
            return;
        }
        cmd.setExecutor((sender, command, label, args) -> {
            if (async) {
                pool.submit(() -> {
                    execution(sender,args);
                });
            }else {
                execution(sender,args);
            }
            return true;
        });

        cmd.setTabCompleter(new TabCompleter() {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
                Invocation<T> invocation = new Invocation<>((T) sender, args[0], Arrays.copyOfRange(args, 1, args.length), args);
                CompletableFuture<List<String>> suggest = suggestAsync(invocation);
                return suggest.getNow(new ArrayList<>());
            }
        });

        registerSubCommand(new DefaultHelpSubCommand<T>());
    }

    private void execution(CommandSender sender, String[] args) {
        try {
            T t = (T) sender;
            Invocation<T> invocation;
            switch (args.length) {
                case 0:
                    invocation = new Invocation<>(t, null, new String[]{}, args);
                    break;
                case 1:
                    invocation = new Invocation<>(t, args[0], new String[]{}, args);
                    break;
                default:
                    invocation = new Invocation<>(t, args[0], Arrays.copyOfRange(args, 1, args.length), args);
                    break;
            }
            if (permission != null && !permission.equalsIgnoreCase("") && !permission.isEmpty() ) {
                if (!invocation.getCommandSender().hasPermission(permission)) {
                    invocation.getCommandSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou're not allowed to execute this command!"));
                    return;
                }
            }
            execute(invocation);
        } catch (Exception e) {
            Logger.getLogger("commands").severe(String.format("Failed to execute %s for %s: %s",
                    name, sender.getName(), e));
            e.printStackTrace();
        }
    }

    public static List<String> getOnlinePlayers() {
        List<String> additions = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            additions.add(p.getName());
        }
        return additions;
    }

    public void execute( Invocation<T> invocation){
        defaultExecution(invocation);
    }

    public CompletableFuture<List<String>> suggestAsync(Invocation<T> invocation){
        return getDefaultComplete(invocation);
    }

    public void registerSubCommand(SubCommand<T> sub) {
        if (!sub.getClass().isAnnotationPresent(SubCommandInfo.class))
            return;
        SubCommandInfo info = sub.getClass().getAnnotation(SubCommandInfo.class);
        subCommands.put(info.name(), sub);
    }
    public void executeSubCommand( SubCommand<T> sub, Invocation<T> invocation){
        if (!sub.getClass().isAnnotationPresent(SubCommandInfo.class))
            return;
        SubCommandInfo info = sub.getClass().getAnnotation(SubCommandInfo.class);
        int enteredLength = invocation.getArguments().length;
        int requiredLength = info.requiredLength();
        LengthCheckType type = info.length();
        if (LengthChecker.check(enteredLength, type, requiredLength)) {
            sub.execute(invocation.getCommandSender(),invocation.getArguments());
            return;
        }
        ((CommandSender) invocation.getCommandSender())
                .sendMessage(ChatColor.translateAlternateColorCodes('&',
                        ("&b/" + name+ " &f" + info.name() + " " + info.syntax() + " &7" + info.description())));
    }

    public void defaultExecution(Invocation<T> invocation) {
        CommandSender player =(CommandSender) invocation.getCommandSender();
        if (invocation.getSubcommand() == null) {
            return;
        }
        SubCommand<T> subCommand = getSubCommand(invocation.getSubcommand());
        if (subCommand == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ("&cInvalid subcommand, please type /<> help for list of commands!".replace("<>",this.name))));
            return;
        }
        if (!subCommand.getClass().isAnnotationPresent(SubCommandInfo.class)) return;
        SubCommandInfo info = subCommand.getClass().getAnnotation(SubCommandInfo.class);
        if (info.permission() != null && !info.permission().equals("") && !info.permission().isEmpty())
        if (!invocation.getCommandSender().hasPermission(info.permission())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou're not allowed to execute this command!"));
            return;
        }
        executeSubCommand(subCommand,invocation);
    }
    protected CompletableFuture<List<String>> getDefaultComplete(Invocation<T> invocation,List<String> additions) {
        if (invocation.getSubcommand() == null) {
            return CompletableFuture.completedFuture(getSubNames());
        }
        if (invocation.getSubcommand() != null && invocation.getArguments().length == 0) {
            List<String> a = new ArrayList<>(getSubNames());
            a.addAll(additions);
            return CompletableFuture.completedFuture(
                    a.stream().filter(e -> e.startsWith(invocation.getSubcommand())).collect(Collectors.toList()));
        }
        if (invocation.getSubcommand() != null) {
            SubCommand<T> subCommand = getSubCommand(invocation.getSubcommand());
            if (subCommand != null) {
                if (!subCommand.getClass().isAnnotationPresent(SubCommandInfo.class)) return CompletableFuture.completedFuture(new ArrayList<>());
                SubCommandInfo info = subCommand.getClass().getAnnotation(SubCommandInfo.class);
                if (info.permission() != null)
                if (!invocation.getCommandSender().hasPermission(info.permission())) {
                    return CompletableFuture.completedFuture(new ArrayList<>());
                }
                return CompletableFuture.completedFuture(subCommand.complete(invocation, new HashMap<>())
                        .getOrDefault(invocation.getArguments().length,new ArrayList<>())
                        .stream()
                        .filter(e -> e.startsWith(invocation.getArguments()[invocation.getArguments().length - 1]))
                        .collect(Collectors.toList()));
            }
        }
        return CompletableFuture.completedFuture(new ArrayList<>());
    }
    protected CompletableFuture<List<String>> getDefaultComplete(Invocation<T> invocation) {
        return getDefaultComplete(invocation, new ArrayList<>());
    }
    public List<String> getSubNames() {
        return new ArrayList<>(subCommands.keySet());
    }

    public ConcurrentHashMap<String, SubCommand<T>> getSubCommands() {
        return subCommands;
    }

    public SubCommand<T> getSubCommand(String sub) {
        return subCommands.get(sub);
    }

    public ExecutorService getPool() {
        return pool;
    }

    public static class LengthChecker{
        public static boolean check(int current, LengthCheckType type, int required){
            switch (type){
                case LESS:
                    return current < required;
                case EQUAL:
                    return current == required;
                case GREATER:
                    return current > required;
                case NO_CHECK:
                    return true;
            }
            return false;
        }
    }


    @SubCommandInfo(name = "help",description = "displays all available commands")
    private class DefaultHelpSubCommand<T extends CommandSender> implements SubCommand<T> {


        @Override
        public void execute(T t, String[] args) {
            StringBuilder builder = new StringBuilder();
            builder.append("\n \n");
            for (SubCommand<?> subCommand : getSubCommands().values()) {
                if (!subCommand.getClass().isAnnotationPresent(SubCommandInfo.class)) continue;
                SubCommandInfo info = subCommand.getClass().getAnnotation(SubCommandInfo.class);
                builder.append("&b/").append(name).append(" ").append(info.name()).append(" ").append(info.syntax()).append(" &f")
                        .append(info.description()).append("\n");
            }
            builder.append("\n \n");
            t.sendMessage(ChatColor.translateAlternateColorCodes('&', builder.toString()));
        }

        @Override
        public HashMap<Integer, List<String>> complete(Invocation<T> invocation, HashMap<Integer, List<String>> map) {
            return map;
        }
    }

}
