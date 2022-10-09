package commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ping extends ACommand {
    public static String name = "ping";
    public static String description = "Calculate ping of the bot";
    public static String access = "none";
    public static OptionData option = null;

    public ping(){
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event){
        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                ).queue(); // Queue both reply and edit
    }
}
