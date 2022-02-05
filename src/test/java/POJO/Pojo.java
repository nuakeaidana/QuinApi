package POJO;

import lombok.*;
import org.testng.annotations.Test;


public class Pojo {
    private Record record;
    private Metadata metadata;

    public Pojo(Record record, Metadata metadata) {
        super();
        this.record = record;
        this.metadata = metadata;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}

