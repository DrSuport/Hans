package commands;

import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class stop extends ACommand{
    private static PlayerManager INSTANCE;
    public static String name = "stop";
    public static String description = "Stop's the current song and clear's the queue";
    public static String access = null;
    public static OptionData option = null;



    public stop() {
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = getINSTANCE().getMusicManager(event.getGuild());

        musicManager.scheduler.audioPlayer.stopTrack();

        event.reply("").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Queue cleared!")
                ).queue();
    }

    public static PlayerManager getINSTANCE(){
        if(INSTANCE == null){
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
