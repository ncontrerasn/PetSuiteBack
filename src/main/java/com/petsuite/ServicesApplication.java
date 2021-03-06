package com.petsuite;

import com.petsuite.Services.basics.CadenaDoble;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableEncryptableProperties
//@ComponentScan({"com.petsuite.Services.basics"})
//@ComponentScan({"com.petsuite.Services.services"})
public class ServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicesApplication.class, args);
                
          
             
        
        }

}