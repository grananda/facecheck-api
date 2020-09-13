package com.grananda.facecheckapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class FaceMemoryCollection extends BaseEntity {

    @NotNull
    @Column(name = "collection_id")
    private String collectionId;

    @NotNull
    @Column(name = "collection_arn")
    private String collectionArn;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "collection")
    private Set<FacialMemory> faces = new HashSet<>();;
}
