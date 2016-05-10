package org.eltech.ddm.associationrules.apriori.hpa.steps;

import org.eltech.ddm.associationrules.ItemSet;
import org.eltech.ddm.associationrules.ItemSets;
import org.eltech.ddm.associationrules.apriori.hpa.HPAMiningModel;
import org.eltech.ddm.associationrules.apriori.steps.CreateKItemSetCandidateStep;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.miningfunctionsettings.EMiningFunctionSettings;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class HPACreateKItemSetCandidateStep extends CreateKItemSetCandidateStep  {
	final protected int numHandlers;

	public HPACreateKItemSetCandidateStep(EMiningFunctionSettings settings) throws MiningException {
		super(settings);
		numHandlers = settings.getAlgorithmSettings().getNumberHandlers();
	}

	@Override
	protected EMiningModel execute(MiningInputStream inputData, EMiningModel model) throws MiningException {
		
		HPAMiningModel modelA = (HPAMiningModel) model;
		ItemSets prevItemsetList = modelA.getLargeItemSetsList().get(modelA.getCurrentLargeItemSets() - 1);
		ItemSets currItemsetList = null;
		if (modelA.getLargeItemSetsList().size() <= (modelA.getCurrentLargeItemSets())) {
			currItemsetList = new ItemSets();
			modelA.getLargeItemSetsList().add(currItemsetList);
		} else
			currItemsetList = modelA.getLargeItemSetsList().get(modelA.getCurrentLargeItemSets());

		ItemSet itemSet = prevItemsetList.get(modelA.getCurrentItemSet());
		ItemSet itemSet2 = prevItemsetList.get(modelA.getCurrentItemSet2());

//		 System.out.println("Thread: " + Thread.currentThread().getName() + 
//				 "itemSet=" + itemSet + " itemSet2=" + itemSet2);

		ItemSet newItemSet = union(itemSet, itemSet2);
		modelA.setCurrentCandidate(-1);
		if ((newItemSet.getItemIDList().size() == itemSet.getItemIDList().size() + 1)
				&& (HPAMiningModel.calculateHash(newItemSet.getItemIDList(), numHandlers)) == modelA.getIdModel()) {
			if (!currItemsetList.contains(newItemSet))
				currItemsetList.add(newItemSet);
			int currentCandidate = currItemsetList.indexOf(newItemSet);
			modelA.setCurrentCandidate(currentCandidate);
		}

		return modelA;
	}
}
