package commands;

import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class loop extends ACommand{
    private static PlayerManager INSTANCE;
    public static String name = "loop";
    public static String description = "Loop's currently playing song";
    public static String access = null;
    public static OptionData option = null;



    public loop() {
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event) {//TODO implement loop

    }

    public static PlayerManager getINSTANCE(){
        if(INSTANCE == null){
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
