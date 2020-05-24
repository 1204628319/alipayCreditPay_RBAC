package com.greatwall.jhgx.util;

import java.util.*;

public class SignUtil {

    public static String sortAndSerialize(Map<String,String> params, String[] excludeKeys,String md5key){
        Set<String> keys = params.keySet();
        List<String> keyList = new ArrayList<>(keys);
        Collections.sort(keyList);
        StringBuilder sb = new StringBuilder();
        List<String> excludeKeyList = excludeKeys==null?new ArrayList<>()
                :Arrays.asList(excludeKeys);
        for (int i=0; i<keyList.size(); i++){
            String key = keyList.get(i);
            if (excludeKeyList.contains(key))
                continue;
            sb.append(key).append("=")
                    .append(params.get(key))
                    .append(i<keyList.size()-1?"&":"");
        }
        sb.append(md5key);
        return sb.toString();
    }

    public static String sortAndSerialize(Map<String,String> params,String key){
        return sortAndSerialize(params,null,key);
    }
}
