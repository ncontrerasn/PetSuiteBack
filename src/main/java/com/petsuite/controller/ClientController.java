package com.petsuite.controller;

import com.petsuite.Services.dto.Client_Dto;
import com.petsuite.Services.dto.DogDayCare_Dto;
import com.petsuite.Services.model.Client;
import com.petsuite.Services.model.Dog;
import com.petsuite.Services.model.DogDaycare;
import com.petsuite.Services.model.WalkPetition;
import com.petsuite.Services.repository.ClientRepository;
import com.petsuite.Services.repository.DogDaycareRepository;
import com.petsuite.Services.repository.DogRepository;
import com.petsuite.Services.repository.InfoUserRepository;
import com.petsuite.Services.repository.WalkPetitionRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class ClientController {

    @Autowired
    WalkPetitionRepository walkPetitionRepository;

    @Autowired
    ClientRepository clientRepository;
    
    
    @Autowired
    DogDaycareRepository dogdaycareRepository;

    @Autowired
    DogRepository dogRepository;

    @Autowired
    InfoUserRepository infoUserRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/all")
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @PostMapping("/load")//Retorna una estructura de tipo client vacia si ya esta utilizado el nombre de usuario
    public Client_Dto createClient(@Valid @RequestBody Client_Dto client) {

        System.out.println("Entramos al load client");
        if(!infoUserRepository.existsById(client.getUser())){
            Client realClient=new Client(client.getClient_address(),
                    null,null,null,null);
            realClient.setUser(client.getUser());
            realClient.setPassword(client.getPassword());
            realClient.setRole("ROLE_CLIENT");
            realClient.setClient_address(client.getClient_address());
            realClient.setPhone(client.getClient_phone());
            realClient.setName(client.getClient_name());
            realClient.setE_mail(client.getClient_e_mail());
            clientRepository.save(realClient);
            return client;
        }
        return null;
    }

    @PostMapping("/dogList")
    public List<Dog> myDogList(@Valid @RequestBody String user){
        return dogRepository.findByUser(user);
    }
    
    
     @GetMapping("/allDogDayCares")
    public List<DogDayCare_Dto> getAllDogDayCares() {
         System.out.println("Diego esta solicitando todas las guarderias");
        List<DogDaycare> lista=dogdaycareRepository.findAll();
        List<DogDayCare_Dto> listaEnviar= new ArrayList<>();
         for (int i = 0; i < lista.size(); i++) {
             DogDayCare_Dto  guarderia = new DogDayCare_Dto(lista.get(i).getE_mail(), lista.get(i).getDog_daycare_address(), lista.get(i).getDog_daycare_type(), lista.get(i).getPhone(), lista.get(i).getDog_daycare_score(), lista.get(i).getName(), lista.get(i).getDog_daycare_base_price(), lista.get(i).getDog_daycare_tax());
             listaEnviar.add(guarderia);
             
         }
         return listaEnviar;
    }
    
    

    @PostMapping("/mypetition")
    public List<WalkPetition> myPetition(@Valid @RequestBody String user){
        return walkPetitionRepository.findPetitionsByUser(user);
    }

    public String getClientJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("CLIENT");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Token " + token;
    }

}



