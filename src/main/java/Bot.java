import commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.EnumSet;
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

        EnumSet<GatewayIntent> intents = EnumSet.of(
                // We need messages in guilds to accept commands from users
                GatewayIntent.GUILD_MESSAGES,
                // We need voice states to connect to the voice channel
                GatewayIntent.GUILD_VOICE_STATES,
                // Enable access to message.getContentRaw()
                GatewayIntent.MESSAGE_CONTENT,

                GatewayIntent.GUILD_EMOJIS_AND_STICKERS

        );

        EnumSet<CacheFlag> flags = EnumSet.of(
                CacheFlag.VOICE_STATE
        );

        // args[0] should be the token
        // We don't need any intents for this bot. Slash commands work without any intents!
        JDA jda = JDABuilder.createDefault(args[0], intents)
                .setEventPassthrough(true)
                .addEventListeners(new Bot())
                .setActivity(Activity.watching("PornHub kids"))
                //.enableCache(CacheFlag.VOICE_STATE)
                .enableCache(flags)
                .build();

        jda.awaitReady();

        jda.addEventListener();



        //Here you add commands to use
        //Base commands
        addCommand(new ping());
        addCommand(new inspire());
        addCommand(new say());

        //Music commands
        addCommand(new play());


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