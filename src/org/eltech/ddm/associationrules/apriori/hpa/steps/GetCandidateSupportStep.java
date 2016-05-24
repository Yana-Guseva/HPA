package org.eltech.ddm.associationrules.apriori.hpa.steps;

import java.util.List;
import java.util.Map;

import org.eltech.ddm.associationrules.ItemSet;
import org.eltech.ddm.associationrules.apriori.hpa.HPAMiningModel;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.algorithms.Step;
import org.eltech.ddm.miningcore.miningfunctionsettings.EMiningFunctionSettings;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class GetCandidateSupportStep extends Step {

	public GetCandidateSupportStep(EMiningFunctionSettings settings) throws MiningException {
		super(settings);
	}

	@Override
	protected EMiningModel execute(MiningInputStream inputData, EMiningModel model) throws MiningException {
		HPAMiningModel modelA = (HPAMiningModel) model;
		ItemSet itemSet = modelA.getLargeItemSetsList().get(modelA.getCurrentLargeItemSets()).get(modelA.getCurrentCandidate());
		
		if (modelA.getKItemSetsTransactionSubset().size() > 0) {
			Map<Integer, Map<List<String>, Integer>> currMap = modelA.getKItemSetsTransactionSubset().get(modelA.getCurrentLargeItemSets() + 1);
			if (currMap != null) {
				Map<List<String>, Integer> map = modelA.getKItemSetsTransactionSubset().get(modelA.getCurrentLargeItemSets() + 1).get(modelA.getIdModel());
				if (map.containsKey(itemSet.getItemIDList())) {
					itemSet.setSupportCount(map.get(itemSet.getItemIDList()));
				}
				else {
					itemSet.setSupportCount(0);
				}
			}
		}
		
		return modelA;
	}
}
