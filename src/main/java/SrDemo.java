import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;
import voiceConntrol.AudioEchoExample;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SrDemo {
    public static void main(String[] args) {

        LibVosk.setLogLevel(LogLevel.DEBUG);

        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 60000, 16, 2, 4, 44100, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine microphone;
        SourceDataLine speakers;

        try (Model model = new Model("src/main/resources/vosk-model-small-en-us-0.15");
             Recognizer recognizer = new Recognizer(model, 120000)) {
            try {

                microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);
                microphone.start();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int numBytesRead;
                int CHUNK_SIZE = 1024;
                int bytesRead = 0;

                DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
                speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                speakers.open(format);
                speakers.start();
                byte[] b = new byte[4096]; //TODO on join or command (idk) push bytes from AudioEchoExample

                while (bytesRead <= 100000000) {
                    numBytesRead = microphone.read(b, 0, CHUNK_SIZE);
                    bytesRead += numBytesRead;

                    out.write(b, 0, numBytesRead);

                    speakers.write(b, 0, numBytesRead);

                    if (recognizer.acceptWaveForm(b, numBytesRead)) {
                        System.out.println(recognizer.getResult());
                    } else {
                        System.out.println(recognizer.getPartialResult());
                    }
                }
                System.out.println(recognizer.getFinalResult());
                speakers.drain();
                speakers.close();
                microphone.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}