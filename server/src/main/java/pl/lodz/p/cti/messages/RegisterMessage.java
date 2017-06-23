package pl.lodz.p.cti.messages;

public class RegisterMessage {
    private Long tvId;

    public RegisterMessage() {}

    public RegisterMessage(Long tvId) {
        this.tvId = tvId;
    }

    public Long getTvId() {
        return tvId;
    }

    public void setTvId(Long tvId) {
        this.tvId = tvId;
    }
}
