package pl.lodz.p.cti.messages;

public class ScheduleMessage {
    private boolean exists;

    public ScheduleMessage(boolean exists) {
        this.exists = exists;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
