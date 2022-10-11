package commands;

import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class leave extends ACommand{ //TODO implement leave
    private static PlayerManager INSTANCE;
    public static String name = "leave";
    public static String description = "Make's bot leave the channel";
    public static String access = null;
    public static OptionData option = null;



    public leave() {
        super(name, description, access, option);
    }

    @Override
    public void Execute(SlashCommandInteractionEvent event) {

    }

    public static PlayerManager getINSTANCE(){
        if(INSTANCE == null){
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
