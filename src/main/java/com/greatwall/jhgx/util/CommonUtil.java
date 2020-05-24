package com.greatwall.jhgx.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class CommonUtil {

	public static StringBuilder buildSign(Hashtable<String, String> map) {
		TreeMap<String,String> params = new TreeMap<String,String>();
		Set keys = map.keySet();
		for (Iterator it = keys.iterator(); it.hasNext();) {
			String key = (String) it.next();
			params.put(key, map.get(key));
		}
		
		StringBuilder sb = new StringBuilder();
		Set keySet = params.keySet();
		for (Iterator it = keySet.iterator(); it.hasNext();) {
			String key = (String) it.next();
			sb.append(key);
			sb.append("=");
			sb.append(params.get(key));
			sb.append("&");
		}
		System.out.println("params=" + sb.toString());
		return sb;
	}
}
