package com.petsuite.Services.services;

import com.petsuite.Services.dto.Client_Dto;
import com.petsuite.Services.dto.DogDayCare_Dto;
import com.petsuite.Services.dto.DogWalker_Dto;
import com.petsuite.Services.dto.Dog_Dto;
import com.petsuite.Services.dto.InfoUser_Dto;
import com.petsuite.Services.model.Client;
import com.petsuite.Services.model.Dog;
import com.petsuite.Services.model.DogDaycare;
import com.petsuite.Services.model.DogWalker;
import com.petsuite.Services.repository.*;
import com.petsuite.Services.services.interfaces.IRegister;
import com.petsuite.controller.TokenController;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService implements IRegister {

    @Autowired
    InfoUserRepository infoUserRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    DogWalkerRepository dogWalkerRepository;

    @Autowired
    DogDaycareRepository dogDaycareRepository;

    @Autowired
    DogRepository dogRepository;
    
      @Autowired
    TokenController tokenController;

public static String encryptThisString(String input)
	{
		try {
			// getInstance() method is called with algorithm SHA-512
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			// digest() method is called
			// to calculate message digest of the input string
			// returned as array of byte
			byte[] messageDigest = md.digest(input.getBytes());
			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);
			// Convert message digest into hex value
			String hashtext = no.toString(16);
			// Add preceding 0s to make it 32 bit
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			// return the HashText
			return hashtext;
		}// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
    @Override
    public DogWalker_Dto createWalker(DogWalker_Dto dogWalker)
    {
        if(!infoUserRepository.existsById(dogWalker.getUser()))
        {
            DogWalker realDogWalker= new DogWalker(dogWalker.getDog_walker_score(), null);
            realDogWalker.setUser(dogWalker.getUser());
            realDogWalker.setPassword(encryptThisString(dogWalker.getPassword()));
            realDogWalker.setRole("ROLE_DOGWALKER");
            realDogWalker.setName(dogWalker.getDog_walker_name());
            realDogWalker.setDog_walker_score((float)3.0);
            realDogWalker.setPhone(dogWalker.getDog_walker_phone());
            realDogWalker.setE_mail(dogWalker.getDog_walker_e_mail());
            
            InfoUser_Dto user= new InfoUser_Dto(realDogWalker.getUser(), realDogWalker.getPassword(), "ROLE_DOGWALKER");
            
            String token= tokenController.generate(user);
            
            realDogWalker.setToken(token);
            
            dogWalkerRepository.save(realDogWalker);

            return dogWalker;
        }
        return null;
    }

    @Override
    public DogDayCare_Dto createDogDaycare(DogDayCare_Dto dogDaycare)
    {
        if(!infoUserRepository.existsById(dogDaycare.getUser()))
        {
            DogDaycare realDogDayCare= new DogDaycare(dogDaycare.getDog_daycare_address(), dogDaycare.getDog_daycare_type() , dogDaycare.getDog_daycare_score(),dogDaycare.getDog_daycare_price_base(),dogDaycare.getDog_daycare_tax(), null, null);
            realDogDayCare.setUser(dogDaycare.getUser());
            realDogDayCare.setPassword(encryptThisString(dogDaycare.getPassword()));
            realDogDayCare.setRole("ROLE_DOGDAYCARE");
            realDogDayCare.setName(dogDaycare.getDog_daycare_name());
            realDogDayCare.setDog_daycare_score((float)3.0);
            realDogDayCare.setE_mail(dogDaycare.getDog_daycare_e_mail());
            realDogDayCare.setPhone(dogDaycare.getDog_daycare_phone());
            
            
            InfoUser_Dto user= new InfoUser_Dto(dogDaycare.getUser(), dogDaycare.getPassword(), "ROLE_DOGDAYCARE");
            
            String token= tokenController.generate(user);
            
            realDogDayCare.setToken(token);
            dogDaycareRepository.save(realDogDayCare);

            return dogDaycare;
        }
        return null;
    }

    @Override
    public Client_Dto createClient(Client_Dto client)
    {
        if(!infoUserRepository.existsById(client.getUser()))
        {
            Client realClient=new Client();
            realClient.setUser(client.getUser());
            realClient.setPassword(encryptThisString(client.getPassword()));
            realClient.setRole("ROLE_CLIENT");
            realClient.setClient_address(client.getClient_address());
            realClient.setPhone(client.getClient_phone());
            realClient.setName(client.getClient_name());
            realClient.setE_mail(client.getClient_e_mail());
            InfoUser_Dto user= new InfoUser_Dto(client.getUser(), client.getPassword(), "ROLE_CLIENT");
            
            String token= tokenController.generate(user);
            
            realClient.setToken(token);
            clientRepository.save(realClient);
            return client;
        }
        return null;
    }

    @Override
    public Dog_Dto createDog(Dog_Dto dog)
    {
        Dog dogReal=new Dog(dog.getDog_name(), dog.getDog_race(), dog.getDog_height(), dog.getDog_weight(), dog.getDog_age(), dog.getDog_notes(), dog.getClient_id());

        dogReal = dogRepository.save(dogReal);

        if(dogReal!=null)
            return dog;
        else
            return null;
    }
}
