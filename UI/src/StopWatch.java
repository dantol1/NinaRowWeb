import sun.util.calendar.LocalGregorianCalendar;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StopWatch
{
    private long startTime;
    private long currentTime;

    public void StartTime() {

        startTime = System.currentTimeMillis();
    }

    public String timeRunning() {

        String time;
        long seconds;
        long realSeconds;
        long realMinutes;

        long currentTime = System.currentTimeMillis();


        long runTime = currentTime - startTime;

        seconds = TimeUnit.MILLISECONDS.toSeconds(runTime);



        realMinutes = (seconds / 60);
        if (realMinutes > 60) {

            realMinutes %=60;
        }
        realSeconds = seconds % 60;

        if (realMinutes / 10  == 0) {

            time = "0" + realMinutes + ":";
        }
        else {

            time = realMinutes + ":";
        }

        if (realSeconds / 10 == 0) {

            time = time + "0" + realSeconds;
        }
        else {

            time = time + realSeconds;
        }

        return time;

    }
}
