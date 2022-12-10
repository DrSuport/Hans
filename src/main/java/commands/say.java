package commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class say extends ACommand {
    private final static String name = "say";
    private final static String description = "Makes bot says things";
    private final static String access = null;
    private final static OptionData option = new OptionData(OptionType.STRING, "message", "What is bot suposed to say", true);

    public say(){
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event){
        super.Execute(event);
        OptionMapping messageOption = event.getOption("message");
        String message;
        if(messageOption != null){
            message = messageOption.getAsString();
        }else{
            message = "ERR";
        }

        event.getChannel().sendMessage(message).queue();
        event.reply("message send").setEphemeral(true).queue();

    }
}
