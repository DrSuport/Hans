
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RecieveAudio extends ListenerAdapter
{

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message message = event.getMessage();
        User author = message.getAuthor();
        String content = message.getContentRaw();
        Guild guild = event.getGuild();

        // Ignore message if bot
        if (author.isBot())
            return;

        // We only want to handle message in Guilds
        if (!event.isFromGuild())
        {
            return;
        }
        if (content.equals("!echo"))
        {
            onEchoCommand(event);
        }
    }

    /**
     * Handle command without arguments.
     *
     * @param event
     *        The event for this command
     */
    private void onEchoCommand(MessageReceivedEvent event)
    {
        // Note: None of these can be null due to our configuration with the JDABuilder!
        Member member = event.getMember();                              // Member is the context of the user for the specific guild, containing voice state and roles
        GuildVoiceState voiceState = member.getVoiceState();            // Check the current voice state of the user
        AudioChannel channel = voiceState.getChannel();                 // Use the channel the user is currently connected to
        if (channel != null)
        {
            connectTo(channel);                                         // Join the channel of the user
            onConnecting(channel, event.getChannel());                  // Tell the user about our success
        }
        else
        {
            onUnknownChannel(event.getChannel(), "your voice channel"); // Tell the user about our failure
        }
    }


    /**
     * Inform user about successful connection.
     *
     * @param channel
     *        The voice channel we connected to
     * @param messageChannel
     *        The text channel to send the message in
     */
    private void onConnecting(AudioChannel channel, MessageChannel messageChannel)
    {
        messageChannel.sendMessage("Connecting to " + channel.getName()).queue(); // never forget to queue()!
    }

    /**
     * The channel to connect to is not known to us.
     *
     * @param channel
     *        The message channel (text channel abstraction) to send failure information to
     * @param comment
     *        The information of this channel
     */
    private void onUnknownChannel(MessageChannel channel, String comment)
    {
        channel.sendMessage("Unable to connect to ``" + comment + "``, no such channel!").queue(); // never forget to queue()!
    }

    /**
     * Connect to requested channel and start echo handler
     *
     * @param channel
     *        The channel to connect to
     */
    private void connectTo(AudioChannel channel)
    {
        Guild guild = channel.getGuild();
        // Get an audio manager for this guild, this will be created upon first use for each guild
        AudioManager audioManager = guild.getAudioManager();
        // Create our Send/Receive handler for the audio connection
        EchoHandler handler = new EchoHandler();

        // The order of the following instructions does not matter!

        // Set the sending handler to our echo system
        audioManager.setSendingHandler(handler);
        // Set the receiving handler to the same echo system, otherwise we can't echo anything
        audioManager.setReceivingHandler(handler);
        // Connect to the voice channel
        audioManager.openAudioConnection(channel);
    }

    public static class EchoHandler implements AudioSendHandler, AudioReceiveHandler
    {
        /*
            All methods in this class are called by JDA threads when resources are available/ready for processing.

            The receiver will be provided with the latest 20ms of PCM stereo audio
            Note you can receive even while setting yourself to deafened!

            The sender will provide 20ms of PCM stereo audio (pass-through) once requested by JDA
            When audio is provided JDA will automatically set the bot to speaking!
         */
        private final Queue<byte[]> queue = new ConcurrentLinkedQueue<>();

        /* Receive Handling */

        @Override // combine multiple user audio-streams into a single one
        public boolean canReceiveCombined()
        {
            // limit queue to 10 entries, if that is exceeded we can not receive more until the send system catches up
            return queue.size() < 10;
        }

        @Override
        public void handleCombinedAudio(CombinedAudio combinedAudio)
        {
            // we only want to send data when a user actually sent something, otherwise we would just send silence
            if (combinedAudio.getUsers().isEmpty())
                return;

            byte[] data = combinedAudio.getAudioData(1.0f); // volume at 100% = 1.0 (50% = 0.5 / 55% = 0.55)
            queue.add(data);
        }


        /* Send Handling */

        @Override
        public boolean canProvide()
        {
            // If we have something in our buffer we can provide it to the send system
            return !queue.isEmpty();
        }

        @Override
        public ByteBuffer provide20MsAudio()
        {
            // use what we have in our buffer to send audio as PCM
            byte[] data = queue.poll();
            return data == null ? null : ByteBuffer.wrap(data); // Wrap this in a java.nio.ByteBuffer
        }

        @Override
        public boolean isOpus()
        {
            // since we send audio that is received from discord we don't have opus but PCM
            return false;
        }
    }
}
