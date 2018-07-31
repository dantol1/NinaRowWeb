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

        time = String.format("%d:%d", minutes, seconds);

        return time;
    }
}
