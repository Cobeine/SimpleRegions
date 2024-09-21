package me.cobeine.regions.api.commands;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;

public interface SubCommand<T extends CommandSender> {
    void execute(T t, String[] args);


   default HashMap<Integer, List<String>> complete(Invocation<T> invocation, HashMap<Integer, List<String>> map){
       return map;
   }



}
