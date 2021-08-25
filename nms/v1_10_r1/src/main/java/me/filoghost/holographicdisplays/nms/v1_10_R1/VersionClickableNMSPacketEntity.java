/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.nms.v1_10_R1;

import me.filoghost.holographicdisplays.common.PositionCoordinates;
import me.filoghost.holographicdisplays.nms.common.EntityID;
import me.filoghost.holographicdisplays.nms.common.NMSPacketList;
import me.filoghost.holographicdisplays.nms.common.entity.ClickableNMSPacketEntity;

public class VersionClickableNMSPacketEntity implements ClickableNMSPacketEntity {

    private final EntityID slimeID;

    public VersionClickableNMSPacketEntity(EntityID slimeID) {
        this.slimeID = slimeID;
    }

    @Override
    public EntityID getID() {
        return slimeID;
    }

    @Override
    public void addSpawnPackets(NMSPacketList packetList, PositionCoordinates position) {
        packetList.add(EntityLivingSpawnNMSPacket.builder(slimeID, EntityTypeID.SLIME, position, SLIME_Y_OFFSET)
                .setInvisible()
                .setSlimeSmall() // Required for a correct client-side collision box
                .build()
        );
    }

    @Override
    public void addTeleportPackets(NMSPacketList packetList, PositionCoordinates position) {
        packetList.add(new EntityTeleportNMSPacket(slimeID, position, SLIME_Y_OFFSET));
    }

    @Override
    public void addDestroyPackets(NMSPacketList packetList) {
        packetList.add(new EntityDestroyNMSPacket(slimeID));
    }

}
