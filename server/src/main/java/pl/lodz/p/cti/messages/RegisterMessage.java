package pl.lodz.p.cti.messages;

public class RegisterMessage {
    private String hash;

    public RegisterMessage() {}

    public RegisterMessage(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
