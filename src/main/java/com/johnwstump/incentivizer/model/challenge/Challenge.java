package com.johnwstump.incentivizer.model.challenge;

import com.johnwstump.incentivizer.model.challengeentry.ChallengeEntry;
import com.johnwstump.incentivizer.model.goal.Goal;
import com.johnwstump.incentivizer.model.reward.Reward;
import com.johnwstump.incentivizer.model.user.impl.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Represents a single Challenge. Broadly speaking, a Challenge consists of 1 to many Goals, which,
 * when satisfied by Challenge Entries, generate Points. When the Challenge meets the thresholds indicated
 * in the Reward, the challenge has succeeded.
 */
@Entity
@NoArgsConstructor
@Table(name = "challenge")
public @Data
class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 500)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner")
    private User owner;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "goal", referencedColumnName = "id")
    private Goal goal;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "challengeId")
    private List<ChallengeEntry> entries;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reward", referencedColumnName = "id")
    private Reward reward;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Generated(GenerationTime.INSERT)
    private Calendar created;

    // If and when the owner acknowledged the success of this challenge.
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "successAcknowledged")
    private Calendar successAcknowledged;

    /**
     * @return A List containing all entries associated with this challenge. If no such entries exist,
     * the list will be empty.
     */
    public List<ChallengeEntry> getEntries() {
        if (this.entries == null) {
            this.entries = new ArrayList<>();
        }

        return entries;
    }

    /**
     * @return The number of entries for this challenge
     */
    public int getNumEntries() {
        if (entries == null) {
            return 0;
        }

        return entries.size();
    }

    /**
     * Calculates the number of points currently associated with the challenge
     * given its Goal and all entries currently associated with it.
     *
     * @return The number of points as computed using the goal and entries.
     */
    public double getNumPoints() {
        if (entries == null || entries.isEmpty()) {
            return 0;
        }

        double totalPoints = 0;

        for (ChallengeEntry entry : entries) {
            totalPoints += goal.determinePoints(entry);
        }

        return totalPoints;
    }

    /**
     * Calculates the number of points earned by this challenge
     * for the entry indicated.
     *
     * @param entry The Entry whose point contribution will be calculated.
     * @return The number of points for the entry.
     */
    public double getNumPoints(ChallengeEntry entry) {
        if (entry == null) {
            return 0;
        }

        return goal.determinePoints(entry);
    }

    /**
     * @return The id of the owning User of this
     * challenge.
     */
    public long getOwnerId() {
        return owner.getId();
    }

    /**
     * Indicates whether the challenge was completed.
     * <p>
     * Here, 'completed' implies 'successfully completed.' That is, challenges
     * where success is no longer possible are not considered complete.
     *
     * @return Whether the challenge is completed.
     */
    public boolean getCompleted() {
        return this.reward != null && this.reward.satisfiesEntryRequirements()
                && this.reward.satisfiesPointRequirements();
    }

    /**
     * Indicates whether the challenge has been failed. Not all challenges have a failure
     * state. When no number future entries, regardless of value, could lead to the challenge succeeding, it
     * is considered to have failed.
     *
     * @return Whether the challenge has been failed.
     */
    public boolean getFailed() {
        return this.reward != null && this.reward.failed();
    }
}
