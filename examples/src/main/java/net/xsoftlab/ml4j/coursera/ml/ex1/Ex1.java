package net.xsoftlab.ml4j.coursera.ml.ex1;

import java.io.IOException;

import net.xsoftlab.ml4j.minfunc.GradientDescent;
import net.xsoftlab.ml4j.minfunc.MinFunc;
import net.xsoftlab.ml4j.model.BaseModel;
import net.xsoftlab.ml4j.model.supervised.LinearRegression;
import net.xsoftlab.ml4j.util.MathUtil;
import net.xsoftlab.ml4j.util.MatrixUtil;
import net.xsoftlab.ml4j.util.TestUtil;

import org.jblas.FloatMatrix;

public class Ex1 extends TestUtil {

	public static void main(String[] args) throws IOException {

		tic();

		logger.info("加载数据...\n");

		String path = RESOURCES_PATH + "/coursera/ml/ex1/ex1data1.txt";
		FloatMatrix[] matrixs = MatrixUtil.loadDataWithXY(path, ",", true);

		logger.info("模型初始化...\n");
		BaseModel model = new LinearRegression(matrixs[0], matrixs[1]);

		logger.info("执行训练...\n");
		// MinFunc minFunc = new BFGS(model);
		MinFunc minFunc = new GradientDescent(model, 0.024f, 1500);
		FloatMatrix theta = minFunc.compute();

		logger.info("计算均方差...\n");
		float rms = MathUtil.std(matrixs[0].mmul(theta), matrixs[1]);

		logger.info("训练完成.\n \t theta = {} \n\t RMS = {}\n", new Object[] { theta, rms });

		toc();
	}
}