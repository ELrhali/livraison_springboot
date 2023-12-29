package com.livraison.admins.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String CIN;
    private String roles;
    @Column(unique = true)
    private Long livreurId;
    @Column(unique = true)
    private Long  commercantId;
    private String phone;
    private String addrese;


}
