package ch.so.agi.cli.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Feature {
    @JsonProperty("dataproduct_id")
    private String dataproductId;
    
    private String display;
    
    @JsonProperty("feature_id")
    private String featureId;
    
    @JsonProperty("id_field_name")
    private String idFieldName;

    public String getDataproductId() {
        return dataproductId;
    }

    public void setDataproductId(String dataproductId) {
        this.dataproductId = dataproductId;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }
}
