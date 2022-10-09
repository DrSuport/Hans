import com.google.gson.Gson;
import commands.BaseCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class Bot extends ListenerAdapter
{
    private static CommandsList BaseCommandsList;
    private static CommandsList MusicCommandsList;
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

        Gson gson = new Gson();
        String BaseJson;
        String MusicJson;
        try{
            BaseJson = Files.readString(Path.of("src/main/resources/commands.json"), StandardCharsets.UTF_8);
            MusicJson = Files.readString(Path.of("src/main/resources/MusicCommands.json"), StandardCharsets.UTF_8);
        }catch (Exception e){
            System.out.println(e.getMessage());
            BaseJson = "";
            MusicJson = "";
        }
        BaseCommandsList = gson.fromJson(BaseJson, CommandsList.class);
        MusicCommandsList = gson.fromJson(MusicJson, CommandsList.class);

        jda.addEventListener();

        OptionData messageOption = new OptionData(OptionType.STRING, "message", "The message you want the bot say", true);

        for (Command c: BaseCommandsList.commands) {
            if(c.options.length>0){
                jda.upsertCommand(c.name, c.description).addOption(messageOption.getType(), messageOption.getName(), messageOption.getDescription(), messageOption.isRequired()).queue();
            }else{
                jda.upsertCommand(c.name, c.description).queue();
            }

        }

        for (Command c: MusicCommandsList.commands) {
            jda.upsertCommand(c.name, c.description).queue();
        }



    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {

        switch (event.getName()) {
            default -> {}
            //Base Commands
            case "ping" -> BaseCommands.ping(event);
            case "inspire" -> BaseCommands.inspire(event);
            case "say" -> BaseCommands.say(event);
            //Music Commands

        }
    }
}