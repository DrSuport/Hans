package commands;

import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;

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
        OptionMapping messageOption = event.getOption("url");
        String URL = messageOption.getAsString();

        System.out.println(event.getMember().getVoiceState().inAudioChannel());
        System.out.println(event.getMember().getVoiceState().getChannel());

        if(!event.getMember().getVoiceState().inAudioChannel()){
            event.reply("Message").setEphemeral(true) // reply or acknowledge
                    .flatMap(v ->
                            event.getHook().editOriginalFormat("You need to be in a voice channel for this command to work")
                    ).queue();
            return;
        }

        if(!event.getJDA().getVoiceChannels().contains(event.getMember().getVoiceState().getChannel())){
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

            audioManager.openAudioConnection(memberChannel);
        }

        if(!isUrl(URL)){
            URL = "ytsearch:" + URL + " audio";
        }

        PlayerManager.getINSTANCE().loadAndPlay((TextChannel) event.getMessageChannel(), URL);

    }
}
