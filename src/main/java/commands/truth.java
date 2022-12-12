package commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class truth extends ACommand {
    private final static String name = "truth";
    private final static String description = "Makes bot say an ultimate truth";
    private final static String access = null;
    private final static OptionData option = null;

    public truth(){
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event){
        super.Execute(event);
        event.reply("2 + 2 = 4").setEphemeral(true).queue();

    }
}