package loadtest;

import static org.junit.Assert.*;

import org.eltech.ddm.associationrules.apriori.hpa.HPAMiningModel;
import org.eltech.ddm.associationrules.apriori.hpa.ParallelHPAAlgorithm;
import org.eltech.ddm.handlers.ExecutionSettings;
import org.eltech.ddm.handlers.thread.MultiThreadedExecutionEnvironment;
import org.eltech.ddm.inputdata.DataSplitType;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.miningfunctionsettings.DataProcessingStrategy;
import org.eltech.ddm.miningcore.miningfunctionsettings.MiningModelProcessingStrategy;
import org.eltech.ddm.miningcore.miningtask.EMiningBuildTask;
import org.junit.Test;

public class HPAParallelAlgorithmLoadTest extends HPALoadTest{

private final int NUMBER_HANDLERS = 2;
	
	@Test
	public void testParallelHPAAlgorithm() throws MiningException {
		System.out.println("----- ParallelHPAAlgorithm (" + NUMBER_HANDLERS + ")-------");
		
		try{
//			for(int i=0; i < dataSets.length; i++){
				setSettings(0);
			
				miningSettings.getAlgorithmSettings().setDataSplitType(DataSplitType.block);
				miningSettings.getAlgorithmSettings().setDataProcessingStrategy(DataProcessingStrategy.SeparatedDataSet);
				miningSettings.getAlgorithmSettings().setModelProcessingStrategy(MiningModelProcessingStrategy.SeparatedMiningModel);
			
				ExecutionSettings executionSettings = new ExecutionSettings();
//				executionSettings.setNumberHandlers(NUMBER_HANDLERS);
				miningSettings.getAlgorithmSettings().setNumberHandlers(NUMBER_HANDLERS);
				MultiThreadedExecutionEnvironment environment = new MultiThreadedExecutionEnvironment(executionSettings); 
				
				ParallelHPAAlgorithm algorithm = new ParallelHPAAlgorithm(miningSettings);
	
				EMiningBuildTask buildTask = new EMiningBuildTask();
				buildTask.setInputStream(inputData);
				buildTask.setMiningAlgorithm(algorithm); 
				buildTask.setMiningSettings(miningSettings);
				buildTask.setExecutionEnvironment(environment);
				System.out.println("Start algorithm");
				miningModel = (HPAMiningModel) buildTask.execute();
				System.out.println("Finish algorithm");
				
				verifyModel();
//			}
		}
		catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}

}
