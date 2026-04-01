package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.TokenUtil;
import com.devsuperior.dsmovie.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;

	@Mock
	private ScoreRepository repository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TokenUtil tokenUtil;

	private long nonExistingMovieId;

	private String adminToken;
	private UserEntity user;
	private ScoreEntity score, scoreNonExistingMovieId;
	private MovieEntity movie, movieNonExistingMovieId;
	private ScoreDTO scoreDTO;

	@BeforeEach
	void setUp() throws Exception {
		nonExistingMovieId = 29L;

		user = UserFactory.createUserEntity();
		adminToken = "eyJraWQiOiJjOTZhNGU1Yi0zZWU0LTQwZGItOGEwNS1lYTQzMDE3YjFiNmYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJteWNsaWVudGlkIiwiYXVkIjoibXljbGllbnRpZCIsIm5iZiI6MTc3NDk3NzYyMiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIiwiZXhwIjoxNzc1MDY0MDIyLCJpYXQiOjE3NzQ5Nzc2MjIsImp0aSI6IjRkM2IyOGJiLTJhNjItNGZlMi04ODM3LTMxNzQyZDBhZDdmMyIsImF1dGhvcml0aWVzIjpbIlJPTEVfQ0xJRU5UIiwiUk9MRV9BRE1JTiJdLCJ1c2VybmFtZSI6Im1hcmlhQGdtYWlsLmNvbSJ9.aFVCnK5-3-EDqWm4Gm4FdoKKgQzLwG1xJ8Cq0g1vjy6tpx4d2FFoaBiOg3VaK3VZEHzTC6SQ2OcnBqcHjeJaSQmR8wdtqY5iDByc8cOBxbz8jBtWDhtjzjYNTIMbjeD9_HCUHX8Su8SNraBq83Ol3PiXpH2-g6YDAf9MYJft9pXiyYyLLo31gQHxZJ7CSIJgHD8Qj4qdNOncLF8MUsEDvemv1nz4-4UfWPkIMwfbmAp5CkcEXxW_xxJgpkewPBKLCLW0E2LzYMTgQi06UasHQcNx9GZUI4qiBo4-3rR610SU3iLCDZUzELlzSBIveltfaWOTAvNVIBsrFFbG66oXSg";

		score = ScoreFactory.createScoreEntity();
		scoreDTO = ScoreFactory.createScoreDTO();
		movie = MovieFactory.createMovieEntity();
		movieNonExistingMovieId = movie;
		scoreNonExistingMovieId = score;

		movieNonExistingMovieId.setId(nonExistingMovieId);
		scoreNonExistingMovieId.setMovie(movieNonExistingMovieId);

		Mockito.when(repository.saveAndFlush(any())).thenReturn(score);
	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {
		MovieDTO result = service.saveScore(scoreDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), score.getId());
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		scoreDTO = new ScoreDTO(scoreNonExistingMovieId);
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.saveScore(scoreDTO);
		});
	}
}
