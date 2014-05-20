package reasoning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RuleFileMapperReader {
	private static final String FILE_PATH = "/var/lib/one/workspace/CloudScheduler/src/reasoning/rules";

	/*
	 * each line has the form: RULE_NAME WEIGHT PARAM1 PARAM2 ...
	 */
	public static List<RuleFileMapper> readFromFile() {

		BufferedReader br = null;
		List<RuleFileMapper> rules = new ArrayList<RuleFileMapper>();
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(FILE_PATH));

			while ((sCurrentLine = br.readLine()) != null) {

				String[] values = sCurrentLine.split(" ");
				RuleFileMapper rule = new RuleFileMapper();
				if (RuleName.SERVER_POLICY.toString().equals(values[0])) {
					rule.setName(RuleName.SERVER_POLICY);
				} else if (RuleName.VM_POLICY.toString().equals(values[0])) {
					rule.setName(RuleName.VM_POLICY);
				} else if (RuleName.RACK_POLICY.toString().equals(values[0])) {
					rule.setName(RuleName.RACK_POLICY);
				}
				try {
					rule.setWeight(Float.parseFloat(values[1]));
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}
				List<String> params = new ArrayList<String>();
				for (int i = 2; i < values.length; i++) {
					params.add(values[i]);
				}
				rule.setParams(params);
				rules.add(rule);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return rules;
	}

	public static void main(String args[]) {
		List<RuleFileMapper> rules = readFromFile();
		for (RuleFileMapper r : rules) {
			System.out.println(r.toString());
		}
	}
}
