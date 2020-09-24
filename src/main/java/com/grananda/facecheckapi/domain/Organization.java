package com.grananda.facecheckapi.domain;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class Organization extends BaseEntity {

    @Column(name = "name")
    @NotNull
    @Size(min = 2, max = 30)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    private Set<User> users = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    private Set<FaceMemoryCollection> memoryCollections = new HashSet<>();
}
