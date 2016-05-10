package loadtest;

import static org.junit.Assert.*;

import org.eltech.ddm.associationrules.apriori.hpa.HPAAlgorithm;
import org.eltech.ddm.associationrules.apriori.hpa.HPAMiningModel;
import org.eltech.ddm.miningcore.MiningException;
import org.junit.Before;
import org.junit.Test;

public class HPAAlgorithmLoadTest extends HPALoadTest{

	@Before
	public void setUp() throws Exception {
	}

	
	@Test
	public void testHPAAlgoritm() throws MiningException {
		System.out.println("----- HPAAlgoritm -------");
		
//		for(int i=0; i < dataSets.length; i++){
			setSettings(4);
		
			HPAAlgorithm algorithm = new HPAAlgorithm(miningSettings);
			System.out.println("Start algorithm");
			miningModel = (HPAMiningModel) algorithm.buildModel(inputData);

			System.out.println("Finish algorithm. Calculation time: " + algorithm.getTimeSpentToBuildModel());
			
			verifyModel();
			
		}
//	}

}
