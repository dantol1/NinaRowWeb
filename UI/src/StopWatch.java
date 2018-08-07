import sun.util.calendar.LocalGregorianCalendar;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class StopWatch
{
    private long startTime;
    private long currentTime;

    public  StopWatch()
    {
        startTime = currentTime = System.nanoTime();
    }


    public String timeRunning()
    {
        long runTime;
        double minutes = 0, seconds;
        String time;

        currentTime = System.nanoTime();
        runTime = currentTime - startTime;
        seconds = runTime * 0.0000000001;
        while(seconds > 60)
        {
            seconds -= 60;
            minutes++;
        }
        //TODO check the time format
        time = String.format("%d:%d", (int)minutes, (int)seconds);

        return time;
    }
}
