package com.nnk.springboot.ut;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
/*import org.junit.Assert;
import org.junit.Test;*/
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
/*import org.junit.runner.RunWith;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
//@RunWith(SpringRunner.class)
@SpringBootTest
public class CurvePointTests {

	@Autowired
	private CurvePointRepository curvePointRepository;

	//TODO @Test

	//TODO Supprimer ce test historique
	@Test
	public void curvePointTest() {
		CurvePoint curvePoint = new CurvePoint(10, 10d, 30d);

		// Save
		curvePoint = curvePointRepository.save(curvePoint);
		assertThat(curvePoint.getId()).isNotNull();
		/*		Assert.assertNotNull(curvePoint.getId());
		Assert.assertTrue(curvePoint.getCurveId() == 10);

		// Update
		curvePoint.setCurveId(20);
		curvePoint = curvePointRepository.save(curvePoint);
		Assert.assertTrue(curvePoint.getCurveId() == 20);

		// Find
		List<CurvePoint> listResult = curvePointRepository.findAll();
		Assert.assertTrue(listResult.size() > 0);

		// Delete
		Integer id = curvePoint.getId();
		curvePointRepository.delete(curvePoint);
		Optional<CurvePoint> curvePointList = curvePointRepository.findById(id);
		Assert.assertFalse(curvePointList.isPresent());*/
	}

}
