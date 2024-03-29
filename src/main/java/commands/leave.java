package commands;

import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;


public class leave extends ACommand{
    private final static String name = "leave";
    private final static String description = "Make's bot leave the channel";
    private final static String access = null;
    private final static OptionData option = null;



    public leave() {
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event) {
        super.Execute(event);
        if(!event.getMember().getVoiceState().inAudioChannel()){
            event.reply("").setEphemeral(true) // reply or acknowledge
                    .flatMap(v ->
                            event.getHook().editOriginalFormat("You need to be in a voice channel for this command to work")
                    ).queue();
            return;
        }

        if(event.getJDA().getVoiceChannels().contains(event.getMember().getVoiceState().getChannel())){
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());

            musicManager.scheduler.audioPlayer.stopTrack();
            audioManager.closeAudioConnection();
        }

        event.reply("").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Cya")
                ).queue();

    }


}
