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
    private final static String name = "shuffle";
    private final static String description = "Shuffles queue";
    private final static String access = null;
    private final static OptionData option = null;



    public shuffle() {
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event) {
        super.Execute(event);
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
