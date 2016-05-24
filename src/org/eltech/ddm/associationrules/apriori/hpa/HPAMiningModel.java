package org.eltech.ddm.associationrules.apriori.hpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import org.eltech.ddm.associationrules.AssociationRulesFunctionSettings;
import org.eltech.ddm.associationrules.ItemSet;
import org.eltech.ddm.associationrules.ItemSets;
import org.eltech.ddm.associationrules.apriori.AprioriMiningModel;
import org.eltech.ddm.miningcore.MiningException;
import org.eltech.ddm.miningcore.miningmodel.EMiningModel;

public class HPAMiningModel extends AprioriMiningModel {

	protected Map<Integer, Map<Integer, Map<List<String>, Integer>>> kItemSetsTransactionSubset = new HashMap<>();

	private int idModel;

	public Map<Integer, Map<Integer, Map<List<String>, Integer>>> getKItemSetsTransactionSubset() {
		return kItemSetsTransactionSubset;
	}

	public void setkItemSetsTransactionSubset(Map<Integer, Map<Integer, Map<List<String>, Integer>>> kItemSetsTransactionSubset) {
		this.kItemSetsTransactionSubset = kItemSetsTransactionSubset;
	}

	public int getIdModel() {
		return idModel;
	}

	public void setIdModel(int idModel) {
		this.idModel = idModel;
	}

	public HPAMiningModel(AssociationRulesFunctionSettings settings) throws MiningException {
		super(settings);
	}

	public synchronized List<ItemSets> getLargeItemSetsList() {
		return largeItemSetsList;
	}
	
	@Override
	public void join(List<EMiningModel> joinModels) throws MiningException {
//		System.out.println("----------------------------------------------------------------------------");
		for (EMiningModel mm : joinModels) {
			if (mm == this) {
				continue;
			}
			
			HPAMiningModel hpamm = (HPAMiningModel) mm;
			int index = hpamm.getCurrentLargeItemSets() + 2;
			
			if (kItemSetsTransactionSubset.containsKey(index - 1)) {
				kItemSetsTransactionSubset.remove(index - 1);
			}
			hpamm.kItemSetsTransactionSubset.remove(index - 1);
			Map<Integer, Map<List<String>, Integer>> currentMap = hpamm.kItemSetsTransactionSubset.get(index);
//			System.out.println("hpamm " + hpamm.idModel + " " + currentMap);
			if (currentMap != null) {
				if (!kItemSetsTransactionSubset.containsKey(index)) {
					kItemSetsTransactionSubset.put(index, new HashMap<Integer, Map<List<String>, Integer>>());
				}
				for (Integer id : currentMap.keySet()) {
					if (!kItemSetsTransactionSubset.get(index).containsKey(id)) {
						kItemSetsTransactionSubset.get(index).put(id, new HashMap<List<String>, Integer>());
					}
					Map<List<String>, Integer> map = currentMap.get(id);
					Map<List<String>, Integer> commonMap = kItemSetsTransactionSubset.get(index).get(id);
					for (List<String> key : map.keySet()) {
						boolean isContains = commonMap.containsKey(key);
						if (isContains) {
							Integer count = commonMap.get(key) + map.get(key);
							commonMap.put(key, count);
//							System.out.println("put " +key + " " + count);
						} else {
							commonMap.put(key, map.get(key));
						}
					}
				}
//				System.out.println("after join " + getKItemSetsTransactionSubset());
			}

			List<ItemSets> curList = hpamm.getLargeItemSetsList();
			if (curList != null) {
				int k = curList.size();
				if(k >= largeItemSetsList.size())
					largeItemSetsList.add(new ItemSets());
				largeItemSetsList.get(k - 1).addAll(curList.get(k -1));
			}
			
		}
	}

	@Override
	public ArrayList<EMiningModel> split(int handlerCount) throws MiningException {
		ArrayList<EMiningModel> models = super.split(handlerCount);
		for (int i = 0; i < handlerCount; i++) {
			((HPAMiningModel) models.get(i)).setIdModel(i);
		}
//		System.out.println("0 " + ((AprioriMiningModel) models.get(0)).getTransactionList());
//		System.out.println("1 " + ((AprioriMiningModel) models.get(1)).getTransactionList());
		return models;
	}

	public Object clone() {
		HPAMiningModel o = null;
		o = (HPAMiningModel) super.clone();
		if (kItemSetsTransactionSubset != null) {
			o.kItemSetsTransactionSubset = new HashMap<Integer, Map<Integer, Map<List<String>, Integer>>>();
			for (Integer key : kItemSetsTransactionSubset.keySet()) {
				Map<Integer, Map<List<String>,Integer>> copiedMap = new HashMap<Integer, Map<List<String>, Integer>>();
				Map<Integer, Map<List<String>,Integer>> map = kItemSetsTransactionSubset.get(key);
				for (Integer inKey : map.keySet()) {
					Map<List<String>, Integer> inCopiedMap = new HashMap<List<String>, Integer>();	
					Map<List<String>, Integer> inMap = map.get(inKey);
					for (List<String> itemKey : inMap.keySet()) {
						inCopiedMap.put(new ArrayList<String>(itemKey), new Integer(inMap.get(itemKey)));
					}
					copiedMap.put(new Integer(inKey), inCopiedMap);
				}
				o.kItemSetsTransactionSubset.put(new Integer(key), copiedMap);
			}
		}
		o.idModel = idModel;
		return o;
	}
	
	public static Integer calculateHash(List<String> elements, int numHandlers) {
		return Math.abs(elements.hashCode() % numHandlers);
	}
}
