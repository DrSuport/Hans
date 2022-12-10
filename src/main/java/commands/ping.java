package commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ping extends ACommand {
    private final static String name = "ping";
    private final static String description = "Calculate ping of the bot";
    private final static String access = null;
    private final static OptionData option = null;

    public ping(){
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event){
        super.Execute(event);
        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                ).queue(); // Queue both reply and edit
    }
}
