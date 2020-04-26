package com.petsuite.Services.model;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "client")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Client extends InfoUser{
   
    @NotBlank
    private String client_name;
    
    @NotBlank
    private String client_phone;
    
    @NotBlank
    private String client_e_mail;
    
    @NotBlank
    private String client_address;
    
    @OneToMany(mappedBy = "client_d")
    private Set<Dog> dog;
    
    @OneToMany(mappedBy = "client_wi")
    private Set<WalkInvoice> walkInvoices;

    @OneToMany(mappedBy = "client_i")
    private Set<DogDaycareInvoice> dogDaycareInvoices;
    
    @OneToOne(mappedBy = "client_p")
    private WalkPetition walkPetition;

}