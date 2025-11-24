
public class Timer implements Runnable {
    private int timeRemaining;
    private boolean isRunning;
    private Thread timerThread;
    
    public Timer(int initialTime) {
        this.timeRemaining = initialTime;
        this.isRunning = false;
    }
    
    @Override
    public void run() {
        // TODO: Implement timer countdown logic
    }
    
    public void startTimer() {
        // TODO: Implement timer start logic
    }
    
    public void stopTimer() {
        // TODO: Implement timer stop logic
    }
    
    public int getTimeRemaining() {
        // TODO: Implement get remaining time
        return timeRemaining;
    }
    
    // Getters and Setters
    public void setTimeRemaining(int timeRemaining) { this.timeRemaining = timeRemaining; }
    public boolean isRunning() { return isRunning; }
    public void setRunning(boolean running) { isRunning = running; }
}
