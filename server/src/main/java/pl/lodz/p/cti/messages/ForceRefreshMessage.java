package pl.lodz.p.cti.messages;

public class ForceRefreshMessage {
    private Long tvId;

    public ForceRefreshMessage(Long tvId) {
        this.tvId = tvId;
    }

    public Long getTvId() {
        return tvId;
    }

    public void setTvId(Long tvId) {
        this.tvId = tvId;
    }
}
