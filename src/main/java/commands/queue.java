package commands;


import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class queue extends ACommand{
    private static PlayerManager INSTANCE;
    public static String name = "queue";
    public static String description = "Shows queue";
    public static String access = null;
    public static OptionData option = null;



    public queue() {
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
