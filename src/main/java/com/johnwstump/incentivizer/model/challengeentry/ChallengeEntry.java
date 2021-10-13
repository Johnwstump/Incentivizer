package com.johnwstump.incentivizer.model.challengeentry;

import com.johnwstump.incentivizer.model.challenge.Challenge;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Represents a single entry for a given challenge.
 * <p>
 * The entry is essentially a wrapper around a single numeric (double) value entered for the challenge.
 */
@Entity
@NoArgsConstructor
@Table(name = "challengeEntry")
public @Data
class ChallengeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long challengeId;

    @Basic
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "challengeId", referencedColumnName = "id", updatable = false, insertable = false)
    @Generated(GenerationTime.INSERT)
    private Challenge challenge;

    private double value;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Generated(GenerationTime.INSERT)
    private Calendar created;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Generated(GenerationTime.INSERT)
    private Calendar updated;

    /**
     * Calculates the points obtained for this entry.
     *
     * @return The number of points obtained by this challenge entry.
     */
    double getPoints() {
        return this.challenge.getNumPoints(this);
    }

    @Override
    public String toString() {
        return "ChallengeEntry{" +
                "id=" + id +
                ", challengeId=" + challengeId +
                ", value=" + value +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
