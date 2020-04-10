/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.petsuite.controller;

import com.petsuite.Services.dto.Client_Dto;
import com.petsuite.Services.dto.DogDayCare_Dto;
import com.petsuite.Services.dto.DogWalker_Dto;
import com.petsuite.Services.dto.InfoUser_Dto;
import com.petsuite.Services.model.InfoUser;
import com.petsuite.Services.repository.InfoUserRepository;
import com.petsuite.security.JwtGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 *
 * @author huber
 */
@RestController
@RequestMapping("/api/users")
public class InfoUserController {

    @Autowired
    InfoUserRepository infoUserRepository;
    @Autowired 
    ClientController clientController;
    @Autowired 
    DogDayCareController dogDaycareController;
    @Autowired 
    DogWalkerController dogWalkerController;
    @Autowired 
    TokenController tokenController;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    



    @GetMapping("/all")
    public List<InfoUser> getAllUsers() {
        return infoUserRepository.findAll();
    }
    
    
   
    
    
  
    
    @RequestMapping("/login")
    @ResponseBody
    public Object clientLogin(@Valid @RequestBody InfoUser_Dto user){

        
     String sqlA = "SELECT * FROM info_user where user = ?";
        String user__user = user.getUser();
        String user_password = user.getPassword();
        
        
       
        List<InfoUser> ul= jdbcTemplate.query(sqlA, new Object[]{user__user}, (rs, rowNum) -> new InfoUser(
                        rs.getString("user"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
        InfoUser u;
        if (ul.isEmpty()==false){
            u = ul.get(0);
            if (u.getPassword().equals(user_password)){
                if("ROLE_CLIENT".equals(u.getRole())){
                    String sqlC = "SELECT * FROM info_user natural join client where user = ?";
                    List<Client_Dto> ul2= jdbcTemplate.query(sqlC, new Object[]{user.getUser()}, (rs, rowNum) -> new Client_Dto(
                        rs.getString("client_name"),
                        rs.getString("client_phone"),
                        rs.getString("client_e_mail"),
                        rs.getString("client_address")
                    ));
                    
                    if(ul2.get(0)!=null)
                    {
                      //  String token = clientController.getClientJWTToken(u.getUser());
                      //  ul2.get(0).setToken(token);
                        user.setRole(u.getRole());
                        String token= tokenController.generate(user);
                        ul2.get(0).setToken(token);
                        
                        return ul2.get(0);
                    }
                }
                if("ROLE_DOGWALKER".equals(u.getRole())){
                    String sqlP = "SELECT * FROM info_user natural join dog_walker where user = ?";
                    List<DogWalker_Dto> ul2= jdbcTemplate.query(sqlP, new Object[]{user.getUser()}, (rs, rowNum) -> new DogWalker_Dto(
                        rs.getString("dog_walker_name"),
                        rs.getString("dog_walker_phone"),
                        rs.getString("dog_walker_e_mail"),
                        rs.getFloat("dog_walker_score")
                    ));
                    if(ul2.get(0)!=null)
                    {
                         user.setRole(u.getRole());
                        String token = getJWTToken(u.getUser());
                        ul2.get(0).setToken(token);
                        return ul2.get(0);
                    }
                }
                if(u.getRole()=="ROLE_DOGDAYCARE"){
                    String sqlG = "SELECT * FROM info_user natural join dog_daycare where client_user = ?";
                    List<DogDayCare_Dto> ul2= jdbcTemplate.query(sqlA, new Object[]{user.getUser()}, (rs, rowNum) -> new DogDayCare_Dto(
                        rs.getString("dog_daycare_e_mail"),
                        rs.getString("dog_daycare_address"),
                        rs.getBoolean("dog_daycare_type"),
                        rs.getString("dog_daycare_phone"),
                        rs.getFloat("dog_daycare_score"),
                        rs.getString("dog_daycare_name")
                    ));
                    
                      if(ul2.get(0)!=null)
                      {
                           user.setRole(u.getRole());
                          String token = getJWTToken(u.getUser());
                          ul2.get(0).setToken(token);
                          return ul2.get(0);
                      }
                }
                
                
              
            }
            
        }
        return null;
        
    }

    private String getJWTToken(String username) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
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

		return "Bearer " + token;
	}
}
       


 
