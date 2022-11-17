package commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class play extends ACommand{
    public static String name = "play";
    public static String description = "Play's music from youtube";
    public static String access = null;
    public static OptionData option = new OptionData(OptionType.STRING, "url", "URL to youtube video or playlist", true);



    public boolean isUrl(String url){
        try{
            new URI(url);
            return true;
        }catch (URISyntaxException e){
            return false;
        }
    }

    public play() {
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event) {
        super.Execute(event);
        OptionMapping messageOption = event.getOption("url");
        String URL = messageOption.getAsString();


        Member member = event.getMember();                              // Member is the context of the user for the specific guild, containing voice state and roles
        GuildVoiceState voiceState = member.getVoiceState();            // Check the current voice state of the user


        event.reply("Searching for channel").setEphemeral(true).queue();


        if(!event.getMember().getVoiceState().inAudioChannel()){
            event.reply("").setEphemeral(true) // reply or acknowledge
                    .flatMap(v ->
                            event.getHook().editOriginalFormat("You need to be in a voice channel for this command to work")
                    ).queue();
            return;
        }

        if(event.getJDA().getVoiceChannels().contains(event.getMember().getVoiceState().getChannel())){
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

            audioManager.openAudioConnection(memberChannel);
        }

        if(!isUrl(URL)){
            URL = "ytsearch:" + URL + " audio";
        }

        event.getHook().editOriginalFormat("Got it!").queue();


        PlayerManager.getINSTANCE().loadAndPlay((TextChannel) event.getMessageChannel(), URL);

    }
}
