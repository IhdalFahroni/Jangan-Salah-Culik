public abstract class Timer implements Runnable {
    private int timeRemaining;
    private boolean isRunning;
    private Thread timerThread;

    public Timer(int initialTime) {
        this.timeRemaining = initialTime;
        this.isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning && timeRemaining > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                break;
            }
            if (!isRunning) {
                break;
            }
            timeRemaining--;
            onTick(timeRemaining);
        }
        if (isRunning) {
            onTimerFinished();
        }
        isRunning = false;
    }

    public void startTimer() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        timerThread = new Thread(this, "DecisionTimerThread");
        timerThread.start();
    }

    public void stopTimer() {
        isRunning = false;
        if (timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
        }
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    protected abstract void onTick(int remainingSeconds);

    protected abstract void onTimerFinished();
}