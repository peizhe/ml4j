package net.xsoftlab.ml4j.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jblas.FloatMatrix;

/**
 * 数据加载器
 * 
 * @author 王彦超
 *
 */
public class DataLoader {

	/**
	 * 将字符串数组转化为float数组
	 * 
	 * @param data
	 *            字符串数组
	 * @param intercept
	 *            是否添加截距项
	 * 
	 * @return float数组
	 */
	private static float[] convert(String[] data, boolean intercept) {

		int length = intercept ? data.length + 1 : data.length;
		float[] result = new float[length];

		for (int i = 0; i < length; i++) {
			if (intercept) {
				if (i == 0) {
					result[i] = 1;
				} else {
					result[i] = Float.parseFloat(data[i - 1]);
				}
			} else
				result[i] = Float.parseFloat(data[i]);
		}

		return result;
	}

	/**
	 * 从文件中加载数据
	 * 
	 * @param filePath
	 *            文件路径
	 * @param split
	 *            分隔符
	 * @param intercept
	 *            是否添加截距项
	 * 
	 * @return 数据矩阵
	 */
	public static FloatMatrix loadData(InputStream filePath, String split, boolean intercept) throws IOException {

		String line;
		String[] data;
		FloatMatrix matrix;
		int numColumns = -1;
		List<float[]> list = new ArrayList<float[]>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(filePath));

		while ((line = reader.readLine()) != null) {

			data = line.trim().split(split);
			if (numColumns < 0)
				numColumns = data.length;
			else
				assert data.length == numColumns : "数据前后大小不一致！";

			list.add(convert(data, intercept));
		}

		matrix = new FloatMatrix(list.size(), numColumns);
		for (int i = 0; i < list.size(); i++)
			matrix.putRow(i, new FloatMatrix(list.get(i)));

		return matrix;
	}

	/**
	 * 从文件中加载数据
	 * 
	 * @param filePath
	 *            文件路径
	 * @param split
	 *            分隔符
	 * @param intercept
	 *            是否添加截距项
	 * 
	 * @return 数据矩阵
	 */
	public static FloatMatrix loadData(String filePath, String split, boolean intercept) throws IOException {

		return loadData(new FileInputStream(filePath), split, intercept);
	}

	/**
	 * 将字符串数组转化为float数组
	 * 
	 * @param data
	 *            字符串数组
	 * @param intercept
	 *            是否添加截距项
	 * 
	 * @return float数组集合
	 */
	private static List<float[]> convertWithXY(String[] data, boolean intercept) {

		List<float[]> list = new ArrayList<float[]>();
		int length = intercept ? data.length + 1 : data.length;

		float[] result0 = new float[length - 1];
		float[] result1 = new float[1];

		for (int i = 0; i < length; i++) {
			if (intercept) {
				if (i == 0)
					result0[i] = 1;
				else if (i != length - 1)
					result0[i] = Float.parseFloat(data[i - 1]);
				else
					result1[0] = Float.parseFloat(data[i - 1]);
			} else {
				if (i != length - 1)
					result0[i] = Float.parseFloat(data[i]);
				else
					result1[0] = Float.parseFloat(data[i]);
			}
		}

		list.add(result0);
		list.add(result1);

		return list;
	}

	/**
	 * 从文件中加载包含X、Y的数据
	 * 
	 * @param filePath
	 *            文件路径
	 * @param split
	 *            分隔符
	 * @param intercept
	 *            是否添加截距项
	 * 
	 * @return 数据矩阵数组
	 */
	public static FloatMatrix[] loadDataWithXY(InputStream filePath, String split, boolean intercept)
			throws IOException {

		String line;
		String[] data;
		FloatMatrix[] matrix = new FloatMatrix[2];
		int numColumns = -1;
		List<float[]> list;
		List<float[]> resList;
		Map<String, List<float[]>> map = new HashMap<String, List<float[]>>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(filePath));

		while ((line = reader.readLine()) != null) {

			data = line.trim().split(split);
			if (numColumns < 0)
				numColumns = data.length;
			else
				assert data.length == numColumns : "数据前后大小不一致！";

			resList = convertWithXY(data, intercept);
			if (map.containsKey("0")) {
				list = map.get("0");
				list.add(resList.get(0));
				map.put("0", list);
			} else {
				list = new ArrayList<float[]>();
				list.add(resList.get(0));
				map.put("0", list);
			}

			if (map.containsKey("1")) {
				list = map.get("1");
				list.add(resList.get(1));
				map.put("1", list);
			} else {
				list = new ArrayList<float[]>();
				list.add(resList.get(1));
				map.put("1", list);
			}
		}

		List<float[]> map0 = map.get("0");
		matrix[0] = new FloatMatrix(map0.size(), numColumns);
		for (int i = 0; i < map0.size(); i++)
			matrix[0].putRow(i, new FloatMatrix(map0.get(i)));

		List<float[]> map1 = map.get("1");
		matrix[1] = new FloatMatrix(map1.size(), 1);
		for (int i = 0; i < map1.size(); i++)
			matrix[1].putRow(i, new FloatMatrix(map1.get(i)));

		return matrix;
	}

	/**
	 * 从文件中加载包含X、Y的数据
	 * 
	 * @param filePath
	 *            文件路径
	 * @param split
	 *            分隔符
	 * @param intercept
	 *            是否添加截距项
	 * 
	 * @return 数据矩阵数组
	 */
	public static FloatMatrix[] loadDataWithXY(String filePath, String split, boolean intercept) throws IOException {

		return loadDataWithXY(new FileInputStream(filePath), split, intercept);
	}
}