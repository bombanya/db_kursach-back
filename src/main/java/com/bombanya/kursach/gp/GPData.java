package com.bombanya.kursach.gp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;

@ToString
@Getter
@Setter
public class GPData implements Persistable<Integer> {

    @Id
    @JsonProperty("NORAD_CAT_ID")
    private Integer noradId;
    @JsonProperty("OBJECT_ID")
    private String intldes;
    @JsonProperty("OBJECT_NAME")
    private String satName;
    @JsonProperty("LAUNCH_DATE")
    private LocalDate launch;
    @JsonProperty("DECAY_DATE")
    private LocalDate decay;
    @JsonProperty("INCLINATION")
    private Double inclination;
    @JsonProperty("RA_OF_ASC_NODE")
    private Double longitudeOfTheAscendingNode;
    @JsonProperty("ECCENTRICITY")
    private Double eccentricity;
    @JsonProperty("ARG_OF_PERICENTER")
    private Double argumentOfPeriapsis;
    @JsonProperty("MEAN_MOTION")
    private Double meanMotion;
    @JsonProperty("MEAN_ANOMALY")
    private Double meanAnomaly;
    private String tleEpoch;
    @JsonProperty("BSTAR")
    private String bstar;

    @JsonSetter("TLE_LINE1")
    public void setTleEpochAndBstar(String tle) {
        this.tleEpoch = tle.substring(18, 32);
        this.bstar = tle.substring(53, 61);
    }

    @Override
    @Transient
    public Integer getId() {
        return noradId;
    }

    @Override
    @Transient
    public boolean isNew() {
        return true;
    }
}
