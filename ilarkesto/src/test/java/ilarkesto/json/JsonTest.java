package ilarkesto.json;

import ilarkesto.core.base.Utl;
import ilarkesto.testng.ATest;

import org.testng.annotations.Test;

public class JsonTest extends ATest {

	@Test
	public void parseString() {
		assertEquals(Json.parseString("\\t"), "\t");
		assertEquals(Json.parseString("a \\n \\\\ \\/"), "a \n \\ /");
		assertEquals(Json.parseString("\\u20ac"), "€");
	}

	@Test
	public void escapeString() {
		assertEquals(Json.escapeString("\t"), "\\t");
	}

	@Test
	public void toStringBasics() {
		JsonObject jo = new JsonObject();
		assertEquals(jo.toString(), "{}");
		jo.put("a", "string");
		jo.put("b", null);
		jo.put("c", 23);
		jo.put("d", true);
		assertEquals(jo.toString(), "{\"a\":\"string\",\"b\":null,\"c\":23,\"d\":true}");
	}

	@Test
	public void toStringNested() {
		JsonObject subJo = new JsonObject();
		JsonObject jo = new JsonObject();
		jo.put("sub", subJo);
		assertEquals(jo.toString(), "{\"sub\":{}}");

		subJo.put("a", null);
		assertEquals(jo.toString(), "{\"sub\":{\"a\":null}}");
	}

	@Test
	public void toStringWithEscaping() {
		JsonObject jo = new JsonObject();
		jo.put("a", "this is \"a\"");
		jo.put("b", "new\nline");
		String s = jo.toString();
		assertEquals(s, "{\"a\":\"this is \\\"a\\\"\",\"b\":\"new\\nline\"}");
	}

	@Test
	public void parseWithEscaping() {
		assertEquals(Json.parseString("new\\nline"), "new\nline");
		JsonObject jo = new JsonObject("{\"a\":\"new\\nline\"}");
		assertEquals(jo.get("a"), "new\nline");
	}

	@Test
	public void toStringArray() {
		JsonObject jo = new JsonObject();
		jo.put("list", Utl.toList(1, 2, 3));
		assertEquals(jo.toString(), "{\"list\":[1,2,3]}");
		JsonObject sub1 = new JsonObject();
		JsonObject sub2 = new JsonObject();
		jo.put("subs", Utl.toList(sub1, sub2));
		assertEquals(jo.toString(), "{\"list\":[1,2,3],\"subs\":[{},{}]}");
	}

	@Test
	public void parseEmpty() {
		assertEquals(new JsonObject("{}").toString(), "{}");
		assertEquals(new JsonObject(" { } ").toString(), "{}");
		assertEquals(new JsonObject(" \n\t\r {\n\t}\n } ").toString(), "{}");
	}

	@Test
	public void parseBasic() {
		assertEquals(new JsonObject("{\"a\":null}").toString(), "{\"a\":null}");
		assertEquals(new JsonObject(" {\t\"a\" :\nnull } ").toString(), "{\"a\":null}");
		assertEquals(new JsonObject("{\"a\":true}").toString(), "{\"a\":true}");
		assertEquals(new JsonObject("{\"a\":false}").toString(), "{\"a\":false}");
		assertEquals(new JsonObject("{\"a\":5}").toString(), "{\"a\":5}");
		assertEquals(new JsonObject("{\"a\":5,\"b\":7}").toString(), "{\"a\":5,\"b\":7}");
		assertEquals(new JsonObject("{\"a\":\"string\"}").toString(), "{\"a\":\"string\"}");
		assertEquals(new JsonObject("{\"a\":null,\"b\":null}").toString(), "{\"a\":null,\"b\":null}");
	}

	@Test
	public void parseNested() {
		assertEquals(new JsonObject("{\"sub\":{}}").toString(), "{\"sub\":{}}");
		assertEquals(new JsonObject(" { \"sub\" : { } } ").toString(), "{\"sub\":{}}");
		assertEquals(new JsonObject("{\"sub\":{\"a\":null}}").toString(), "{\"sub\":{\"a\":null}}");
	}

	@Test
	public void parseArray() {
		assertEquals(new JsonObject("{\"list\":[]}").toString(), "{\"list\":[]}");
		assertEquals(new JsonObject("{\"list\":[1,2]}").toString(), "{\"list\":[1,2]}");
		assertEquals(new JsonObject(" { \"list\" : [  ] }").toString(), "{\"list\":[]}");
		assertEquals(new JsonObject("{\"list\":[[]]}").toString(), "{\"list\":[[]]}");
	}

	@Test
	public void getParent() {
		JsonObject witek = new JsonObject();
		JsonObject address = new JsonObject();
		witek.put("address", address);
		assertSame(witek, address.getParent());

		witek = new JsonObject(witek.toString());
		address = witek.getObject("address");
		assertSame(witek, address.getParent());
	}

	@Test
	public void equals() {
		assertEquals(new JsonObject("{\"list\":[[]]}"), new JsonObject("{\"list\":[[]]}"));
		assertNotEquals(new JsonObject("{\"list\":[[{}]]}"), new JsonObject("{\"list\":[[]]}"));
	}

	// --- helper ---

}
