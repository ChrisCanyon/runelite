/*
 * Copyright (c) 2018, Cas <https://github.com/casvandongen>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.ancientmagichelper;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("Ancient Magic")
public interface AncientMagicHelperConfig extends Config
{
	@ConfigItem(
		keyName = "testConfigItem",
		name = "test config item",
		description = "Test",
		position = 0
	)
	default boolean testConfigItem()
	{
		return true;
	}

	@ConfigItem(
			keyName = "colorTiles",
			name = "color tiles",
			description = "Test",
			position = 0
	)
	default boolean colorTiles()
	{
		return true;
	}

	@ConfigItem(
			keyName = "markerColor",
			name = "Color of the tile",
			description = "Configures the color of marked tile"
	)
	default Color markerColor()
	{
		return Color.RED;
	}

	@ConfigItem(
			keyName = "rememberTileColors",
			name = "Remember color per tile",
			description = "Color tiles using the color from time of placement"
	)
	default boolean rememberTileColors()
	{
		return false;
	}
}
