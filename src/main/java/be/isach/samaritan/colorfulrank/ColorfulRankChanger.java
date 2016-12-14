package be.isach.samaritan.colorfulrank;

import be.isach.samaritan.Samaritan;
import be.isach.samaritan.util.MathUtils;
import com.google.common.collect.Maps;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import org.joda.time.DateTime;

import java.awt.*;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

/**
 * Created by sacha on 14/12/16.
 */
public class ColorfulRankChanger extends TimerTask {

    private Samaritan samaritan;
    private Role colorfulRole;

    public ColorfulRankChanger(Samaritan samaritan) {
        this.samaritan = samaritan;
        for(Role role : samaritan.getJda().getGuildById("186941943941562369").getRoles()) {
            if(role.getName().equalsIgnoreCase("Colorful")) {
                this.colorfulRole = role;
                break;
            }
        }
    }

    @Override
    public void run() {
        colorfulRole.getManager().setColor(randomizeColor());
    }

    private Color randomizeColor() {
        Random r = MathUtils.getRandom();
        int red = r.nextInt(256);
        int green = r.nextInt(256);
        int blue = r.nextInt(256);
        return new Color(red, green, blue);
    }

}
