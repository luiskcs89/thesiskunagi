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
package ilarkesto.richtext;

import ilarkesto.base.Str;
import ilarkesto.core.html.Html;
import ilarkesto.integration.links.MultiLinkConverter;

import java.util.List;

public class RichTextFormatter {

	public static String toHtml(String s) {
		if (s == null) return null;
		if (!s.startsWith("<html")) {
			s = textToHtml(s);
		}
		String html = Html.cutHtmlAndHeaderAndBody(s);
		html = Str.activateLinksInHtml(html, MultiLinkConverter.ALL);
		return html;
	}

	public static String textToHtml(String s) {
		StringBuilder sb = new StringBuilder();
		List<String> lines = Str.toStringList(s);
		boolean inQuote = false;
		for (String line : lines) {
			if (line.startsWith("> ")) {
				line = line.substring(2);
				if (!inQuote) {
					sb.append("<blockquote><i>");
					inQuote = true;
				}
				sb.append(Str.toHtml(line)).append("<br>");
			} else {
				sb.append(Str.toHtml(line));
				if (inQuote) {
					sb.append("</i></blockquote>");
					inQuote = false;
				} else {
					sb.append("<br>");
				}
			}
		}
		return sb.toString();
	}
}
