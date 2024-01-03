package com.livraison.admins.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String prenom;
    @Column(unique = true)
    private String email;
    private String password;
    private String cin;
    private String roles;
    @Column(unique = true)
    private Long livreurId;
    @Column(unique = true)
    private Long  commercantId;
    private String phone;
    private String addrese;
    @Column(name = "date_creation")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateCreation = LocalDate.now();

}
