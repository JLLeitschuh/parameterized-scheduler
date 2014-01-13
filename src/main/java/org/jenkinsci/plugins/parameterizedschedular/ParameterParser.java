package org.jenkinsci.plugins.parameterizedschedular;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

public class ParameterParser {
	/**
	 * if ever changed, documentation and messages will need to be updated as well
	 */
	private static final String PARAMETER_SEPARATOR = "%";
	private static final String NAME_VALUE_SEPARATOR = "=";
	private static final String PAIR_SEPARATOR = ";";

	/**
	 * 
	 * @param nameValuePairFormattedString of name=value;other=value name value pairs
	 * @return
	 */
	public Map<String, String> parse(String nameValuePairFormattedString) {
		if (StringUtils.isBlank(nameValuePairFormattedString)) {
			return Maps.<String, String> newHashMap();
		}
		String clean = nameValuePairFormattedString.trim();
		if (nameValuePairFormattedString.endsWith(PAIR_SEPARATOR)) {
			//the default splitter message in this scenario is not user friendly, so snip a trailing semicolon
			clean = clean.substring(0, clean.length() - 1);
		}
		return Splitter.on(PAIR_SEPARATOR).withKeyValueSeparator(NAME_VALUE_SEPARATOR).split(clean);
	}

	public String checkSanity(String cronTabSpec) {
		String[] split = cronTabSpec.split(PARAMETER_SEPARATOR);
		if (split.length < 2) {
			return null;
		}
		if (split.length > 2) {
			return Messages.ParameterizedTimerTrigger_MoreThanOnePercent();
		}

		try {
			parse(split[1]);
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
		return null;
	}
}
