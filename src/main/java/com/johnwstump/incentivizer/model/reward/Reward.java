package com.johnwstump.incentivizer.model.reward;


import com.johnwstump.incentivizer.model.challenge.Challenge;
import com.johnwstump.incentivizer.model.comparator.Comparator;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Table(name = "reward")
public @Data
class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "reward")
    private Challenge challenge;

    @Column(columnDefinition = "VARCHAR(5)")
    private Comparator entryComparator;

    private int entryTarget;

    @Column(columnDefinition = "VARCHAR(5)")
    private Comparator pointComparator;

    private int pointTarget;

    @NotNull
    @NotEmpty
    @Column(columnDefinition = "VARCHAR(5000)")
    private String reward;

    public String getEntryComparatorCode() {
        return entryComparator.getSymbol();
    }

    public String getPointComparatorCode() {
        return pointComparator.getSymbol();
    }

    public boolean satisfiesEntryRequirements() {
        return entryComparator.compare(challenge.getNumEntries(), entryTarget);
    }

    public boolean satisfiesPointRequirements() {
        return pointComparator.compare(challenge.getNumPoints(), pointTarget);
    }

    public boolean failed() {
        boolean tooManyEntries = !satisfiesEntryRequirements()
                && getEntryComparator().canFail() && challenge.getNumEntries() >= entryTarget;
        boolean tooManyPoints = !satisfiesPointRequirements()
                && getPointComparator().canFail() && challenge.getNumPoints() >= pointTarget;

        return tooManyEntries || tooManyPoints;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "id=" + id +
                ", entryComparator=" + entryComparator +
                ", entryTarget=" + entryTarget +
                ", pointComparator=" + pointComparator +
                ", pointTarget=" + pointTarget +
                ", reward='" + reward + '\'' +
                '}';
    }
}
