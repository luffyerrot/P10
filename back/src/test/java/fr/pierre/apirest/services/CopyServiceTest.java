package fr.pierre.apirest.services;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.pierre.apirest.entities.Copy;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CopyServiceTest {

	@Autowired
	CopyService copyService;
	
	@Test
	@Transactional
	@Rollback
	public void saveUpdateDeleteProcessCopy() {
		Copy copy = new Copy("bon");
	
		Copy copyReturn = copyService.save(copy);
		
		Assert.assertEquals("bon", copyReturn.getEtat());
		
		copyReturn.setEtat("mauvais");
		
		Copy copyUpdate = copyService.update(copyReturn);
		
		Assert.assertEquals("mauvais", copyUpdate.getEtat());
			
		copyService.delete(copyUpdate.getId());
	}
}
