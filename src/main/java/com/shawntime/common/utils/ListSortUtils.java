package com.shawntime.common.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

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

}
