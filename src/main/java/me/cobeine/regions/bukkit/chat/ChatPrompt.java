package me.cobeine.regions.bukkit.chat;

import me.cobeine.regions.bukkit.SimpleRegions;
import me.cobeine.regions.bukkit.region.cuboid.BukkitRegion;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="https://github.com/Cobeine">Cobeine</a>
 */

public class ChatPrompt extends StringPrompt {
    private final TextCallback callback;
    private final Player player;
    private final BukkitRegion region;
    public ChatPrompt(Player player, BukkitRegion region, TextCallback callback) {
        this.callback = callback;
        this.player = player;
        this.region = region;
        ConversationFactory factory = new ConversationFactory(SimpleRegions.getInstance())
                .withFirstPrompt(this)
                .withLocalEcho(false)
                .withEscapeSequence("cancel");
        factory.buildConversation(player).begin();
    }
    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
        return "";
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String message) {
        ChatPrompts.ONGOING_PROMPTS.remove(player.getUniqueId());
        if(message == null){
            conversationContext.getForWhom().sendRawMessage("Invalid prompt!");
            return Prompt.END_OF_CONVERSATION;
        }
        if (message.equalsIgnoreCase("cancel")) {
            conversationContext.getForWhom().sendRawMessage("Region modification Cancelled");
            return Prompt.END_OF_CONVERSATION;
        }
        Bukkit.getScheduler().runTaskLater(SimpleRegions.getInstance(), () -> {
            callback.call(player,region,message);
        },2);
        return Prompt.END_OF_CONVERSATION;
    }
}
