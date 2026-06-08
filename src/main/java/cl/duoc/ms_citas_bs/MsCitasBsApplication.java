package cl.duoc.ms_citas_bs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsCitasBsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCitasBsApplication.class, args);
	}

}
