/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package jp.ac.osaka_u.ist.padla;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LearningData {
	List<double[]> learningData = new ArrayList<double[]>();
	double ep = 0;
	private boolean exitFlag = false;
	FileWriter file = null;
	static boolean ISDEBUG = false;

	private final String messageHead = "[LOG4JCORE-EXTENDED]:";

	public LearningData(String filename, double EP, int numOfMethods,boolean isDebug) throws FileNotFoundException {
		ep = EP;
		ISDEBUG = isDebug;
		//forExperiment
		try {
			file = new FileWriter("e:\\log4j2\\logs\\output.log");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//
		if(filename != null) {
			File learningDataFile = new File(filename);
			BufferedReader learningDataBr = new BufferedReader(new FileReader(learningDataFile));
			for (;;) {
				String text = null;
				try {
					text = learningDataBr.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (text == null)
					break;
				String textWithoutBracket = text.substring(1, text.length() - 1);
				String[] exeTimeVectorString = textWithoutBracket.split(",", 0);
				double[] exeTimeVector = new double[exeTimeVectorString.length];
				for (int i = 0; i < exeTimeVector.length; i++) {
					exeTimeVector[i] = Double.parseDouble(exeTimeVectorString[i]);
				}
				if(exeTimeVector.length == numOfMethods) {
					learningData.add(exeTimeVector);
				}else {
						System.out.println(messageHead + "ERROR Invalid Length of Learning Data");
					exitFlag = true;
					break;
				}
			}
			try {
				learningDataBr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(ISDEBUG) {
				System.out.println(messageHead + "Learning data size:" + learningData.size());
			}
		}
	}


	public boolean isInvalidLearningData() {
		return exitFlag;
	}

	/**
	 * It adds vec to learningdata
	 * @param vec
	 */
	public void add(double[] vec) {
		learningData.add(vec);
	}

	/**
	 * It compare vec with learningData and return false if the similarity is above threshold, otherwise return true
	 * If vec and learningData are both zero vector, the similarity is 1
	 * @param vec
	 * @param numOfMethods
	 * @return
	 */
	public boolean isUnknownPhase(double[] vec, int numOfMethods) {
		double maxSimilarity = 0;
		double innerProduct = 0;
		double[] max = null;
		int phaseNum = 0;

		for (int i = 0; i < learningData.size(); i++) {
			innerProduct = calcInnerProduct(vec, learningData.get(i), numOfMethods);
			if(innerProduct == 0.0) {
				double sum = 0.0;
				for(double v : vec) {
					sum += v;
				}
				for(int j = 0; j < vec.length; j++) {
					sum += learningData.get(i)[j];
				}
				if(sum == 0.0) innerProduct = 1;
			}
			if (innerProduct > maxSimilarity) {
				maxSimilarity = innerProduct;
				max = learningData.get(i);
				phaseNum = i;
			}
		}
		if(maxSimilarity > ep) {
	        //forExperiment
			try {
				file.write(messageHead + "Sim: " + maxSimilarity + "  phaseNum: " + phaseNum +" <Known phase>\n" );
				file.flush();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			//
			return false;
		}
        //forExperiment
		try {
			file.write(messageHead + "Sim: " + maxSimilarity + "  phaseNum: " + phaseNum +" <Unknown phase>\n" );
			file.flush();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		//
		return true;
	}


	/**
	 * It returns inner product of two vectors(array1 and array2)
	 * @param array1
	 * @param array2
	 * @param numOfMethods
	 * @return
	 */
	private double calcInnerProduct(double[] array1, double[] array2, int numOfMethods) {
		double innerProduct = 0;

		for (int i = 0; i < numOfMethods; i++) {
			innerProduct += array1[i] * array2[i];
		}

		return innerProduct;
	}

}
