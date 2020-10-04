package com.grananda.facecheckapi.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class FaceMemory extends BaseEntity {

    @NotNull
    @Column(name = "face_id")
    String faceId;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private FaceMemoryCollection collection;

    @OneToOne(mappedBy = "faceMemory")
    private User user;
}
