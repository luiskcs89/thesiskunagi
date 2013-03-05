package ilarkesto.core.base;

public class Parser {

	protected String data;
	protected int pos = 0;

	public Parser(String data) {
		super();
		if (data == null) throw new IllegalArgumentException("data == null");
		this.data = data;
	}

	public boolean isBefore(String s, String... others) {
		int idx = data.indexOf(s, pos);
		if (idx < 0) return false;
		int othersIdx = indexOf(data, pos, others);
		if (othersIdx < 0) return true;
		return idx < othersIdx;
	}

	public boolean isNext(String s) {
		return data.startsWith(s, pos);
	}

	public String getUntilIf(String... ss) {
		try {
			return getUntil(ss);
		} catch (ParseException e) {
			return null;
		}
	}

	public String getUntil(String... ss) throws ParseException {
		int idx = indexOf(data, pos, ss);
		if (idx < 0) throw new ParseException("getUntil <" + ss + "> failed.", pos, data);
		return data.substring(pos, idx);
	}

	public String getUntilAndGotoAfter(String... ss) throws ParseException {
		String ret = getUntil(ss);
		gotoAfter(ss);
		return ret;
	}

	private int indexOf(String s, int start, String... subs) {
		int lowest = -1;
		for (String sub : subs) {
			int i = s.indexOf(sub, start);
			if (lowest == -1 || i < lowest) lowest = i;
		}
		return lowest;
	}

	public String getNext(int count) {
		return data.substring(pos, pos + count);
	}

	public void skip(int count) {
		pos += count;
	}

	public String getUntilAfterIf(String until, String after) {
		try {
			return getUntilAfter(until, after);
		} catch (ParseException e) {
			return null;
		}
	}

	public String getUntilAfter(String until, String after) throws ParseException {
		int idx = data.indexOf(after, pos);
		if (idx < 0) { throw new ParseException("getUntilAfter after <" + until + "> <" + after + "> failed.", pos,
				data); }
		idx = data.indexOf(until, idx + after.length());
		if (idx < 0) { throw new ParseException("getUntilAfter until <" + until + "> <" + after + "> failed: <"
				+ getTail() + ">", pos, data); }
		return data.substring(pos, idx);
	}

	public boolean gotoAfterIf(String... ss) {
		try {
			gotoAfter(ss);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public void gotoAfter(String... ss) throws ParseException {
		int idx = -1;
		int len = 0;
		for (String sub : ss) {
			int i = data.indexOf(sub, pos);
			if (idx == -1 || i < idx) {
				idx = i;
				len = sub.length();
			}
		}
		if (idx < 0) throw new ParseException("gotoAfter <" + format(ss) + "> failed", pos, data);
		pos = idx + len;
	}

	private String format(String[] ss) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean first = true;
		for (String s : ss) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append("\"");
			sb.append(s);
			sb.append("\"");
		}
		sb.append("]");
		return sb.toString();
	}

	public void gotoAfterIfNext(String s) {
		if (isNext(s)) {
			try {
				gotoAfter(s);
			} catch (ParseException ex) {
				throw new IllegalStateException(ex);
			}
		}
	}

	public void gotoAfterWhileNext(String s) {
		while (isNext(s)) {
			try {
				gotoAfter(s);
			} catch (ParseException ex) {
				throw new IllegalStateException(ex);
			}
		}
	}

	@Deprecated
	public static String text(String s) {
		if (s == null) return null;
		s = s.trim();
		s = s.replace("<br />", "\n");
		s = s.replace("&#167;", "§");
		s = s.replace("&#228;", "ä");
		s = s.replace("&auml;", "ä");
		s = s.replace("&#196;", "Ä");
		s = s.replace("&Auml;", "ä");
		s = s.replace("&#252;", "ü");
		s = s.replace("&uuml;", "ü");
		s = s.replace("&#220;", "Ü");
		s = s.replace("&Uuml;", "ü");
		s = s.replace("&#246;", "ö");
		s = s.replace("&ouml;", "ö");
		s = s.replace("&#214;", "Ö");
		s = s.replace("&Ouml;", "ö");
		s = s.replace("&#223;", "ß");
		s = s.replace("&nbsp;", " ");
		s = s.replace("&amp;", "&");
		return s;
	}

	public int getPos() {
		return pos;
	}

	public void gotoPos(int pos) {
		this.pos = pos;
	}

	public String getData() {
		return data;
	}

	public String getTail() {
		return data.substring(getPos());
	}

	public static class ParseException extends Exception {

		private static final long serialVersionUID = 1L;

		public ParseException(String message) {
			super(message);
		}

		public ParseException(String message, int pos, String data) {
			super(message + "\n@" + pos + ": " + data);
		}
	}

}
