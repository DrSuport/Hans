package commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class invite extends ACommand{
    public static String name = "invite";
    public static String description = "Generates invitation link for bot";
    public static String access = null;
    public static OptionData option = new OptionData(OptionType.BOOLEAN, "ispublic", "Should bot send invite to you or to everyone in chat", false);



    public invite() {
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event) {

        boolean isEphemeral;
        OptionMapping messageOption = event.getOption("ispublic");
        if(messageOption != null){
            isEphemeral = !messageOption.getAsBoolean();
        }else{
            isEphemeral = true;
        }



        event.reply("").setEphemeral(isEphemeral) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat(event.getJDA().getInviteUrl())
                ).queue();

    }
}
