package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.io.*;
import java.net.*;

public class BaseCommands {
    public static void ping(SlashCommandInteractionEvent event){
        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                ).queue(); // Queue both reply and edit
    }

    public static void inspire(SlashCommandInteractionEvent event){
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
    public static void say(SlashCommandInteractionEvent event){

    }
}
