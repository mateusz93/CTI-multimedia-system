package pl.lodz.p.cti.models;

public class MapJSModel {
	private Long key;
	private String label;
	public MapJSModel(CollectionModel model) {
		super();
		this.key = model.getId();
		this.label = model.getName();
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
