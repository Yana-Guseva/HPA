package org.eltech.ddm.associationrules.apriori.hpav2.steps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eltech.ddm.associationrules.AssociationRulesFunctionSettings;
import org.eltech.ddm.associationrules.Item;
import org.eltech.ddm.associationrules.ItemSet;
import org.eltech.ddm.associationrules.ItemSets;
import org.eltech.ddm.associationrules.apriori.hpa.HPAMiningModel;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.algorithms.Step;
import org.eltech.ddm.miningcore.miningfunctionsettings.EMiningFunctionSettings;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class HPAGetLargeCandidateStep extends Step{
	final protected double minSupport;

	public HPAGetLargeCandidateStep(EMiningFunctionSettings settings) throws MiningException {
		super(settings);
		minSupport = ((AssociationRulesFunctionSettings)settings).getMinSupport();
	}

	@Override
	protected EMiningModel execute(MiningInputStream inputData, EMiningModel model) throws MiningException {
		HPAMiningModel modelA = (HPAMiningModel) model;
		ItemSets currItemsetList = null;
		if (modelA.getLargeItemSetsList().size() <= (modelA.getCurrentLargeItemSets())) {
			currItemsetList = new ItemSets();
			modelA.getLargeItemSetsList().add(currItemsetList);
		} else
			currItemsetList = modelA.getLargeItemSetsList().get(modelA.getCurrentLargeItemSets());
		Map<Integer, Map<List<String>, Integer>> currMap = modelA.getKItemSetsTransactionSubset().get(modelA.getCurrentLargeItemSets() + 1);
		if (currMap != null) { 
			Map<List<String>, Integer> map = modelA.getKItemSetsTransactionSubset().get(modelA.getCurrentLargeItemSets() + 1).get(modelA.getIdModel());
			if (map != null) {
				for (List<String> key : map.keySet()) {
					int sup = map.get(key);
					if ((sup / (double)modelA.getTransactionCount()) >= minSupport) {
						ItemSet itemSet = new ItemSet(key);
						itemSet.setSupportCount(sup);
						currItemsetList.add(itemSet);
					}
				}
			}
		}
		return modelA;
	}

}
