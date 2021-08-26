package com.shawntime.common.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import org.apache.commons.collections4.CollectionUtils;

public final class ListSortUtils {

    private ListSortUtils() {
        super();
    }

    public static <T> List<T> sortByList(List<Integer> sortFieldList,
                                         List<T> dataList,
                                         Function<T, Integer> function) {
        if (CollectionUtils.isEmpty(sortFieldList) || CollectionUtils.isEmpty(dataList)) {
            return dataList;
        }
        int maxSize = sortFieldList.size() + dataList.size() + 1;
        Map<Integer, Integer> sortIndexMap = new HashMap<>();
        for (int sortField : sortFieldList) {
            sortIndexMap.put(sortField, maxSize--);
        }
        Map<Integer, T> resultMap = new TreeMap<>(Comparator.reverseOrder());
        for (T data : dataList) {
            Integer fieldName = function.apply(data);
            int index = sortIndexMap.getOrDefault(fieldName, 0);
            if (index > 0) {
                resultMap.put(index, data);
            } else {
                resultMap.put(maxSize--, data);
            }
        }
        return resultMap.values().stream().collect(Collectors.toList());
    }

    public static <T> List<T> sortByMultiList(List<Integer> sortFieldList,
                                              List<T> dataList,
                                              Function<T, Integer> function) {
        if (CollectionUtils.isEmpty(sortFieldList) || CollectionUtils.isEmpty(dataList)) {
            return dataList;
        }
        int maxSize = sortFieldList.size() + dataList.size() + 1;
        Map<Integer, Integer> sortIndexMap = new HashMap<>();
        for (int sortField : sortFieldList) {
            sortIndexMap.put(sortField, maxSize--);
        }
        Multimap<Integer, T> resultMap = TreeMultimap.create(Ordering.natural().reversed(), Ordering.usingToString());
        for (T data : dataList) {
            Integer fieldName = function.apply(data);
            int index = sortIndexMap.getOrDefault(fieldName, 0);
            if (index > 0) {
                resultMap.put(index, data);
            } else {
                resultMap.put(maxSize--, data);
            }
        }
        return resultMap.values().stream().collect(Collectors.toList());
    }

    public static <T> void swap(List<T> list, int index1, int index2) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        if (index1 >= list.size() || index2 >= list.size()) {
            return;
        }
        T data = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, data);
    }

    public static <T> void move(List<T> list, T data, int index) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        boolean isRemove = list.remove(data);
        if (!isRemove) {
            return;
        }
        list.add(index, data);
    }

    /**
     * 集合按照groupNum个数，集合内的function值尽量不重复
     */
    public static <T> List<T> groupDistinctSortList(int groupNum,
                                                    List<T> dataList,
                                                    Function<T, Integer> function) {
        List<T> sortList = new ArrayList<>();
        while (true) {
            List<T> groupList = new ArrayList<>();
            Set<Integer> groupIds = new HashSet<>();
            for (T data : dataList) {
                if (groupList.size() >= groupNum) {
                    break;
                }
                Integer groupId = function.apply(data);
                if (groupIds.contains(groupId)) {
                    continue;
                }
                groupList.add(data);
                groupIds.add(groupId);
            }
            if (groupList.size() < groupNum) {
                break;
            }
            sortList.addAll(groupList);
            dataList.removeAll(groupList);
        }
        sortList.addAll(dataList);
        return sortList;
    }

}
