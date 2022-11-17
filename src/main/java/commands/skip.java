package commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class skip extends ACommand{
    public static String name = "skip";
    public static String description = "Skip's the current song";
    public static String access = null;
    public static OptionData option = null;



    public skip() {
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event) {
        super.Execute(event);
        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());

        AudioPlayer audioPlayer = musicManager.scheduler.audioPlayer;

        if(audioPlayer.getPlayingTrack() == null){
            event.reply("").setEphemeral(true) // reply or acknowledge
                    .flatMap(v ->
                            event.getHook().editOriginalFormat("There is no track currently playing")
                    ).queue();
            return;
        }

        musicManager.scheduler.nextTrack();


        event.reply("").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Current song skipped!")
                ).queue();
    }

}
