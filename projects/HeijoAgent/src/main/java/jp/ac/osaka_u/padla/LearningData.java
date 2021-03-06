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

package jp.ac.osaka_u.padla;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LearningData {
	private List<double[]> learningData = new ArrayList<double[]>();
	private boolean exitFlag = false;
	static String MODE = null;

	static DebugMessage debugmessage = null;

	static CalcVectors calcvectors = null;
	static MethodVector vecofaninterval = null;

	public LearningData(Options options,int numOfMethods,String mode) {
		MODE = mode;

		debugmessage = new DebugMessage();
		debugmessage.setISDEBUG(options.isDebug());

		if(MODE.equals("Adapter") && options.getLearningData() != null) {
			loadLearningDataFile(options.getLearningData(), numOfMethods);
		}
	}



	public List<double[]> getLearningData() {
		return learningData;
	}



	/**
	 * Load Learning Data from "filename" to "List<double[]> learningData"
	 * @param filename
	 * @param numOfMethods
	 * @throws FileNotFoundException
	 */
	private void loadLearningDataFile(String filename, int numOfMethods) {
		File learningDataFile = new File(filename);
		BufferedReader learningDataBr = null;
		try {
			learningDataBr = new BufferedReader(new FileReader(learningDataFile));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
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
				debugmessage.print("ERROR Invalid Length of Learning Data");
				exitFlag = true;
				break;
			}
		}
		try {
			learningDataBr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		debugmessage.printOnDebug("Learning data size:" + learningData.size());
	}



	public boolean isInvalidLearningData() {
		return exitFlag;
	}
}
