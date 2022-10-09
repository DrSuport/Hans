import commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Bot extends ListenerAdapter
{
    private static Map<String, ACommand> commands = new HashMap<>();

    private static void addCommand(ACommand target){
        commands.put(target.getName(), target);
    }

    public static void main(String[] args) throws InterruptedException {

        if (args.length < 1) {
            System.out.println("You have to provide a token as first argument!");
            System.exit(1);
        }
        // args[0] should be the token
        // We don't need any intents for this bot. Slash commands work without any intents!
        JDA jda = JDABuilder.createLight(args[0], Collections.emptyList())
                .addEventListeners(new Bot())
                .setActivity(Activity.watching("PornHub kids"))
                .build();

        jda.awaitReady();

        jda.addEventListener();


        //Here you add commands to use
        addCommand(new ping());
        addCommand(new inspire());
        addCommand(new say());


        for (Map.Entry<String, ACommand> entry : commands.entrySet()){
            ACommand command = entry.getValue();
            OptionData optionData = command.getOption();
            String name = command.getName();
            String description = command.getDescription();

            if(optionData!=null) jda.upsertCommand(name, description).addOption(optionData.getType(), optionData.getName(), optionData.getDescription(), optionData.isRequired()).queue();
            else jda.upsertCommand(name, description).queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        try{
            commands.get(event.getName()).Execute(event);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}