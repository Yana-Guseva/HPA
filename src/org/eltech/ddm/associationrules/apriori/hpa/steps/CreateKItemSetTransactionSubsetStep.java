package org.eltech.ddm.associationrules.apriori.hpa.steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eltech.ddm.associationrules.ItemSet;
import org.eltech.ddm.associationrules.Transaction;
import org.eltech.ddm.associationrules.TransactionList;
import org.eltech.ddm.associationrules.apriori.hpa.HPAMiningModel;
import org.eltech.ddm.inputdata.MiningInputStream;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.algorithms.Step;
import org.eltech.ddm.miningcore.miningfunctionsettings.EMiningFunctionSettings;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class CreateKItemSetTransactionSubsetStep extends Step {
	final protected int numHandlers;

	public CreateKItemSetTransactionSubsetStep(EMiningFunctionSettings settings) throws MiningException {
		super(settings);
		numHandlers = settings.getAlgorithmSettings().getNumberHandlers();
	}

	@Override
	protected EMiningModel execute(MiningInputStream inputData, EMiningModel model) throws MiningException {
		HPAMiningModel modelA = (HPAMiningModel) model;
		TransactionList transactionList = modelA.getTransactionList();
		Transaction transaction = transactionList.get(modelA.getCurrentTransaction());
//		System.out.println("thread " + Thread.currentThread().getName() + " " + transaction);;
		int k = modelA.getCurrentLargeItemSets() + 2;
		Map<Integer, List<ItemSet>> kItemSetSubset = createSubsets(transaction, k);
		
		if (kItemSetSubset != null) {
			if (!modelA.getKItemSetsTransactionSubset().containsKey(k)) {
				modelA.getKItemSetsTransactionSubset().put(k, new HashMap<Integer, Map<List<String>, Integer>>());
			}
			Map<Integer, Map<List<String>, Integer>> map = modelA.getKItemSetsTransactionSubset().get(k);
			merge(map, kItemSetSubset);

		}
		return modelA;
	}

	private void merge(Map<Integer, Map<List<String>, Integer>> map, Map<Integer, List<ItemSet>> kItemSetSubset) {

		for (Integer id : kItemSetSubset.keySet()) {
			List<ItemSet> itemSetList = kItemSetSubset.get(id);
			Map<List<String>, Integer> inItemSetList = map.get(id);
			if (inItemSetList == null) {
				inItemSetList = new HashMap<List<String>, Integer>();
				map.put(id, inItemSetList);
			}
			for (ItemSet itemSet : itemSetList) {
				List<String> key = itemSet.getItemIDList();
				boolean isContains = inItemSetList.containsKey(key);
				if (isContains) {
					Integer count = inItemSetList.get(key) + 1;
					inItemSetList.put(key, count);
				} else {
					inItemSetList.put(key, 1);
				}
			}
		}
	}

	protected Map<Integer, List<ItemSet>> createSubsets(Transaction transaction, int k) {
		Map<Integer, List<ItemSet>> map = new HashMap<>();
		List<String> transactionItemIDList = transaction.getItemIDList();
		if (transactionItemIDList.size() < k) {
			return null;
		}

		int indexes[] = new int[k];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = i;
		}

		boolean flag = true;
		while (flag) {
			List<String> elements = new ArrayList<String>();
			for (int i : indexes) {
				elements.add(transactionItemIDList.get(i));
			}
			elements.sort(null);
			Integer handlerID = HPAMiningModel.calculateHash(elements, numHandlers);
			List<ItemSet> itemSetList = map.get(handlerID);
			if (itemSetList == null) {
				itemSetList = new ArrayList<>();
				map.put(handlerID, itemSetList);
			}
//			System.out.println("thread " + handlerID + " add " + elements);
			itemSetList.add(new ItemSet(elements));

			int n = 1;
			while (flag) {
				indexes[indexes.length - n]++;
				if (indexes[indexes.length - n] < transactionItemIDList.size() - n + 1) {
					for (int h = indexes.length - n + 1; h < indexes.length; h++) {
						indexes[h] = indexes[h - 1] + 1;
					}
					n = 1;
					break;
				} else {
					n++;
					if (n == k + 1) {
						flag = false;
					}
				}
			}
		}
		return map;

	}
}
