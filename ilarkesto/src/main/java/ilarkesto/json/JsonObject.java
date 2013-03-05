package ilarkesto.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonObject {

	public static void main(String[] args) {
		JsonObject json = new JsonObject();
		json.put("name", "Witek");
		json.put("gender", "male");

		json.putNewObject("subobject").put("a", "1");
		json.addToArray("array", "1");
		json.addToArray("array", "2");
		json.addToArray("array", new JsonObject());

		String s = json.toFormatedString();
		System.out.println(s);

		new JsonObject(s);
	}

	private final Map<String, Object> elements = new LinkedHashMap<String, Object>();
	private int idx = -1;
	private JsonObject parent;
	private File file;

	public JsonObject() {}

	private JsonObject(String json, int offset) {
		parse(json, offset);
	}

	public JsonObject(String json) {
		if (json != null) parse(json, 0);
	}

	public JsonObject(Map<?, ?> map) {
		for (Map.Entry entry : map.entrySet()) {
			String name = entry.getKey().toString();
			put(name, entry.getValue());
		}
	}

	public JsonObject(File file) {
		this(load(file));
		this.file = file;
	}

	public void save(boolean formated) {
		if (file == null) throw new IllegalStateException("file == null");
		write(file, formated);
	}

	public void save() {
		save(true);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof JsonObject)) return false;
		return elements.equals(((JsonObject) obj).elements);
	}

	@Override
	public int hashCode() {
		return elements.hashCode();
	}

	// --- inspecting ---

	public Set<String> getProperties() {
		return elements.keySet();
	}

	public Object get(String name) {
		return elements.get(name);
	}

	public boolean contains(String name) {
		return elements.containsKey(name);
	}

	public boolean isSet(String name) {
		if (!elements.containsKey(name)) return false;
		return get(name) != null;
	}

	public boolean containsString(String name, String expected) {
		String value = getString(name);
		return value == expected || (value != null && value.equals(expected));
	}

	public String getString(String name) {
		return (String) get(name);
	}

	public String getString(String name, String defaultValue) {
		String value = getString(name);
		return value == null ? defaultValue : value;
	}

	public JsonObject getObject(String name) {
		return (JsonObject) get(name);
	}

	public List getArray(String name) {
		return (List) get(name);
	}

	public Number getNumber(String name) {
		return (Number) get(name);
	}

	public Integer getInteger(String name) {
		Number value = getNumber(name);
		return toInteger(value);
	}

	private Integer toInteger(Number value) {
		if (value == null) return null;
		if (value instanceof Integer) return (Integer) value;
		return value.intValue();
	}

	public Long getLong(String name) {
		Number value = getNumber(name);
		if (value == null) return null;
		if (value instanceof Long) return (Long) value;
		return value.longValue();
	}

	public Double getDouble(String name) {
		Number value = getNumber(name);
		if (value == null) return null;
		if (value instanceof Double) return (Double) value;
		return value.doubleValue();
	}

	public Float getFloat(String name) {
		Number value = getNumber(name);
		if (value == null) return null;
		if (value instanceof Float) return (Float) value;
		return value.floatValue();
	}

	public Byte getByte(String name) {
		Number value = getNumber(name);
		if (value == null) return null;
		if (value instanceof Byte) return (Byte) value;
		return value.byteValue();
	}

	public Boolean getBoolean(String name) {
		return (Boolean) get(name);
	}

	public boolean isTrue(String name) {
		Boolean value = getBoolean(name);
		if (value == null) return false;
		return value.booleanValue();
	}

	public List<String> getArrayOfStrings(String name) {
		return (List<String>) get(name);
	}

	public List<JsonObject> getArrayOfObjects(String name) {
		return (List<JsonObject>) get(name);
	}

	public List<Integer> getArrayOfIntegers(String name) {
		List<Number> values = (List<Number>) get(name);
		List<Integer> ret = new ArrayList<Integer>(values.size());
		for (Number value : values) {
			ret.add(toInteger(value));
		}
		return ret;
	}

	public JsonObject getParent() {
		return parent;
	}

	// --- manipulating ---

	public <V> V put(String name, V value) {
		if (name == null || name.length() == 0) throw new RuntimeException("name required");
		elements.put(name, adopt(value));
		return value;
	}

	public List addToArray(String name, Object value) {
		List array = getArray(name);
		if (array == null) {
			array = new ArrayList();
			put(name, array);
			return addToArray(name, value);
		}
		array.add(adopt(value));
		return array;
	}

	public boolean removeFromArray(String name, Object value) {
		List array = getArray(name);
		if (array == null) return false;
		return array.remove(value);
	}

	public Object remove(String name) {
		if (name == null || name.length() == 0) throw new RuntimeException("name required");
		return elements.remove(name);
	}

	public JsonObject putNewObject(String name) {
		return put(name, new JsonObject());
	}

	private Object adopt(Object childToAdopt) {
		Object child = Json.convertValue(childToAdopt);
		if (child instanceof JsonObject) {
			((JsonObject) child).parent = this;
			return child;
		}
		if (child instanceof Iterable) {
			List list = new ArrayList();
			for (Object item : ((Iterable) child)) {
				list.add(adopt(item));
			}
			return list;
		}
		return child;
	}

	// --- formating ---

	String toString(int indentation) {
		if (isShort()) indentation = -1;
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		if (indentation >= 0) indentation++;
		boolean first = true;
		for (Map.Entry<String, Object> element : elements.entrySet()) {
			if (first) {
				first = false;
			} else {
				sb.append(',');
			}
			if (indentation >= 0) sb.append('\n');
			Json.indent(sb, indentation);
			sb.append('"').append(Json.escapeString(element.getKey())).append("\":");
			if (indentation >= 0) sb.append(' ');
			sb.append(Json.valueToString(element.getValue(), indentation));
		}
		if (indentation >= 0) {
			indentation--;
			sb.append('\n');
			Json.indent(sb, indentation);
		}
		sb.append('}');
		return sb.toString();
	}

	public String toFormatedString() {
		return toString(0);
	}

	@Override
	public String toString() {
		return toString(-1);
	}

	private boolean isShort() {
		if (elements.size() > 3) return false;
		for (Object value : elements.values()) {
			if (!isPrimitive(value)) return false;
		}
		return true;
	}

	private boolean isPrimitive(Object value) {
		if (value == null) return true;
		if (value instanceof String) return true;
		if (value instanceof Number) return true;
		if (value instanceof Boolean) return true;
		return false;
	}

	// --- parsing ---

	private int parse(String json, int offset) {
		if (json.length() < 2) throw new ParseException("Empty string is invalid", json, 0);
		idx = offset;
		parseWhitespace(json, "'{'");
		if (json.charAt(idx) != '{') throw new ParseException("Expecting '{'", json, idx);
		idx++;
		parseWhitespace(json, "elements or '}'");
		boolean first = true;
		while (json.charAt(idx) != '}') {
			if (first) {
				first = false;
			} else {
				if (json.charAt(idx) == ',') {
					idx++;
				} else {
					throw new ParseException("Expecting ','", json, idx);
				}
			}
			parseElement(json);
			parseWhitespace(json, "',' or '}'");
		}
		idx++;
		return idx;
	}

	private void parseWhitespace(String json, String expectation) {
		idx = Json.getFirstNonWhitespaceIndex(json, idx);
		if (idx < 0) throw new ParseException("Expecting " + expectation, json, idx);
	}

	private void parseElement(String json) {
		parseWhitespace(json, "\"");
		if (json.charAt(idx) != '"') throw new ParseException("Expecting '\"'", json, idx);
		idx++;
		int nameEndIdx = Json.getFirstQuoting(json, idx);
		if (nameEndIdx < 0) throw new ParseException("Unclosed element name", json, idx);
		String name = json.substring(idx, nameEndIdx);
		idx = nameEndIdx + 1;
		parseWhitespace(json, "':'");
		if (json.charAt(idx) != ':')
			throw new ParseException("Expecting ':' after element name \"" + name + "\"", json, idx);
		idx++;
		parseWhitespace(json, "element value");
		Object value = parseValue(json);
		put(name, value);
	}

	private Object parseValue(String json) {
		if (json.startsWith("null", idx)) {
			idx += 4;
			return null;
		} else if (json.startsWith("true", idx)) {
			idx += 4;
			return true;
		} else if (json.startsWith("false", idx)) {
			idx += 5;
			return false;
		} else if (json.charAt(idx) == '"') {
			idx++;
			int valueEndIdx = Json.getFirstQuoting(json, idx);
			if (valueEndIdx < 0) throw new ParseException("Unclosed element string value", json, idx);
			String value = json.substring(idx, valueEndIdx);
			idx = valueEndIdx + 1;
			return Json.parseString(value);
		} else if (json.charAt(idx) == '{') {
			JsonObject value = new JsonObject(json, idx);
			idx = value.idx;
			return value;
		} else if (json.charAt(idx) == '[') {
			List list = new ArrayList();
			idx++;
			while (true) {
				parseWhitespace(json, "array");
				if (json.charAt(idx) == ']') break;
				Object value = parseValue(json);
				list.add(value);
				parseWhitespace(json, "array");
				if (json.charAt(idx) == ']') break;
				if (json.charAt(idx) != ',') throw new ParseException("Expecting array separator ','", json, idx);
				idx++;
			}
			idx++;
			return list;
		} else {
			int len = json.length();
			int endIdx = idx;
			while (endIdx < len) {
				endIdx++;
				char endCh = json.charAt(endIdx);
				if (endCh == ',' || endCh == '}' || endCh == ']' || Json.isWhitespace(endCh)) {
					break;
				}
			}
			String sNumber = json.substring(idx, endIdx);
			Number number;
			try {
				number = Json.parseNumber(sNumber);
			} catch (NumberFormatException ex) {
				throw new ParseException("Expecting number in <" + sNumber + ">", json, idx);
			}
			idx = endIdx;
			return number;
		}
	}

	// --- IO ---

	public void write(OutputStream out, boolean formated) {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		write(writer, formated);
		try {
			writer.flush();
		} catch (IOException ex) {
			throw new RuntimeException("Writing failed", ex);
		}
	}

	public void write(Writer out, boolean formated) {
		write(new PrintWriter(out), formated);
	}

	public void write(File file, boolean formated) {
		File dir = file.getParentFile();
		if (!dir.exists()) {
			if (!dir.mkdirs()) throw new RuntimeException("Creating directory failed: " + dir.getAbsolutePath());
		}
		PrintWriter out;
		try {
			out = new PrintWriter(new FileWriter(file));
		} catch (IOException ex) {
			throw new RuntimeException("Writing file failed: " + file.getAbsolutePath(), ex);
		}
		write(out, formated);
		out.close();
	}

	public void write(PrintWriter out, boolean formated) {
		if (formated) {
			out.print(toFormatedString());
		} else {
			out.print(toString());
		}
		out.flush();
	}

	private static String load(File file) {
		if (!file.exists()) return null;
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = in.readLine()) != null) {
				sb.append(line).append('\n');
			}
			return sb.toString();
		} catch (IOException ex) {
			throw new RuntimeException("Loading file failed: +" + file.getAbsolutePath(), ex);
		}
	}

}