package com.dam.starwars;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Star Wars API Application Tests")
@SpringBootTest
class StarWarsApiApplicationTests {

	@Test
	@DisplayName("Deve carregar o contexto da aplicação")
	void contextLoads() {
		// Teste básico para verificar se o contexto da aplicação carrega corretamente
	}
}
