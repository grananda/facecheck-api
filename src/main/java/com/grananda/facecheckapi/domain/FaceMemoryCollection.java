package com.grananda.facecheckapi.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class FaceMemoryCollection extends BaseEntity {

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "collection_id")
    private String collectionId;

    @NotNull
    @Column(name = "collection_arn")
    private String collectionArn;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "collection")
    private Set<FaceMemory> faces = new HashSet<>();;
}
