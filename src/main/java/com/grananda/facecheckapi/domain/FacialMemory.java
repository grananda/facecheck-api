package com.grananda.facecheckapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class FacialMemory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private FaceMemoryCollection collection;

    @OneToOne(mappedBy = "address")
    private User user;
}
