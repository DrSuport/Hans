package commands;


import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;


public class queue extends ACommand{
    public static String name = "queue";
    public static String description = "Shows queue";
    public static String access = null;
    public static OptionData option = new OptionData(OptionType.INTEGER, "page", "Which page should i display", false);



    public queue() {
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event) {
        super.Execute(event);
        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());

        StringBuilder response = new StringBuilder();

        int page;

        ArrayList<AudioTrack> audioTracks = new ArrayList<>();

        musicManager.scheduler.queue.drainTo(audioTracks);
        musicManager.scheduler.queue.addAll(audioTracks);

        OptionMapping messageOption = event.getOption("page");
        if(messageOption != null){
            page = messageOption.getAsInt();
        }else{
            page = 1;
        }

        page-=1;

        int start = 20*page;
        int end = 20*(page+1);

        if(end>audioTracks.size()) end = audioTracks.size();




        for (int i = start; i < end; i++) {
            AudioTrack track = audioTracks.get(i);
            response.append(i+1).append(". **'").append(track.getInfo().title).append("'** by **'").append(track.getInfo().author).append("'**\n");
        }

        if(end!=audioTracks.size()) response.append("And ").append(audioTracks.size() - end).append(" more...");

        final String out = response.toString();

        event.reply("").setEphemeral(false) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat(out)
                ).queue();
    }
}
