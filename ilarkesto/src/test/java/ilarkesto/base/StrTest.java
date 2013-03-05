/*
 * Copyright 2011 Witoslaw Koczewsi <wi@koczewski.de>, Artjom Kochtchi
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package ilarkesto.base;

import ilarkesto.core.html.Html;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StrTest extends Assert {

	@Test
	public void getLine() {
		String s = "line 0\nline 1\r\nline 2\nline 3";
		assertEquals(Str.getLine(s, 0, 100, null), "line 0");
		assertEquals(Str.getLine(s, 1, 100, null), "line 1");
		assertEquals(Str.getLine(s, 2, 100, null), "line 2");
		assertEquals(Str.getLine(s, 3, 100, null), "line 3");
	}

	@Test
	public void htmlToText() {
		String html = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html><head><title>HTML Page</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-15\"><style type=\"text/css\">\n"
				+ "<!--\n"
				+ "hr { border: 0; border-top: thin solid #DEC822; }</style><head><body><em>Hello world</em></body></html>";
		String text = Html.convertHtmlToText(html);
		System.out.println(text);
		assertEquals(text, "Hello world");
	}

	@Test
	public void getLeadingSpaces() {
		assertEquals(Str.getLeadingSpaces("   a"), "   ");
		assertEquals(Str.getLeadingSpaces("   "), "   ");
		assertEquals(Str.getLeadingSpaces("a"), "");
		assertEquals(Str.getLeadingSpaces(""), "");
	}

	@Test
	public void cutFromTo() {
		assertEquals(Str.cutFromTo("Hello <em>world</em>!", "<em>", "</em>"), "world");
	}

}
