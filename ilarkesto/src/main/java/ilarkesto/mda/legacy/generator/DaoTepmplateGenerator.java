/*
 * Copyright 2011 Witoslaw Koczewsi <wi@koczewski.de>, Artjom Kochtchi
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package ilarkesto.mda.legacy.generator;

import ilarkesto.mda.legacy.model.EntityModel;

public class DaoTepmplateGenerator extends ABeanGenerator<EntityModel> {

	public DaoTepmplateGenerator(EntityModel bean) {
		super(bean);
	}

	@Override
	protected String getName() {
		return bean.getName() + "Dao";
	}

	@Override
	protected boolean isInterface() {
		return false;
	}

	@Override
	protected void writeContent() {}

	@Override
	protected String getSuperclass() {
		return "G" + bean.getName() + "Dao";
	}

	@Override
	protected boolean isAbstract() {
		return bean.isAbstract();
	}

	@Override
	protected boolean isOverwrite() {
		return false;
	}

}
