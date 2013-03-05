/*
 * Copyright 2011 Witoslaw Koczewsi <wi@koczewski.de>
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
package ilarkesto.media.player;

import java.util.List;

public class PlaylistPlayerState {

	private boolean playing;
	private List<PlaylistItem> previousItems;
	private PlaylistItem currentItem;
	private List<PlaylistItem> nextItems;

	public PlaylistPlayerState(boolean playing, List<PlaylistItem> previousItems, PlaylistItem currentItem,
			List<PlaylistItem> nextItems) {
		super();
		this.playing = playing;
		this.previousItems = previousItems;
		this.currentItem = currentItem;
		this.nextItems = nextItems;
	}

	public boolean isPlaying() {
		return playing;
	}

	public List<PlaylistItem> getPreviousItems() {
		return previousItems;
	}

	public PlaylistItem getCurrentItem() {
		return currentItem;
	}

	public List<PlaylistItem> getNextItems() {
		return nextItems;
	}

}
