package com.johnwstump.incentivizer.model.metric;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Table(name = "metric")
public @Data
class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotEmpty
    private String name;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDefault;
}
