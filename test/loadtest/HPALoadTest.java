package loadtest;

import static org.junit.Assert.*;

import java.util.List;

import org.eltech.ddm.associationrules.AssociationRulesFunctionSettings;
import org.eltech.ddm.associationrules.ItemSet;
import org.eltech.ddm.associationrules.apriori.AprioriAlgorithmSettings;
import org.eltech.ddm.associationrules.apriori.AprioriMiningModel;
import org.eltech.ddm.associationrules.apriori.hpa.HPAMiningModel;
import org.eltech.ddm.inputdata.MiningArrayStream;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.inputdata.file.MiningArffStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.miningdata.ELogicalData;
import org.junit.Assert;
import org.junit.Test;

public class HPALoadTest {

protected String dataSets[] = {"T_200", "T_2000", "T_20000", "I_5", "I_10", "I_15", "I_10_20", "I_10_30", "I_10_50"};
	
	protected AssociationRulesFunctionSettings miningSettings;
	protected MiningInputStream inputData;
	protected HPAMiningModel miningModel; 
	
	protected void setSettings(int index) throws  MiningException{
		
		System.out.println(dataSets[index]);
		inputData = 
					new MiningArrayStream(new MiningArffStream("../data/arff/association/" + dataSets[index] + ".arff"));
		
        ELogicalData logicalData = inputData.getLogicalData();

        AprioriAlgorithmSettings algorithmSettings = new AprioriAlgorithmSettings();

        miningSettings = new AssociationRulesFunctionSettings(logicalData);
        miningSettings.setTransactionIDsArributeName("transactId");
        miningSettings.setItemIDsArributeName("itemId");
        miningSettings.setMinConfidence(0.01);
        miningSettings.setMinSupport(0.01);
        miningSettings.setAlgorithmSettings(algorithmSettings);
   		miningSettings.verify();

	}
	

	protected void verifyModel() {
        Assert.assertNotNull(miningModel);
        System.out.println("AssociationRuleSet: number of rules = " + miningModel.getAssociationRuleSet().size());
        int l=0;
        for(List<ItemSet> list: miningModel.getLargeItemSetsList()){
        	System.out.println("Level " + l++ + " number of rules " + list.size());
        }

	}


}
