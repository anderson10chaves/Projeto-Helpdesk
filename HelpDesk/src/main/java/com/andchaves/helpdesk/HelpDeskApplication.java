package com.andchaves.helpdesk;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.andchaves.helpdesk.api.entity.Usuario;
import com.andchaves.helpdesk.api.enums.ProfileEnum;
import com.andchaves.helpdesk.api.repository.UsuarioRepository;

@SpringBootApplication
public class HelpDeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpDeskApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			initUsers(usuarioRepository, passwordEncoder);
		};
	}
	
	private void initUsers(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		Usuario admin = new Usuario();
		admin.setEmail("andchaves10@gmail.com");
		admin.setPassword(passwordEncoder.encode("135121"));
		admin.setProfile(ProfileEnum.ROLE_ADMIN);
		
		Usuario find = usuarioRepository.findByEmail("andchaves10@gmail.com");
		if (find == null) {
			usuarioRepository.save(admin);
		}
	}

}

