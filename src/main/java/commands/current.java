package commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class current extends ACommand{
    private static PlayerManager INSTANCE;
    public static String name = "current";
    public static String description = "Show's currently playing song";
    public static String access = null;
    public static OptionData option = null;



    public current() {
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = getINSTANCE().getMusicManager(event.getGuild());

        musicManager.scheduler.audioPlayer.stopTrack();

        final AudioPlayer audioPlayer = musicManager.scheduler.audioPlayer;

        final AudioTrack track = audioPlayer.getPlayingTrack();

        event.reply("Checking if any track is playing").queue();

        if(track==null){
            event.getChannel().sendMessage("There is no track currently playing").queue();
            return;
        }

        final AudioTrackInfo info = track.getInfo();

        event.getChannel().sendMessageFormat("Now playing `%s` by `%s` (Link: <%s>)", info.title, info.author, info.uri).queue();


    }

    public static PlayerManager getINSTANCE(){
        if(INSTANCE == null){
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
