package com.grananda.facecheckapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(name = "first_name")
    @NotNull
    @Size(min = 2, max = 15)
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    @Size(min = 2, max = 25)
    private String lastName;

    @Column(name = "username")
    @NotNull
    @Size(min = 5, max = 15)
    private String username;

    @Column(name = "email")
    @NotNull
    @Email
    @Size(min = 6, max = 50)
    private String email;

    @Column(name = "password")
    @NotNull
    @Size(min = 8, max = 50)
    private String password;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "face_memory_id", referencedColumnName = "id")
    private FaceMemory faceMemory;
}
