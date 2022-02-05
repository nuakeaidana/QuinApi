package POJO;

/**
 * No args constructor for use in serialization
 *
 */
public class Metadata {
    private String parentId;
    private boolean _private;

    public Metadata(String parentId, boolean _private) {
        super();
        this.parentId = parentId;
        this._private = _private;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isPrivate() {
        return _private;
    }

    public void setPrivate(boolean _private) {
        this._private = _private;
    }

}


