package org.eltech.ddm.associationrules.apriori.hpa;

import org.eltech.ddm.associationrules.AssociationRulesFunctionSettings;
import org.eltech.ddm.associationrules.apriori.AprioriModelTest;
import org.eltech.ddm.associationrules.apriori.hpa.HPAAlgorithm;
import org.eltech.ddm.associationrules.apriori.hpa.HPAMiningModel;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;

public class HPAAlgorithmModelTest extends AprioriModelTest {
	@Override
	protected HPAMiningModel buildModel(AssociationRulesFunctionSettings miningSettings, MiningInputStream inputData)
			throws MiningException {
		HPAAlgorithm algorithm = new HPAAlgorithm(miningSettings);
		HPAMiningModel model = (HPAMiningModel) algorithm.buildModel(inputData);

		System.out.println("calculation time [s]: " + algorithm.getTimeSpentToBuildModel());

		return model;
	}

}
