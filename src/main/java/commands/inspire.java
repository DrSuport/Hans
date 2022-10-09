package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class inspire extends ACommand {
    public static String name = "inspire";
    public static String description = "Inspires you";
    public static String access = "none";
    public static OptionData option = null;

    public inspire(){
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event){
        try {
            URLConnection connection = new URL("https://inspirobot.me/api?generate=true").openConnection();
            InputStream input = connection.getInputStream();

            String pictureUrl = new BufferedReader(new InputStreamReader(input)).readLine();


            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle("Inspiration", null);
            eb.setColor(new Color(115, 172, 230));
            eb.addBlankField(false);
            eb.setImage(pictureUrl);

            event.reply("Inspiration").setEphemeral(false) // reply or acknowledge
                    .flatMap(v ->
                            event.getHook().editOriginalEmbeds(eb.build())
                    ).queue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
