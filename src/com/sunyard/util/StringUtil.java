package com.sunyard.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class StringUtil {

	/**
	 * 将STRING转换成INT
	 * 
	 * @param s
	 * @return
	 */
	public static int string2Int(String s) {
		if (s == null || "".equals(s) || "undefined".equals(s))
			return 0;
		int result = 0;
		try {
			if (s.indexOf(",") != -1) {
				s = s.substring(0, s.indexOf(","));
			} else if (s.indexOf(".") != -1) {
				s = s.substring(0, s.indexOf("."));
			} else if (s.indexOf("|") != -1) {
				s = s.substring(0, s.indexOf("|"));
			}
			result = Integer.parseInt(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 禁止显示出NULL值
	 * 
	 * @param str
	 *            输入字串
	 * @return 输出字串
	 */
	public static String forbidNull(Object obj) {
		return (null == obj) ? "" : String.valueOf(obj).trim();
	}

	/**
	 * <li>判断字串是否为空值</li> <li>NULL、空格均为空值</li>
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		return null == value || value.length() == 0;
	}

	
	/**
	 * 重复字串 如 repeatString("a",3) ==> "aaa"
	 * 
	 * @param src
	 * @param repeats
	 * @return
	 */
	public static String repeatString(String src, int repeats) {
		if (null == src || repeats <= 0) {
			return src;
		} else {
			StringBuffer bf = new StringBuffer();
			for (int i = 0; i < repeats; i++) {
				bf.append(src);
			}
			return bf.toString();
		}
	}

	/**
	 * 左对齐字串 * lpadString("X",3); ==>" X" *
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static String lpadString(String src, int length) {
		return lpadString(src, length, " ");
	}

	/**
	 * 以指定字串填补空位，左对齐字串 * lpadString("X",3,"0"); ==>"00X"
	 * 
	 * @param src
	 * @param byteLength
	 * @param temp
	 * @return
	 */
	public static String lpadString(String src, int length, String single) {
		if (src == null || length <= src.getBytes().length) {
			return src;
		} else {
			return repeatString(single, length - src.getBytes().length) + src;
		}
	}

	/**
	 * 右对齐字串 * rpadString("9",3)==>"9 "
	 * 
	 * @param src
	 * @param byteLength
	 * @return
	 */
	public static String rpadString(String src, int byteLength) {
		return rpadString(src, byteLength, " ");
	}

	/**
	 * 以指定字串填补空位，右对齐字串 rpadString("9",3,"0")==>"900"
	 * 
	 * @param src
	 * @param byteLength
	 * @param single
	 * @return
	 */
	public static String rpadString(String src, int length, String single) {
		if (src == null || length <= src.getBytes().length) {
			return src;
		} else {
			return src + repeatString(single, length - src.getBytes().length);
		}
	}

	/**
	 * 去除,分隔符，用於金额值去格式化
	 * 
	 * @param value
	 * @return
	 */
	public static String decimal(String value) {
		if (null == value || "".equals(value.trim())) {
			return "0";
		} else {
			return value.replaceAll(",", "");
		}
	}

	/**
	 * 在数组中查找字串
	 * 
	 * @param params
	 * @param name
	 * @param ignoreCase
	 * @return
	 */
	public static int indexOf(String[] params, String name, boolean ignoreCase) {
		if (params == null)
			return -1;
		for (int i = 0, j = params.length; i < j; i++) {
			if (ignoreCase && params[i].equalsIgnoreCase(name)) {
				return i;
			} else if (params[i].equals(name)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 将字符串转数组
	 * 
	 * @param str
	 * @return
	 */
	public static String[] toArr(String str) {
		return toArr(str, ",");
	}

	public static String[] toArr(String str, String delim) {
		String inStr = str;
		String a[] = null;
		try {
			if (null != inStr) {
				StringTokenizer st = new StringTokenizer(inStr, delim);
				if (st.countTokens() > 0) {
					a = new String[st.countTokens()];
					int i = 0;
					while (st.hasMoreTokens()) {
						a[i++] = st.nextToken();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}
	
	public static List<String> toList(String str){
		return Arrays.asList(toArr(str));
	}
	/**
	 * 字串数组包装成字串
	 * 
	 * @param ary
	 * @param s
	 *            包装符号如 ' 或 "
	 * @return
	 */
	public static String toStr(String[] ary, String s) {
		if (ary == null || ary.length < 1)
			return "";
		StringBuffer bf = new StringBuffer();
		bf.append(s);
		bf.append(ary[0]);
		for (int i = 1; i < ary.length; i++) {
			bf.append(s).append(",").append(s);
			bf.append(ary[i]);
		}
		bf.append(s);
		return bf.toString();
	}
	
	/**
	 * 设定MESSAGE中的变{0}...{N}
	 * 
	 * @param msg
	 * @param vars
	 * @return
	 */
	public static String getMessage(String msg, String[] vars) {
		for (int i = 0; i < vars.length; i++) {
			msg = msg.replaceAll("\\{" + i + "\\}", vars[i]);
		}
		return msg;
	}

	/**
	 * @param msg
	 * @param var
	 * @return
	 */
	public static String getMessage(String msg, String var) {
		return getMessage(msg, new String[] { var });
	}

	/**
	 * @param msg
	 * @param var
	 * @param var2
	 * @return
	 */
	public static String getMessage(String msg, String var, String var2) {
		return getMessage(msg, new String[] { var, var2 });
	}

	/**
	 * 从Map中取String类型值
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getMapValue(Map map, Object key) {
		if (null == map || null == key)
			return "";
		Object value = map.get(key);
		return null == value ? "" : value;
	}

	/**
	 * 从Map中取Integer类型值
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getMapIntValue(Map map, Object key) {
		if (null == map || null == key)
			return new Integer(0);
		Object value = map.get(key);
		return null == value ? new Integer(0) : value;
	}

	/**
	 * 将key=value;key2=value2……转换成Map
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map gerneryParams(String params) {
		Map args = new HashMap();
		if (!isEmpty(params)) {
			try {
				String map, key, value;
				StringTokenizer st = new StringTokenizer(params, ";");
				StringTokenizer stMap;
				while (st.hasMoreTokens()) {
					map = st.nextToken();
					if (isEmpty(map.trim()))
						break;
					stMap = new StringTokenizer(map, "=");
					key = stMap.hasMoreTokens() ? stMap.nextToken() : "";
					if (isEmpty(key.trim()))
						continue;
					value = stMap.hasMoreTokens() ? stMap.nextToken() : "";
					args.put(key, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return args;
	}

		
   /**
    * 去掉字符串空格
    * @param str
    * @return
    */
	public static String delSpace(String str) {

		if (str == null) {
			return null;
		}

		// 先将半角空格删除
		str = str.trim();

		while (str.startsWith("　")) {

			// 只可惜String中没有提供replaceLast(), 否则就简单点了
			// 所以本循环完成以后，只能将字符串前端的空格删除，却不能删除后端的空格
			// 故而本循环完成后，又将字符串翻转后再去一次空格
			str = str.replaceFirst("　", "");

			// 一定要 trim()， 不然的话，如果前端的空格是全角和半角相间的话，就搞不定了
			str = str.trim();
		}

		// 将字符串翻转
		str = reverse(str);

		// 再去一次空格
		while (str.startsWith("　")) {

			str = str.replaceFirst("　", "");

			str = str.trim();
		}

		// 最后再将字符串翻转回去
		return str = reverse(str).trim();
	}

	// 字符串翻转方法。
	public static String reverse(String str) {
		char[] charsOld = str.toCharArray();

		char[] charsNew = new char[charsOld.length];

		int index = charsOld.length - 1;

		for (int i = 0; i < charsOld.length; i++) {

			charsNew[i] = charsOld[index - i];
		}

		return String.valueOf(charsNew);
	}

	public static String toCron(Date date){
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int ss = calendar.get(Calendar.SECOND);
		int mm = calendar.get(Calendar.MINUTE);
		int hh = calendar.get(Calendar.HOUR_OF_DAY);
		int dd = calendar.get(Calendar.DAY_OF_MONTH);
		int MM = calendar.get(Calendar.MONTH)+1;
		int yyyy = calendar.get(Calendar.YEAR);
		return ""+ss+" "+mm+" "+hh+" "+dd+" "+MM+" ? "+yyyy;
	}
}
