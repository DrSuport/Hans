package commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public abstract class ACommand{
    public String name;
    public String description;
    public String access;
    public OptionData option;

    public ACommand(String name, String description, String access, OptionData option) {
        this.name = name;
        this.description = description;
        this.access = access;
        this.option = option;
    }


    public String getName() {return name;}

    public String getDescription() {return description;}

    public String getAccess() {return access;}

    public OptionData getOption(){return option;}


    public void Post(JDA jda, OptionData optionData){
        if(optionData!=null) jda.upsertCommand(name, description).addOption(optionData.getType(), optionData.getName(), optionData.getDescription(), optionData.isRequired()).queue();
        else jda.upsertCommand(name, description).queue();
    }


    public abstract void Execute(SlashCommandInteractionEvent event);


}
