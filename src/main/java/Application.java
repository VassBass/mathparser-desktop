import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.mainScreen.MainScreen;

import java.awt.*;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[]args){
        LOGGER.info("Application has started");
        EventQueue.invokeLater(() -> new MainScreen().setVisible(true));
    }
}
