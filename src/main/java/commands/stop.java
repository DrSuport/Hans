package commands;

import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class stop extends ACommand{
    private final static String name = "stop";
    private final static String description = "Stop's the current song and clear's the queue";
    private final static String access = null;
    private final static OptionData option = null;



    public stop() {
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event) {
        super.Execute(event);
        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());

        //musicManager.scheduler.audioPlayer.stopTrack();
        musicManager.scheduler.queue.clear();
        musicManager.scheduler.nextTrack();



        event.reply("").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Queue cleared!")
                ).queue();
    }


}
