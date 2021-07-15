/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
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
package net.runelite.client.plugins.efficientfiremaking;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.OverlayMenuClicked;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.efficientfiremaking.services.UIOverlayService;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;


@PluginDescriptor(
	name = "Ancient Magic Helper",
	description = "Overlay useful info when using ancient magics",
	tags = {"ancient", "magic", "magics", "mage"}
)
@PluginDependency(XpTrackerPlugin.class)
@Slf4j
public class AncientMagicHelperPlugin extends Plugin
{
	private static final String CONFIG_GROUP = "groundMarker";
	private static final String REGION_PREFIX = "region_";

	@Getter(AccessLevel.PACKAGE)
	private List<ColorTileMarker> colorTileMarkers;

	@Getter(AccessLevel.PACKAGE)
	private List<GroundMarkerPoint> groundMarkerPoints;

	@Getter
	private final Set<NPC> npcs = new HashSet<>();

	@Inject
	private ConfigManager configManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private HitboxOverlay hitboxOverlay;

	@Inject
	private GroundMarkerOverlay groundMarkerOverlay;

	@Inject
	private Notifier notifier;

	@Inject
	private Client client;

	@Inject
	private InfoBoxManager infoBoxManager;

	@Inject
	private AncientMagicHelperConfig config;

	@Inject
	private ItemManager itemManager;

	@Inject
	private XpTrackerService xpTrackerService;

	private int lastAgilityXp;
	private WorldPoint lastArenaTicketPosition;

	@Getter
	private int agilityLevel;

	@Getter(AccessLevel.PACKAGE)
	private Tile stickTile;

	public AncientMagicHelperPlugin() {
		colorTileMarkers = new ArrayList<>();
		groundMarkerPoints = new ArrayList<>();
	}

	@Provides
    AncientMagicHelperConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AncientMagicHelperConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		if(config.colorTiles()){
			overlayManager.add(groundMarkerOverlay);
		}

	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(groundMarkerOverlay);
		npcs.clear();
	}

	@Subscribe
	public void onOverlayMenuClicked(OverlayMenuClicked overlayMenuClicked)
	{

	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{

	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{

	}

	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{

	}

	@Subscribe
	public void onItemSpawned(ItemSpawned itemSpawned)
	{

	}

	@Subscribe
	public void onItemDespawned(ItemDespawned itemDespawned)
	{

	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
//		clearPoints();
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		onTileObject(event.getTile(), null, event.getGameObject());
	}

	@Subscribe
	public void onGameObjectChanged(GameObjectChanged event)
	{
		onTileObject(event.getTile(), event.getPrevious(), event.getGameObject());
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		onTileObject(event.getTile(), event.getGameObject(), null);
	}

	@Subscribe
	public void onGroundObjectSpawned(GroundObjectSpawned event)
	{
		onTileObject(event.getTile(), null, event.getGroundObject());
	}

	@Subscribe
	public void onGroundObjectChanged(GroundObjectChanged event)
	{
		onTileObject(event.getTile(), event.getPrevious(), event.getGroundObject());
	}

	@Subscribe
	public void onGroundObjectDespawned(GroundObjectDespawned event)
	{
		onTileObject(event.getTile(), event.getGroundObject(), null);
	}

	@Subscribe
	public void onWallObjectSpawned(WallObjectSpawned event)
	{
		onTileObject(event.getTile(), null, event.getWallObject());
	}

	@Subscribe
	public void onWallObjectChanged(WallObjectChanged event)
	{
		onTileObject(event.getTile(), event.getPrevious(), event.getWallObject());
	}

	@Subscribe
	public void onWallObjectDespawned(WallObjectDespawned event)
	{
		onTileObject(event.getTile(), event.getWallObject(), null);
	}

	@Subscribe
	public void onDecorativeObjectSpawned(DecorativeObjectSpawned event)
	{
		onTileObject(event.getTile(), null, event.getDecorativeObject());
	}

	@Subscribe
	public void onDecorativeObjectChanged(DecorativeObjectChanged event)
	{
		onTileObject(event.getTile(), event.getPrevious(), event.getDecorativeObject());
	}

	@Subscribe
	public void onDecorativeObjectDespawned(DecorativeObjectDespawned event)
	{
		onTileObject(event.getTile(), event.getDecorativeObject(), null);
	}

	private void onTileObject(Tile tile, TileObject oldObject, TileObject newObject)
	{

	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		NPC npc = npcSpawned.getNpc();
		npcs.add(npc);
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		NPC npc = npcDespawned.getNpc();
		npcs.remove(npc);
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		Tile target = client.getSelectedSceneTile();
		if (target == null) {
			return;
		}

		client.getLocalPlayer().getWorldLocation();
		markTiles(UIOverlayService.createRectangleWorldPointList(1,1, client.getLocalPlayer().getWorldLocation(), true));

//		return;

		if((event.getOption().startsWith("Cast")
		|| event.getTarget().contains("Burst")
		|| event.getTarget().contains("Barrage"))
		&& event.getTarget().contains("->")
		&& event.getTarget().contains("col=ffff00")
		){
			for (NPC npc : client.getNpcs()	) {
				if(event.getIdentifier() == npc.getIndex()){
					List<WorldPoint> worldPoints = new ArrayList<>();
					WorldPoint centralPoint = npc.getWorldLocation();

					if(event.getTarget().contains("Burst") || event.getTarget().contains("Barrage")){
						worldPoints.addAll(UIOverlayService.createRectangleWorldPointList(3,3, centralPoint, true));
					}else{
						worldPoints.add(centralPoint);
					}
					markTiles(worldPoints);
					return;
				}
			}
		}
	}

	private void markTiles(List<WorldPoint> worldPoints)
	{
		if (worldPoints == null)
		{
			return;
		}

		groundMarkerPoints.clear();

		for (WorldPoint wp : worldPoints) {
			GroundMarkerPoint point = new GroundMarkerPoint(wp.getRegionID(), wp.getRegionX(), wp.getRegionY(), client.getPlane(), config.markerColor(), null);
			groundMarkerPoints.add(point);
		}

		convertGroundMarkerToTileMarker();
	}

	private void clearPoints(){

		colorTileMarkers.clear();
		groundMarkerPoints.clear();
	}

	private void convertGroundMarkerToTileMarker()
	{
		colorTileMarkers.clear();
		colorTileMarkers.addAll(translateToColorTileMarker(groundMarkerPoints));
	}

	private Collection<ColorTileMarker> translateToColorTileMarker(Collection<GroundMarkerPoint> points)
	{
		if (points.isEmpty())
		{
			return Collections.emptyList();
		}

		return points.stream()
				.map(point -> new ColorTileMarker(
						WorldPoint.fromRegion(point.getRegionId(), point.getRegionX(), point.getRegionY(), point.getZ()),
						point.getColor(), point.getLabel()))
				.flatMap(colorTile ->
				{
					final Collection<WorldPoint> localWorldPoints = WorldPoint.toLocalInstance(client, colorTile.getWorldPoint());
					return localWorldPoints.stream().map(wp -> new ColorTileMarker(wp, colorTile.getColor(), colorTile.getLabel()));
				})
				.collect(Collectors.toList());
	}
}
