package nu.epsilon.rss.ui.components;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.animation.timing.TimingSource;

/**
 *
 * @author Pär Sikö
 */
public class NanoSource extends TimingSource {

    private long resolution;
    private boolean stop = false;
    private Logger logger = Logger.getLogger("nu.epsilon.rss.ui.components");

    @Override
    public void start() {
        new Thread() {

            @Override
            public void run() {
                stop = false;
                
                while (!stop) {
                    try {
                        sleep(resolution);
                    } catch (Exception e) {
                        logger.logp(Level.WARNING, this.getClass().toString(),
                                "run", "Error occured when sleeping", e);
                    }
                    timingEvent();
                }
            }
        }.start();
    }

    @Override
    public void stop() {
        stop = true;
    }

    @Override
    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    @Override
    public void setStartDelay(int arg0) {
    }
}
