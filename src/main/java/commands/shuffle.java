package commands;


import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;


public class shuffle extends ACommand{
    public static String name = "shuffle";
    public static String description = "Shuffles queue";
    public static String access = null;
    public static OptionData option = null;



    public shuffle() {
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());

        ArrayList<AudioTrack> audioTracks = new ArrayList<>();

        musicManager.scheduler.queue.drainTo(audioTracks);
        Random rand = new Random();

        while (!audioTracks.isEmpty()){
            int n = rand.nextInt(audioTracks.size());
            AudioTrack audioTrack = audioTracks.remove(n);
            musicManager.scheduler.queue.add(audioTrack);
        }


        event.reply("").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Queue shuffled!")
                ).queue();
    }
}
