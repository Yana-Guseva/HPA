package org.eltech.ddm.associationrules.apriori.hpav2;

import static org.junit.Assert.*;

import org.eltech.ddm.associationrules.AssociationRulesFunctionSettings;
import org.eltech.ddm.associationrules.apriori.AprioriModelTest;
import org.eltech.ddm.associationrules.apriori.hpa.HPAMiningModel;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.junit.Test;

public class HPAv2AlgorithmModelTest extends AprioriModelTest{

	@Override
	protected HPAMiningModel buildModel(AssociationRulesFunctionSettings miningSettings, MiningInputStream inputData)
			throws MiningException {
		HPAv2Algorithm algorithm = new HPAv2Algorithm(miningSettings);
		HPAMiningModel model = (HPAMiningModel) algorithm.buildModel(inputData);

		System.out.println("calculation time [s]: " + algorithm.getTimeSpentToBuildModel());

		return model;
	}
}
