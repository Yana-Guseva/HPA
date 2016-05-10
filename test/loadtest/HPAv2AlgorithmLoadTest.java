package loadtest;

import static org.junit.Assert.*;

import org.eltech.ddm.associationrules.apriori.hpa.HPAAlgorithm;
import org.eltech.ddm.associationrules.apriori.hpa.HPAMiningModel;
import org.eltech.ddm.associationrules.apriori.hpav2.HPAv2Algorithm;
import org.eltech.ddm.miningcore.MiningException;
import org.junit.Before;
import org.junit.Test;

public class HPAv2AlgorithmLoadTest extends HPALoadTest{
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws MiningException {
		System.out.println("----- HPAv2Algoritm -------");
		
//		for(int i=0; i < dataSets.length; i++){
			setSettings(3);
		
			HPAv2Algorithm algorithm = new HPAv2Algorithm(miningSettings);
			System.out.println("Start algorithm");
			miningModel = (HPAMiningModel) algorithm.buildModel(inputData);

			System.out.println("Finish algorithm. Calculation time: " + algorithm.getTimeSpentToBuildModel());
			
			verifyModel();
	}

}
